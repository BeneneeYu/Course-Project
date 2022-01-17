package com.softwaretest.demo.Controller.RemoteResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LoginSuccessResponse {
    private String expireTime;
    private String token;

    @Autowired
    public LoginSuccessResponse(){

    }

    public LoginSuccessResponse(String expireTime,String token){
        this.expireTime = expireTime;
        this.token = token;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public String getToken() {
        return token;
    }
}
