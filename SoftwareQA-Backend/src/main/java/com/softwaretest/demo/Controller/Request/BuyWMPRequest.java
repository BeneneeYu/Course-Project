package com.softwaretest.demo.Controller.Request;

public class BuyWMPRequest {
    private long accountId;
    private String title;
    private int type;
    private double amount;
    private String startDate;
    private String endDate;
    private int number;

    public BuyWMPRequest(long accountId, String title, int type, double amount, String startDate, String endDate, int number) {
        this.accountId = accountId;
        this.title = title;
        this.type = type;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.number = number;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
