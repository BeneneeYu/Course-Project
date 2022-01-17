package com.softwaretest.demo.Controller.RemoteResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


/*
@Description: /account/check 响应的包装类
 */
@Controller
public class CheckIdentityResponse {
    private Integer code;
    private Long count;
    private Object data;
    private boolean flag;

    @Autowired
    public CheckIdentityResponse(){

    }

    public CheckIdentityResponse(Integer code, Long count,Object data,boolean flag){
        this.code = code;
        this.count = count;
        this.data = data;
        this.flag = flag;
    }

    public Integer getCode() {
        return code;
    }

    public Long getCount() {
        return count;
    }

    public Object getData() {
        return data;
    }

    public boolean isFlag() {
        return flag;
    }
}
