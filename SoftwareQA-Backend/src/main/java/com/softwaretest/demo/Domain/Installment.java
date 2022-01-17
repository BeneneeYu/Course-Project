package com.softwaretest.demo.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

/*
@Description : 分期实体类
 */
@Entity
public class Installment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double amount;

    //剩余未还款项
    private Double amountRemained;

    //本期最后还款日期
    private Timestamp deadline;

    //已经缴纳的罚金，避免出现多次缴纳罚金的情况
    private Boolean  fineHasPaid;

    public Installment(){

    }

    public Installment(Double amount, Double amountRemained, Timestamp deadline){
        this.amount = amount;
        this.amountRemained = amountRemained;
        this.deadline = deadline;
        this.fineHasPaid = false;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmountRemained() {
        return amountRemained;
    }

    public void setAmountRemained(Double amountPaid) {
        this.amountRemained = amountPaid;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public Boolean getFineHasPaid() {
        return fineHasPaid;
    }

    public void setFineHasPaid(Boolean fineHasPaid) {
        this.fineHasPaid = fineHasPaid;
    }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
