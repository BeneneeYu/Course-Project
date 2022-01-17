package com.softwaretest.demo.Controller.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LoanDetailRequest {

    private Long accountId;

    @Autowired
    public  LoanDetailRequest(){

    }

    public Long getAccountId() {
        return accountId;
    }
}
