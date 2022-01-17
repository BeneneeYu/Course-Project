package com.softwaretest.demo.Controller.Response;

public class AccountResponse {
    private Long accountId;
    private String customerName;

    private Double balance;
    private String type;

    private int grade;


    public AccountResponse(Long accountId,String customerName,Double balance,String type,int grade){
        this.accountId = accountId;
        this.customerName = customerName;
        this.balance = balance;
        this.type = type;
        this.grade = grade;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getType() {
        return type;
    }

    public Double getBalance() {
        return balance;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getGrade() {
        return grade;
    }
}
