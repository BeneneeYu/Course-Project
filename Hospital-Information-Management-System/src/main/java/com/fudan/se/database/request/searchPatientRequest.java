package com.fudan.se.database.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-28 17:25
 **/
@Controller
public class searchPatientRequest {
    private Integer staffId;
    private Integer patientId;
    private Integer sickLevel;
    private Integer liveState;
    @Autowired
    public searchPatientRequest() {
    }

    public searchPatientRequest(Integer staffId, Integer patientId, Integer sickLevel, Integer liveState) {
        this.staffId = staffId;
        this.patientId = patientId;
        this.sickLevel = sickLevel;
        this.liveState = liveState;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
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

    public Integer getLiveState() {
        return liveState;
    }

    public void setLiveState(Integer liveState) {
        this.liveState = liveState;
    }
}
