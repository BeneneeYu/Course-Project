package com.softwaretest.demo.Controller.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LoanRepaymentRequest {
    private Long accountId;
    private Long loanId;
    private Integer index;
    private Double amount;

    @Autowired
    public LoanRepaymentRequest(){

    }

    public Long getAccountId() {
        return accountId;
    }

    public Integer getIndex() {
        return index;
    }

    public Double getAmount() {
        return amount;
    }

    public Long getLoanId() {
        return loanId;
    }
}
