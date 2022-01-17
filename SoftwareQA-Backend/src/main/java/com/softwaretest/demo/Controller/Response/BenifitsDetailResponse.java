package com.softwaretest.demo.Controller.Response;

import java.sql.Timestamp;

public class BenifitsDetailResponse {
    private double amount;
    private Timestamp date;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public BenifitsDetailResponse(double amount, Timestamp date) {
        this.amount = amount;
        this.date = date;
    }
}
