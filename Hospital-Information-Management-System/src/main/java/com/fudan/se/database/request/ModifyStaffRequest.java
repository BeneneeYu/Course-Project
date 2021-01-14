package com.fudan.se.database.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-27 17:28
 **/
@Controller
public class ModifyStaffRequest {
    private Integer staffId;
    private String name;
    private String password;
    private Integer age;
    private Integer gender;
    @Autowired
    public ModifyStaffRequest() {
    }


    public ModifyStaffRequest(Integer staffId, String name, String password, Integer age, Integer gender) {
        this.staffId = staffId;
        this.name = name;
        this.password = password;
        this.age = age;
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }
}
