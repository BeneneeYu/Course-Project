package com.softwaretest.demo.Controller.Response;

public class FlowResponse {
    //流水类型
    private String type;

    //关联账号Id
    private Long accountId;


    private Double amount;

    private String date;

    public FlowResponse(String type,Long accountId,Double amount,String date){
        this.type = type;
        this.accountId = accountId;
        this.amount = amount;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public Double getAmount() {
        return amount;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getDate() {
        return date;
    }
}
