package com.softwaretest.demo.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

/*
@Description: 银行流水
 */
@Entity
public class Flow {

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Long id;


    //流水类型，分为贷款发放，贷款还款，理财产品流水, 罚金缴纳
    private String type;


    //相关AccountId
    private Long accountId;

    //金额
    private Double amount;

    private Timestamp date;

    public Flow(){

    }

    public Flow(String type, Long accountId,Double amount,Timestamp date){
        this.type = type;
        this.accountId = accountId;
        this.amount = amount;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Flow{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", accountId=" + accountId +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}
