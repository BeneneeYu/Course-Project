package com.softwaretest.demo;

import com.softwaretest.demo.Config.CustomErrorHandler;
import com.softwaretest.demo.Domain.Account;
import com.softwaretest.demo.Domain.Flow;
import com.softwaretest.demo.Domain.Installment;
import com.softwaretest.demo.Domain.Loan;
import com.softwaretest.demo.Repository.AccountRepository;
import com.softwaretest.demo.Repository.FlowRepository;
import com.softwaretest.demo.Repository.InstallmentRepository;
import com.softwaretest.demo.Repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomErrorHandler());
        return restTemplate;
    }


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private InstallmentRepository installmentRepository;

    @Autowired
    private FlowRepository flowRepository;
    /*
    @Bean
    public CommandLineRunner dataLoader(AccountRepository accountRepository, LoanRepository loanRepository,InstallmentRepository installmentRepository,FlowRepository flowRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Account accountToAdd = new Account();
                int IDLength = getRandom(5,15);
                StringBuilder id = new StringBuilder();
                for (int i = 0; i < IDLength; i++) {
                    id.append(getRandom(1,9));
                }
                accountToAdd.setIdNumber(id.toString());
                accountToAdd.setType("储蓄");
                accountToAdd.setBalance((double) getRandom(500000,1500000));
                int NameLength = getRandom(5,15);
                StringBuilder name = new StringBuilder();
                for (int i = 0; i < IDLength; i++) {
                    char tmp = (char)('a' + getRandom(i,i+10));
                    name.append(tmp);
                }
                accountToAdd.setCustomerName(name.toString());
                accountRepository.save(accountToAdd);
                List<Account> accounts = accountRepository.findByIdNumber(id.toString());
                Account account = accounts.get(0);
                Loan loan = new Loan();
                loan.setAccountId(account.getAccountId());
                double loanAmount = (double) getRandom(100000,500000);
                loan.setAmount(loanAmount);
                loan.setInterestRate(0.05);
                int day = getRandom(1,27);
                String timeStr = "2021-01-" + day + " 00:00:00";
                loan.setStartDate(castStringToTimeStamp(timeStr));
                Installment installment0 = new Installment(1.05*(loanAmount/3.0),1.05*(loanAmount/3.0),castStringToTimeStamp("2021-02-" + day + " 00:00:00"));
                installmentRepository.save(installment0);
                Installment installment1 = new Installment(1.05*(loanAmount/3.0),1.05*(loanAmount/3.0),castStringToTimeStamp("2021-03-" + day + " 00:00:00"));
                Installment installment2 = new Installment(1.05*(loanAmount/3.0),1.05*(loanAmount/3.0),castStringToTimeStamp("2021-04-" + day + " 00:00:00"));
                Installment installment3 = new Installment(1.05*(loanAmount/3.0),1.05*(loanAmount/3.0),castStringToTimeStamp("2021-05-" + day + " 00:00:00"));
                installmentRepository.save(installment1);
                installmentRepository.save(installment2);
                installmentRepository.save(installment3);

                List<Installment> installments = new LinkedList<>();

                installments.add(installment0);
                installments.add(installment1); installments.add(installment2); installments.add(installment3);
                loan.setInstallments(installments);
                loanRepository.save(loan);

                Flow flow = new Flow("贷款发放",account.getAccountId(),loanAmount,castStringToTimeStamp("2021-01-" + day + " 00:00:00"));
                flowRepository.save(flow);
                System.out.println(accountToAdd.toString());

                System.out.println(flow.toString());
            }

        };
    }

     */
    public static int getRandom(int start, int end) {
        int num = (int) (Math.random() * (end - start + 1)) + start;
        return num;
    }
    private Timestamp castStringToTimeStamp(String timeStr){
        return Timestamp.valueOf(timeStr);
    }

}
