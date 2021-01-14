package com.fudan.se.database.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Date;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-25 22:36
 **/
@Controller
public class ImportPatientRequest {
    String name;
    Integer age;
    Integer gender;
    Date arriveDate;
    Double temperature;
    Integer sickLevel;
    Date testDate;
    Integer testResult;

    @Autowired
    public ImportPatientRequest() {
    }


    public ImportPatientRequest(String name, Integer age, Integer gender, Date arriveDate, Double temperature, Integer sickLevel, Date testDate, Integer testResult) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.arriveDate = arriveDate;
        this.temperature = temperature;
        this.sickLevel = sickLevel;
        this.testDate = testDate;
        this.testResult = testResult;
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

    public Date getArriveDate() {
        return arriveDate;
    }

    public void setArriveDate(Date arriveDate) {
        this.arriveDate = arriveDate;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getSickLevel() {
        return sickLevel;
    }

    public void setSickLevel(Integer sickLevel) {
        this.sickLevel = sickLevel;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    public Integer getTestResult() {
        return testResult;
    }

    public void setTestResult(Integer testResult) {
        this.testResult = testResult;
    }
}
