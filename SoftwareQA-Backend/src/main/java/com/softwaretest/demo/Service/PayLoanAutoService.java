package com.softwaretest.demo.Service;

import java.sql.Timestamp;
import java.util.*;

import javax.transaction.Transactional;

import com.softwaretest.demo.Controller.Response.AccountDetailsResponse;
import com.softwaretest.demo.Controller.Response.AccountResponse;
import com.softwaretest.demo.Controller.Response.InstallmentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softwaretest.demo.Domain.Account;
import com.softwaretest.demo.Domain.Flow;
import com.softwaretest.demo.Domain.Installment;
import com.softwaretest.demo.Domain.Loan;
import com.softwaretest.demo.Repository.AccountRepository;
import com.softwaretest.demo.Repository.FlowRepository;
import com.softwaretest.demo.Repository.InstallmentRepository;
import com.softwaretest.demo.Repository.LoanRepository;

@Service
public class PayLoanAutoService {
  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private LoanRepository loanRepository;

  @Autowired
  private InstallmentRepository installmentRepository;

  @Autowired
  private FlowRepository flowRepository;
  @Autowired
  public PayLoanAutoService() {
  }

  public Set<Flow> payLoanAutomatically(){
    HashSet<Flow> flows = new HashSet<>();
    a:for (Loan loan : loanRepository.findAll()) {
      Long accountID = loan.getAccountId();
      Account account = accountRepository.findById(accountID).orElse(null);
      if (null == account)continue a;
      List<Installment> installments = loan.getInstallments();
      if (installments.isEmpty()) continue a;
      HashSet<Long> installmentIDs = new HashSet<>();
      for (Installment installment : installments) {
        installmentIDs.add(installment.getId());
      }
      for (Long installmentID : installmentIDs) {
        Installment installment = installmentRepository.findById(installmentID).orElse(null);
        //分期过期且没还钱
        if (isExpired(installment)){
          double fine = getFine(loan);
          //没有交罚款
          if (fine > 0 && account.getBalance() >= fine){
            Flow tmpflow = payFine(loan.getId(),fine,true);
            if (null != tmpflow){
              flows.add(tmpflow);
              flowRepository.save(tmpflow);
            }else{
              continue a;
            }
          }
          double amountRemained = installment.getAmountRemained();
          if (account.getBalance() >= amountRemained){
              Flow flow = new Flow("贷款还款",account.getAccountId(),amountRemained,new Timestamp(System.currentTimeMillis()));
              flows.add(flow);
              flowRepository.save(flow);
              payBill(installment,account);
          }
        }
      }

    }
    return flows;
  }

  @Transactional
  public void payBill(Installment installment,Account account){
      account.setBalance(account.getBalance()-installment.getAmountRemained());
      installment.setAmountRemained(0.0);
      accountRepository.save(account);
      installmentRepository.save(installment);
  }




  public Flow payFine(Long loanId, Double amount, boolean overload) {
    Loan loan = loanRepository.findById(loanId).orElse(null);
    if (null == loan) {
      return null;
    }
    Account account = accountRepository.findById(loan.getAccountId()).orElse(null);
    if (null == account) {
      return null;
    }
    if (account.getBalance() < amount) {
      return null;
    }
    List<Installment> installments = loan.getInstallments();
    for (Installment installment : installments) {
      if (isExpired(installment)) {
        if (installment.getFineHasPaid()) continue;
        installment.setFineHasPaid(true);
      }
    }
    double remainBalance = Double.parseDouble(String.format("%.2f", account.getBalance() - amount));
    account.setBalance(remainBalance);
    installmentRepository.saveAll(installments);
    loan.setInstallments(installments);
    loanRepository.save(loan);
    accountRepository.save(account);

    Flow flow = new Flow("罚金缴纳", account.getAccountId(), amount, new Timestamp(System.currentTimeMillis()));
    flowRepository.save(flow);
    return flow;
  }


    /*
    @Description : 获取罚金
     */

  public double getFine(Loan loan){
    double result = 0;
    List<Installment> installments = loan.getInstallments();
    for(Installment installment: installments){
      if(isExpired(installment)&&!installment.getFineHasPaid()){
        result +=Double.parseDouble(String.format("%.2f",installment.getAmountRemained()*0.05)) ;
      }
    }
    return result;
  }



    /*
    @Description : 判断该贷款的某个分期是否过期
     */

  public boolean isPaid(Installment installment){
    return installment.getAmountRemained() <0.01;
  }


  public boolean isExpired(Installment installment){
    Timestamp currentTime = new Timestamp(System.currentTimeMillis());
    Timestamp expiredTime = installment.getDeadline();
    return expiredTime.before(currentTime)&&!isPaid(installment);
  }

}