package com.fudan.se.database.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Date;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-24 21:55
 **/
@Controller
public class NucleicAcidTestRequest {
    private Date testDate;
    private Integer testResult;
    private Integer testLiveState;
    private Integer testSickLevel;

    @Autowired
    public NucleicAcidTestRequest() {
    }

    public NucleicAcidTestRequest(Date testDate, Integer testResult, Integer testLiveState, Integer testSickLevel) {
        this.testDate = testDate;
        this.testResult = testResult;
        this.testLiveState = testLiveState;
        this.testSickLevel = testSickLevel;
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

    public Integer getTestLiveState() {
        return testLiveState;
    }

    public void setTestLiveState(Integer testLiveState) {
        this.testLiveState = testLiveState;
    }

    public Integer getTestSickLevel() {
        return testSickLevel;
    }

    public void setTestSickLevel(Integer testSickLevel) {
        this.testSickLevel = testSickLevel;
    }
}
