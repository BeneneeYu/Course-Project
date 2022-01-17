package com.softwaretest.demo.Service;

import com.softwaretest.demo.Controller.Response.AccountDetailsResponse;
import com.softwaretest.demo.Controller.Response.AccountResponse;
import com.softwaretest.demo.Controller.Response.InstallmentResponse;
import com.softwaretest.demo.Domain.Account;
import com.softwaretest.demo.Domain.Flow;
import com.softwaretest.demo.Domain.Installment;
import com.softwaretest.demo.Domain.Loan;
import com.softwaretest.demo.Repository.AccountRepository;
import com.softwaretest.demo.Repository.FlowRepository;
import com.softwaretest.demo.Repository.InstallmentRepository;
import com.softwaretest.demo.Repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

@Service
public class LoanService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private InstallmentRepository installmentRepository;

    @Autowired
    private FlowRepository flowRepository;

    @Autowired
    public LoanService(){

    }

    public List<AccountResponse> getAccounts(String idNumber){
        List<Account> accounts = accountRepository.findByIdNumber(idNumber);
        List<AccountResponse> accountDetailsResponses = new LinkedList<>();
        for(Account account: accounts){
            accountDetailsResponses.add(new AccountResponse(account.getAccountId(),account.getCustomerName(),account.getBalance(),account.getType(),getGrade(account)));
        }
        return accountDetailsResponses; }
    public List<AccountDetailsResponse> getLoans(Long accountId){
        List<Loan> loans = loanRepository.findByAccountId(accountId);
        if(loans.size() == 0){
                return null;
        }
        List<AccountDetailsResponse> accountDetailsResponses = new LinkedList<>();
        for(Loan loan:loans){
            double fine = getFine(loan);
            List<InstallmentResponse> installmentResponses = new LinkedList<>();
            List<Installment> installments = loan.getInstallments();
            for(int i = 0;i<installments.size();i++){
                if(isPaid(installments.get(i))){
                    InstallmentResponse installmentResponse = new InstallmentResponse(installments.get(i).getAmount(),installments.get(i).getAmountRemained(),installments.get(i).getDeadline(),i);
                    installmentResponses.add(installmentResponse);
                }
            }
            AccountDetailsResponse accountDetailsResponse = new AccountDetailsResponse(loan.getId(),loan.getAmount(),installmentResponses,loan.getInterestRate(),loan.getStartDate(),fine);
            accountDetailsResponses.add(accountDetailsResponse);
        }
        return accountDetailsResponses;
    }

  public boolean payFine(Long loanId,Double amount){ Loan loan = loanRepository.findById(loanId).orElse(null);
        if(loan == null){
            return false; }
        Account account = accountRepository.findById(loan.getAccountId()).orElse(null);
        if(account.getBalance()<amount){
            return false; }
        List<Installment> installments = loan.getInstallments();
        for(Installment installment:installments){ if(isExpired(installment)&&!installment.getFineHasPaid()){ installment.setFineHasPaid(true); } }
        double remainBalance = Double.parseDouble(String.format("%.2f",account.getBalance()-amount));
        account.setBalance(remainBalance);
        installmentRepository.saveAll(installments);
        loan.setInstallments(installments);
        loanRepository.save(loan);
        accountRepository.save(account);
        Flow flow = new Flow("贷款罚金缴纳",account.getAccountId(),amount,new Timestamp(System.currentTimeMillis()));
        flowRepository.save(flow);
        return true;
    }

    public double getFine(Loan loan){
        double result = 0;
        List<Installment> installments = loan.getInstallments();
        for(Installment installment: installments){
            if(isExpired(installment)&&!installment.getFineHasPaid()){
                result +=Double.parseDouble(String.format("%.2f",installment.getAmountRemained()*0.05)) ; }
        }
        return result;
    }

    public String repay(Long accountId,Long loanId,Integer index,Double amount){ Account account = accountRepository.findById(accountId).orElse(null);
        if(account == null){
            return "付款账号不存在"; }
        Loan loan = loanRepository.findById(loanId).orElse(null);
        if(loan == null){
            return "贷款不存在"; }
        double balance = account.getBalance();
        if(balance<amount){
            return "余额不足"; }
        List<Installment> installments = loan.getInstallments();
        if(index>=installments.size()||index<0){
            return "分期索引非法"; }
        Installment installment = installments.get(index);
        double amountPaid = Math.min(amount,installment.getAmountRemained());
        double remainAmount = Double.parseDouble(String.format("%.2f",installment.getAmountRemained()-amountPaid));
        double remainBalance = Double.parseDouble(String.format("%.2f",account.getBalance()-amountPaid));
        installment.setAmountRemained(remainAmount);
        account.setBalance(remainBalance);
        Flow flow = new Flow("贷款还款",accountId,amountPaid,new Timestamp(System.currentTimeMillis()));
        installmentRepository.save(installment);
        accountRepository.save(account);
        flowRepository.save(flow);
        return "success"; }
    public boolean isPaid(Installment installment){ return !(installment.getAmountRemained() < 0.01); }

    public boolean isExpired(Installment installment){
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Timestamp expiredTime = installment.getDeadline();
        return expiredTime.before(currentTime)&& isPaid(installment);
    }

    public int getGrade(Account account){ Double balance = account.getBalance();
        List<Loan> loans = loanRepository.findByAccountId(account.getAccountId());
        for(Loan loan:loans){
            List<Installment> installments = loan.getInstallments();
            for(Installment installment:installments){
                balance -= installment.getAmountRemained();
            }
        }
        if(balance>=500000.00){
            return 1; }
        else if(balance>=0.00){
            return 2; }
        return 3; }}
