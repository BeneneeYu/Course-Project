package com.fudan.se.database.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-28 17:15
 **/
@Controller
public class ModifyStateRequest {
    private Integer patientId;
    private Integer newSickLevel;
    private Integer newLiveState;
    @Autowired
    public ModifyStateRequest() {
    }

    public ModifyStateRequest(Integer patientId, Integer newSickLevel, Integer newLiveState) {
        this.patientId = patientId;
        this.newSickLevel = newSickLevel;
        this.newLiveState = newLiveState;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getNewSickLevel() {
        return newSickLevel;
    }

    public void setNewSickLevel(Integer newSickLevel) {
        this.newSickLevel = newSickLevel;
    }

    public Integer getNewLiveState() {
        return newLiveState;
    }

    public void setNewLiveState(Integer newLiveState) {
        this.newLiveState = newLiveState;
    }
}
