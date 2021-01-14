package com.fudan.se.database.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-26 17:56
 **/
@Controller
public class AddNewRoomNurseRequest {
    private Integer nurseLeaderId;
    private String name;
    private Integer age;
    private Integer gender;
    @Autowired
    public AddNewRoomNurseRequest() {
    }

    public AddNewRoomNurseRequest(Integer nurseLeaderId, String name, Integer age, Integer gender) {
        this.nurseLeaderId = nurseLeaderId;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public Integer getNurseLeaderId() {
        return nurseLeaderId;
    }

    public void setNurseLeaderId(Integer nurseLeaderId) {
        this.nurseLeaderId = nurseLeaderId;
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
