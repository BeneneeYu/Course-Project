package com.softwaretest.demo.Domain;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// 理财产品实体类
@Entity
public class WMP {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long wmpId;

    // 所属的账户id
    private Long accountId;

    private String title;



    private Integer type; // 1:定期理财产品product 2：基金fund 3：股票share

    private Double amount;// 初期买入金额

    private Integer number; //如果是股票该字段代表多少支股，如果是定期/基金则为1

    @OneToMany
    private List<Flow> benifits = new LinkedList<>();

    private String startDate;

    private String endDate;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getWmpId() {
        return wmpId;
    }

    public String getTitle() {
        return title;
    }
    public WMP(Long accountId, String title, Integer type, Double amount, Integer number, String startDate, String endDate) {
        this.accountId = accountId;
        this.title = title;
        this.type = type;
        this.amount = amount;
        this.number = number;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public WMP() {

    }

    public void setBenifits(List<Flow> benifits) {
        this.benifits = benifits;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<Flow> getBenifits() {
        return benifits;
    }

    public void setBenifits(ArrayList<Flow> benifits) {
        this.benifits = benifits;
    }
}
