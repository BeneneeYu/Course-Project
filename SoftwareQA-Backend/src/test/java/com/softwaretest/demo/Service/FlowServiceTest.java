package com.softwaretest.demo.Service;

import com.softwaretest.demo.Controller.Response.FlowResponse;
import com.softwaretest.demo.DemoApplication;
import com.softwaretest.demo.Domain.Account;
import com.softwaretest.demo.Domain.Flow;
import com.softwaretest.demo.Domain.Installment;
import com.softwaretest.demo.Domain.Loan;
import com.softwaretest.demo.Repository.AccountRepository;
import com.softwaretest.demo.Repository.FlowRepository;
import com.softwaretest.demo.Repository.InstallmentRepository;
import com.softwaretest.demo.Repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest(classes = DemoApplication.class)
public class FlowServiceTest {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private FlowRepository flowRepository;

    @Autowired
    private InstallmentRepository installmentRepository;

    @Autowired
    private LoanService loanService;

    @Autowired
    private FlowService flowService;

    @BeforeEach
    void delAll(){
        accountRepository.deleteAll();
        loanRepository.deleteAll();
        installmentRepository.deleteAll();
        flowRepository.deleteAll();
    }

    Long insertAccountA(){
        Account account1 = new Account();
        account1.setIdNumber("123456");
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
        return account1.getAccountId();
    }

    Long insertAccountB(){
        Account account2 = new Account();
        account2.setIdNumber("567890");
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

        Flow flow2 = new Flow("贷款发放",account2.getAccountId(),12000.00,castStringToTimeStamp("2021-02-28 00:00:00"));
        flowRepository.save(flow2);
        return account2.getAccountId();
    }

    Long insertAccountC(){
        Account account3 = new Account();
        account3.setBalance(20000000.00);
        account3.setIdNumber("234567");
        account3.setType("储蓄");
        account3.setCustomerName("土豪");
        accountRepository.save(account3);
        return account3.getAccountId();
    }

    @Test
    @Rollback
    void testAllFlow(){
        Long accountAId = insertAccountA();
        Long accountBId = insertAccountB();
        Long accountCId = insertAccountC();
        List<FlowResponse> responses = flowService.findAll();
        Assert.isTrue(responses!=null);

        List<FlowResponse> orderedASCByAmountResponses = flowService.findAllOrderByAmount("asc");
        Double amount = orderedASCByAmountResponses.get(0).getAmount();
        for(int i = 1;i<orderedASCByAmountResponses.size();i++){
            Assert.isTrue(orderedASCByAmountResponses.get(i).getAmount()>=amount);
            amount = orderedASCByAmountResponses.get(i).getAmount();
        }
        List<FlowResponse> orderedDESCByAmountResponses = flowService.findAllOrderByAmount("desc");
        amount = orderedDESCByAmountResponses.get(0).getAmount();
        for(int i = 1;i<orderedDESCByAmountResponses.size();i++){
            Assert.isTrue(orderedDESCByAmountResponses.get(i).getAmount()<=amount);
            amount = orderedDESCByAmountResponses.get(i).getAmount();
        }
        List<FlowResponse> invalidOrderedByAmountResponses = flowService.findAllOrderByAmount("");
        List<FlowResponse> orderedASCByDateResponses = flowService.findAllOrderByDate("asc");
        String date = orderedASCByDateResponses.get(0).getDate();
        System.out.println(date);

        for(int i = 1;i<orderedASCByDateResponses.size();i++){
            System.out.println(orderedASCByAmountResponses.get(i).getDate());
            Assert.isTrue(date.compareTo(orderedASCByAmountResponses.get(i).getDate())<=0);
            date = orderedASCByAmountResponses.get(i).getDate();
        }
        List<FlowResponse> orderedDESCByDateResponses = flowService.findAllOrderByDate("desc");
        date =  orderedDESCByDateResponses.get(0).getDate();
        for(int i = 1;i<orderedDESCByDateResponses.size();i++){
            Assert.isTrue(date.compareTo(orderedDESCByAmountResponses.get(i).getDate())>=0);
            date = orderedDESCByAmountResponses.get(i).getDate();
        }
        List<FlowResponse> invalidOrderedByDateResponses = flowService.findAllOrderByDate("");

    }

    @Test
    @Rollback
    void testFlowByAccountId(){
        Long accountAId = insertAccountA();
        Long accountBId = insertAccountB();
        Long accountCId = insertAccountC();
        List<FlowResponse> responsesNoOrderA = flowService.findByAccountIdOrderByAmount(accountAId,"");
        List<FlowResponse> responsesNoOrderB = flowService.findByAccountIdOrderByDate(accountBId,"");
        for(FlowResponse flowResponse:responsesNoOrderA){
            Assert.isTrue(flowResponse.getAccountId().equals(accountAId));
        }
        for(FlowResponse flowResponse:responsesNoOrderB){
            Assert.isTrue(flowResponse.getAccountId().equals(accountBId));
        }

        List<FlowResponse> responsesOrderByDateAsc = flowService.findByAccountIdOrderByDate(accountAId,"asc");
        List<FlowResponse> responsesOrderByDateDesc = flowService.findByAccountIdOrderByDate(accountAId,"desc");
        List<FlowResponse> responsesOrderByDateInvalid = flowService.findByAccountIdOrderByDate(accountAId,"");

        List<FlowResponse> responsesOrderByAmountAsc = flowService.findByAccountIdOrderByAmount(accountAId,"asc");
        List<FlowResponse> responsesOrderByAmountDesc = flowService.findByAccountIdOrderByAmount(accountAId,"desc");
        List<FlowResponse> responsesOrderByAmountInvalid = flowService.findByAccountIdOrderByAmount(accountAId,"");

    }

    private Timestamp castStringToTimeStamp(String timeStr){
        return Timestamp.valueOf(timeStr);
    }
}
