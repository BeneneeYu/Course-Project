package com.softwaretest.demo.Controller.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class FinePaymentRequest {
    private Long accountId;
    private Long loanId;
    private Double fine;

    @Autowired
    public FinePaymentRequest(){

    }

    public Long getLoanId() {
        return loanId;
    }

    public Double getFine() {
        return fine;
    }
}
