package com.softwaretest.demo.Controller.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LogoutRequest {
    private String token;

    @Autowired
    public LogoutRequest(){

    }

    public String getToken() {
        return token;
    }
}
