package com.softwaretest.demo.Controller.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LoginRequest {
    private String username;
    private String password;

    @Autowired
    public LoginRequest(){

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
