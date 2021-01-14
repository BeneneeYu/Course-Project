package com.fudan.se.database.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-24 15:44
 **/
@Entity
public class NucleicAcidTest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer testID;

    private Date testDate;
    private Integer testResult;
    private Integer testLiveState;
    private Integer testSickLevel;
    private Integer patientID;


    public NucleicAcidTest() {
    }

    public NucleicAcidTest(Date testDate, Integer testResult, Integer testLiveState, Integer testSickLevel, Integer patientID) {
        this.testDate = testDate;
        this.testResult = testResult;
        this.testLiveState = testLiveState;
        this.testSickLevel = testSickLevel;
        this.patientID = patientID;
    }

    public Integer getTestID() {
        return testID;
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

    public Integer getPatientID() {
        return patientID;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
    }

    @Override
    public String toString() {
        return "NucleicAcidTest{" +
                "testID=" + testID +
                ", testDate=" + testDate +
                ", testResult=" + testResult +
                ", testLiveState=" + testLiveState +
                ", testSickLevel=" + testSickLevel +
                ", patientID=" + patientID +
                '}';
    }
}
