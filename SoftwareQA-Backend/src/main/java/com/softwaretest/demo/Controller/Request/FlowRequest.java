package com.softwaretest.demo.Controller.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class FlowRequest {
    private Long accountId;
    private String order;
    private String option;

    @Autowired
    public FlowRequest(){

    }

    public Long getAccountId() {
        return accountId;
    }

    public String getOrder() {
        return order;
    }

    public String getOption() {
        return option;
    }
}
