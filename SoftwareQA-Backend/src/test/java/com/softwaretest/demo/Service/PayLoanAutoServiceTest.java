package com.softwaretest.demo.Service;

import com.softwaretest.demo.Controller.Response.AccountDetailsResponse;
import com.softwaretest.demo.DemoApplication;
import com.softwaretest.demo.Domain.Account;
import com.softwaretest.demo.Domain.Flow;
import com.softwaretest.demo.Domain.Installment;
import com.softwaretest.demo.Domain.Loan;
import com.softwaretest.demo.Repository.AccountRepository;
import com.softwaretest.demo.Repository.FlowRepository;
import com.softwaretest.demo.Repository.InstallmentRepository;
import com.softwaretest.demo.Repository.LoanRepository;
//import org.apache.shiro.crypto.hash.Hash;
//import org.apache.shiro.util.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.util.*;

/**
 * @program: demo
 * @description:
 * @author: Shen Zhengyu
 * @create: 2021-04-11 11:10
 **/
@SpringBootTest
public class PayLoanAutoServiceTest {

    @Autowired
    private LoanService loanService;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private InstallmentRepository installmentRepository;

    @Autowired
    private FlowRepository flowRepository;
    @Autowired
    private PayLoanAutoService payLoanAutoService;

    private Timestamp castStringToTimeStamp(String timeStr) {
        return Timestamp.valueOf(timeStr);
    }


