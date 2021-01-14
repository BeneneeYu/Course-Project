package com.fudan.se.database.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-25 14:50
 **/
@Controller
public class LoginRequest {
    private Integer id;
    private String password;

    @Autowired
    public LoginRequest() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
