package com.softwaretest.demo.Controller.Response;

import java.sql.Timestamp;

public class InstallmentResponse {
    private Double amount;
    private Double amountRemained;


    //期数  大小从0~n-1
    private Integer index;

    private Timestamp deadline;

    public InstallmentResponse(Double amount, Double amountPaid, Timestamp deadline,Integer index){
        this.amount = amount;
        this.amountRemained = amountPaid;
        this.deadline = deadline;
        this.index = index;
    }

    public Double getAmount() {
        return amount;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public Double getAmountRemained() {
        return amountRemained;
    }
}