    /**
     * Method: payLoanAutomatically()
     */
    @Test
    @Rollback()
    public void testPayLoanAutomatically() throws Exception {
        String toAddId = new String();
        for (int i = 0; i < 10000; i++) {
            String randomId = Integer.toString((int) (Math.random() * 900000 + 100000));
            if (accountRepository.findByIdNumber(randomId).isEmpty()) {
                toAddId = randomId;
            }
        }
        if ("".equals(toAddId)) return;
        Loan loanWithoutAccount = new Loan();
        loanWithoutAccount.setAccountId(Long.parseLong(toAddId));
        loanWithoutAccount.setAmount(12000.00);
        loanWithoutAccount.setInterestRate(0.05);
        loanWithoutAccount.setStartDate(castStringToTimeStamp("2021-01-23 00:00:00"));
        loanRepository.save(loanWithoutAccount);
        payLoanAutoService.payLoanAutomatically();
        loanRepository.delete(loanWithoutAccount);
        List<Account> accounts = new ArrayList<>();
        Account accountToAdd = new Account();
        accountToAdd.setIdNumber(toAddId);
        accountToAdd.setType("储蓄");
        accountToAdd.setBalance(1.00);
        accountToAdd.setCustomerName("沈征宇");
        accountRepository.save(accountToAdd);
        accounts = accountRepository.findByIdNumber(toAddId);
        Account account = accounts.get(0);
        Loan loan = new Loan();
        loan.setAccountId(account.getAccountId());
        loan.setAmount(12000.00);
        loan.setInterestRate(0.05);
        loan.setStartDate(castStringToTimeStamp("2021-01-23 00:00:00"));
        payLoanAutoService.payLoanAutomatically();
        List<Installment> installments = new LinkedList<>();
        loan.setInstallments(installments);
        loanRepository.save(loan);
        payLoanAutoService.payLoanAutomatically();
        Installment installment0 = new Installment(4200.00, 4200.00, castStringToTimeStamp("2021-02-23 00:00:00"));
        installmentRepository.save(installment0);
        Long tmpId = installment0.getId();
        installments.add(installment0);
        loan.setInstallments(installments);
        loanRepository.save(loan);
        payLoanAutoService.payLoanAutomatically();
        installmentRepository.save(installment0);
        installments.clear();
        loan.setInstallments(installments);
        installment0.setId(tmpId);
        installmentRepository.save(installment0);
        Installment installment1 = new Installment(4200.00, 4200.00, castStringToTimeStamp("2021-03-23 00:00:00"));
        Installment installment2 = new Installment(4200.00, 4200.00, castStringToTimeStamp("2021-04-23 00:00:00"));
        Installment installment3 = new Installment(4200.00, 4200.00, castStringToTimeStamp("2021-11-23 00:00:00"));
        installmentRepository.save(installment1);
        installmentRepository.save(installment2);
        installmentRepository.save(installment3);


        installments.add(installment0);
        installments.add(installment1);
        installments.add(installment2);
        installments.add(installment3);
        loan.setInstallments(installments);
        loanRepository.save(loan);

        Flow flow = new Flow("贷款发放", account.getAccountId(), 12000.00, castStringToTimeStamp("2021-01-23 00:00:00"));
        flowRepository.save(flow);

        Set<Flow> flows = payLoanAutoService.payLoanAutomatically();
        System.out.println("补充额度前：");
        for (Flow flowToShow : flows ) {
            System.out.println(flowToShow);
        }
        accountToAdd.setBalance((double) 700);
        System.out.println("补充额度后1：");
        accountRepository.save(accountToAdd);
        flows = payLoanAutoService.payLoanAutomatically();
        for (Flow flowToShow : flows ) {
            System.out.println(flowToShow);
        }
        accountToAdd.setBalance(accountToAdd.getBalance()+700);
        System.out.println("补充额度后1：");
        accountRepository.save(accountToAdd);
        flows = payLoanAutoService.payLoanAutomatically();
        for (Flow flowToShow : flows ) {
            System.out.println(flowToShow);
        }
        accountToAdd.setBalance(500000.00);
        System.out.println("补充额度后2：");
        accountRepository.save(accountToAdd);
        flows = payLoanAutoService.payLoanAutomatically();
        for (Flow flowToShow : flows ) {
            System.out.println(flowToShow);
        }
        Assert.isNull(payLoanAutoService.payFine((long) 1,null,true));
        payLoanAutoService.payLoanAutomatically();
        for (int i = 0; i < 10000; i++) {
            Long loanIdDoesNotExist = (long) (Math.random() * 9000000 + 1000000);
            if (null == loanRepository.findById(loanIdDoesNotExist).orElse(null)){
                Assert.isNull(payLoanAutoService.payFine(loanIdDoesNotExist,1.0,false));
                break;
            }
        }

        for (Loan loan1 : loanRepository.findAll()) {
            Assert.isNull(payLoanAutoService.payFine(loan1.getId(),Integer.MAX_VALUE-1.0,true));
            break;
        }

        Installment installment10 = new Installment(4200.00, 0.00, castStringToTimeStamp("2021-02-23 00:00:00"));
        installmentRepository.save(installment10);
        Loan loan10 = new Loan();
        List<Installment> installments10 = new ArrayList<>();
        installments10.add(installment10);
        loan10.setInstallments(installments10);
        loan10.setAccountId(accountToAdd.getAccountId());
        loan10.setAmount(4000.00);
        loan10.setInterestRate(0.05);
        loan10.setStartDate(castStringToTimeStamp("2021-01-23 00:00:00"));
        loanRepository.save(loan10);
        payLoanAutoService.payLoanAutomatically();
        Installment installment20 = new Installment(4200.00, 4200.00, castStringToTimeStamp("2021-02-23 00:00:00"));
        installment20.setFineHasPaid(true);
        installmentRepository.save(installment20);
        Loan loan20 = new Loan();
        List<Installment> installments20 = new ArrayList<>();
        installments20.add(installment20);
        loan20.setInstallments(installments20);
        loan20.setAccountId(accountToAdd.getAccountId());
        loan20.setAmount(4000.00);
        loan20.setInterestRate(0.05);
        loan20.setStartDate(castStringToTimeStamp("2021-01-23 00:00:00"));
        loanRepository.save(loan20);
        payLoanAutoService.payFine(loan20.getId(),1.00,true);
    }

