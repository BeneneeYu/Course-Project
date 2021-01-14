package com.fudan.se.database.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Date;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-28 18:12
 **/
@Controller
public class SubmitNewTest {
    private Integer patientId;
    private Integer sickLevel;
    private Integer testResult;
    private Date testDate;
    @Autowired
    public SubmitNewTest() {
    }

    public SubmitNewTest(Integer patientId, Integer sickLevel, Integer testResult, Date testDate) {
        this.patientId = patientId;
        this.sickLevel = sickLevel;
        this.testResult = testResult;
        this.testDate = testDate;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getSickLevel() {
        return sickLevel;
    }

    public void setSickLevel(Integer sickLevel) {
        this.sickLevel = sickLevel;
    }

    public Integer getTestResult() {
        return testResult;
    }

    public void setTestResult(Integer testResult) {
        this.testResult = testResult;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }
}
