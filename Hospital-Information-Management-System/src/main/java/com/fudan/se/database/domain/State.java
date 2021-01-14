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
 * @create: 2020-11-24 15:37
 **/
@Entity
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer stateID;

    private Date stateDate;

    private Double temperature;


    private Integer liveState;

    private Integer sickLevel;

    private String symptom;

    private Integer patientID;

    public State(){

    }
    public State(Date stateDate, Double temperature, Integer testID, Integer liveState, Integer sickLevel, String symptom, Integer patientID) {
        this.stateDate = stateDate;
        this.temperature = temperature;
        this.liveState = liveState;
        this.sickLevel = sickLevel;
        this.symptom = symptom;
        this.patientID = patientID;
    }

    public Integer getStateID() {
        return stateID;
    }

    public void setStateID(Integer stateID) {
        this.stateID = stateID;
    }

    public Date getStateDate() {
        return stateDate;
    }

    public void setStateDate(Date stateDate) {
        this.stateDate = stateDate;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }


    public Integer getLiveState() {
        return liveState;
    }

    public void setLiveState(Integer liveState) {
        this.liveState = liveState;
    }

    public Integer getSickLevel() {
        return sickLevel;
    }

    public void setSickLevel(Integer sickLevel) {
        this.sickLevel = sickLevel;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public Integer getPatientID() {
        return patientID;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
    }

    @Override
    public String toString() {
        return "State{" +
                "stateID=" + stateID +
                ", stateDate=" + stateDate +
                ", temperature=" + temperature +
                ", liveState=" + liveState +
                ", sickLevel=" + sickLevel +
                ", symptom='" + symptom + '\'' +
                ", patientID=" + patientID +
                '}';
    }
}
