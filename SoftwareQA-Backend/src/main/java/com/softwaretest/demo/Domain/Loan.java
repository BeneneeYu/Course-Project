package com.softwaretest.demo.Domain;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private Long accountId;

    private Double interestRate;

    private Double amount;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Installment> installments = new LinkedList<>();

    private Timestamp startDate;

    public Loan(){

    }

    public Loan(Long accountId, Double interestRate,Double amount,Timestamp startDate){
        this.accountId = accountId;
        this.interestRate = interestRate;
        this.amount = amount;
        this.startDate = startDate;
    }

    public Long getId() {
        return id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public List<Installment> getInstallments() {
        return installments;
    }

    public void setInstallments(List<Installment> installments) {
        this.installments = installments;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }
}
