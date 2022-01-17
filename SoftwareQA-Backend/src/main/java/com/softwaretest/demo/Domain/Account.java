package com.softwaretest.demo.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long accountId;


    private String idNumber;

    private String customerName;

    private String type;

    private Double balance;

    private Integer grade; // 123分别代表一二三级用户

    public Account(){

    }

    public Account(String idNumber,String customerName,String type, Double balance){
        this.idNumber = idNumber;
        this.customerName = customerName;
        this.type = type;
        this.balance = balance;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", idNumber='" + idNumber + '\'' +
                ", customerName='" + customerName + '\'' +
                ", type='" + type + '\'' +
                ", balance=" + balance +
                ", grade=" + grade +
                '}';
    }
}