    @Test
    @Rollback()
    public void testPayFine() throws Exception{
        Account account1 = new Account();
        account1.setIdNumber("123456123456");
        account1.setType("储蓄");
        account1.setBalance(200000.00);
        account1.setCustomerName("郭泰安");
        accountRepository.save(account1);


        //第一笔贷款，用于测试手动还款
        Loan loan = new Loan();
        loan.setAccountId(account1.getAccountId());
        loan.setAmount(12000.00);
        loan.setInterestRate(0.05);
        loan.setStartDate(castStringToTimeStamp("2021-02-28 00:00:00"));

        Installment installment1 = new Installment(4200.00,4200.00,castStringToTimeStamp("2021-03-28 00:00:00"));
        Installment installment2 = new Installment(4200.00,4200.00,castStringToTimeStamp("2021-04-28 00:00:00"));
        Installment installment3 = new Installment(4200.00,4200.00,castStringToTimeStamp("2021-05-28 00:00:00"));
        List<Installment> installments = new LinkedList<>();
        installmentRepository.save(installment1);
        installmentRepository.save(installment2);
        installmentRepository.save(installment3);
        installments.add(installment1); installments.add(installment2); installments.add(installment3);
        loan.setInstallments(installments);
        loanRepository.save(loan);
        Flow flow1 = new Flow("贷款发放",account1.getAccountId(),12000.00,castStringToTimeStamp("2021-02-28 00:00:00"));
        flowRepository.save(flow1);

        //第二笔贷款，用于测试自动还款
        Loan loan2 = new Loan();
        loan2.setAccountId(account1.getAccountId());
        loan2.setAmount(12000.00);
        loan2.setInterestRate(0.05);
        loan2.setStartDate(castStringToTimeStamp("2021-02-28 00:00:00"));

        Installment installment4 = new Installment(4200.00,4200.00,castStringToTimeStamp("2021-03-29 00:00:00"));
        Installment installment5 = new Installment(4200.00,4200.00,castStringToTimeStamp("2021-04-29 00:00:00"));
        Installment installment6 = new Installment(4200.00,4200.00,castStringToTimeStamp("2021-05-29 00:00:00"));
        List<Installment> installments2 = new LinkedList<>();
        installmentRepository.save(installment4);
        installmentRepository.save(installment5);
        installmentRepository.save(installment6);
        installments2.add(installment4); installments2.add(installment5); installments2.add(installment6);
        loan2.setInstallments(installments2);
        loanRepository.save(loan2);

        Flow flow2 = new Flow("贷款发放",account1.getAccountId(),12000.00,castStringToTimeStamp("2021-02-28 00:00:00"));
        flowRepository.save(flow2);



        //

        Account account2 = new Account();
        account2.setIdNumber("567890567890");
        account2.setType("储蓄");
        account2.setBalance(100.00);
        account2.setCustomerName("郭泰安");
        accountRepository.save(account2);

        Loan loan3 = new Loan();
        loan3.setAccountId(account2.getAccountId());
        loan3.setAmount(12000.00);
        loan3.setInterestRate(0.05);
        loan3.setStartDate(castStringToTimeStamp("2021-02-28 00:00:00"));
        Installment installment7 = new Installment(4200.00,4200.00,castStringToTimeStamp("2021-03-28 00:00:00"));
        Installment installment8 = new Installment(4200.00,4200.00,castStringToTimeStamp("2021-04-28 00:00:00"));
        Installment installment9 = new Installment(4200.00,4200.00,castStringToTimeStamp("2021-05-28 00:00:00"));
        List<Installment> installments3 = new LinkedList<>();
        installmentRepository.save(installment7);
        installmentRepository.save(installment8);
        installmentRepository.save(installment9);
        installments3.add(installment7); installments3.add(installment8); installments3.add(installment9);
        loan3.setInstallments(installments3);
        loanRepository.save(loan3);

        Flow flow3 = new Flow("贷款发放",account2.getAccountId(),12000.00,castStringToTimeStamp("2021-02-28 00:00:00"));
        flowRepository.save(flow3);
        Account account3 = new Account();
        account3.setBalance(20000000.00);
        account3.setIdNumber("234567234567");
        account3.setType("储蓄");
        account3.setCustomerName("土豪");
        accountRepository.save(account3);

        List<Account> accounts = accountRepository.findByIdNumber("123456123456");
        Long accountId = accounts.get(0).getAccountId();
        List<AccountDetailsResponse> responses2 = loanService.getLoans(accountId);
        Loan loan1 =loanRepository.findById( responses2.get(0).getLoanId()).orElse(null);
        Assert.isTrue(loan1!=null);
        Long loan1ID = loan1.getAccountId();
        Long accountIdDoesNotExist = (long) (Math.random() * 900000000 + 100000000);
        loan1.setAccountId(accountIdDoesNotExist);
        loanRepository.save(loan1);
        Assert.isTrue(null == payLoanAutoService.payFine(loan1.getId(),responses2.get(0).getFine(),true));
        loan1.setAccountId(loan1ID);
        loanRepository.save(loan1);
        boolean isPayFineSuccess1 = null == payLoanAutoService.payFine(loan1.getId(),responses2.get(0).getFine(),true);
        Assert.isTrue(!isPayFineSuccess1);

        //测试贷款Id不存在
        for (int i = 0; i < 10000; i++) {
            Long loanIdDoesNotExist = (long) (Math.random() * 9000000 + 1000000);
            if (null == loanRepository.findById(loanIdDoesNotExist).orElse(null)){
                boolean isPayFineSuccess2 = null == payLoanAutoService.payFine(loanIdDoesNotExist,10.00,true);
                Assert.isTrue(isPayFineSuccess2);
                break;
            }
        }


        //测试余额不足，缴纳罚款失败
        List<Account> accounts2 = accountRepository.findByIdNumber("567890567890");
        Long accountId2 = accounts2.get(0).getAccountId();
        List<AccountDetailsResponse> responses3 = loanService.getLoans(accountId2);
        Loan loan22 =loanRepository.findById( responses3.get(0).getLoanId()).orElse(null);
        Assert.isTrue(loan22!=null);
        boolean isPayFineSuccess3 = null == payLoanAutoService.payFine(loan22.getId(),responses3.get(0).getFine(),true);
        Assert.isTrue(isPayFineSuccess3);
    }
    /**
     * Method: payBill(Installment installment, Account account)
     */
    @Test
    @Rollback
    public void testPayBill() throws Exception {
        String toAddId = new String();
        for (int i = 0; i < 10000; i++) {
            String randomId = Integer.toString((int) (Math.random() * 900000 + 100000));
            if (accountRepository.findByIdNumber(randomId).isEmpty()) {
                toAddId = randomId;
                break;
            }
        }
        if ("".equals(toAddId)) return;
        List<Account> accounts = new ArrayList<>();
        Account accountToAdd = new Account();
        accountToAdd.setIdNumber(toAddId);
        accountToAdd.setType("储蓄");
        accountToAdd.setBalance(2.00);
        accountToAdd.setCustomerName("沈征宇");
        accountRepository.save(accountToAdd);
        accounts = accountRepository.findByIdNumber(toAddId);
        Account account = accounts.get(0);
        Loan loan = new Loan();
        loan.setAccountId(account.getAccountId());
        loan.setAmount(4000.00);
        loan.setInterestRate(0.05);
        loan.setStartDate(castStringToTimeStamp("2021-01-23 00:00:00"));
        Installment installment0 = new Installment(4200.00, 4200.00, castStringToTimeStamp("2021-02-23 00:00:00"));
        installmentRepository.save(installment0);

        List<Installment> installments = new LinkedList<>();

        installments.add(installment0);
        loan.setInstallments(installments);
        loanRepository.save(loan);
        payLoanAutoService.payBill(installment0,account);
    }

}