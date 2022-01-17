package com.softwaretest.demo.Controller.Response;

import java.sql.Timestamp;
import java.util.List;

public class AccountDetailsResponse {
    private Long loanId;

    private Double amount;
    private Double interestRate;

    private Timestamp startDate;

    private List<InstallmentResponse> installments;

    private Double fine;


    public AccountDetailsResponse(Long loanId, Double amount, List<InstallmentResponse> installments, Double interestRate, Timestamp startDate, Double fine){
        this.loanId = loanId;
        this.amount = amount;
        this.installments = installments;
        this.interestRate = interestRate;
        this.startDate = startDate;
        this.fine = fine;
    }

    public Long getLoanId() {
        return loanId;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public Double getFine() {
        return fine;
    }

    public List<InstallmentResponse> getInstallments() {
        return installments;
    }
}
