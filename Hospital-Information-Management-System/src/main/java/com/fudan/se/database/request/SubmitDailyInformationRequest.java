package com.fudan.se.database.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-28 17:59
 **/
@Controller
public class SubmitDailyInformationRequest {
    private Integer patientId;
    private Double temperature;
    private String symptom;
    private Integer sickLevel;
    private Integer liveState;

    @Autowired
    public SubmitDailyInformationRequest() {
    }

    public SubmitDailyInformationRequest(Integer patientId, Double temperature, String symptom, Integer sickLevel, Integer liveState) {
        this.patientId = patientId;
        this.temperature = temperature;
        this.symptom = symptom;
        this.sickLevel = sickLevel;
        this.liveState = liveState;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
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

    @Override
    public String toString() {
        return "SubmitDailyInformationRequest{" +
                "patientId=" + patientId +
                ", temperature=" + temperature +
                ", symptom='" + symptom + '\'' +
                ", sickLevel=" + sickLevel +
                ", liveState=" + liveState +
                '}';
    }
}
