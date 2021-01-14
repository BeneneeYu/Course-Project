package com.fudan.se.database.domain;

import javax.persistence.*;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-24 16:32
 **/
@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer patientID;

    private Integer sickBedID;

    private String name;
    private Integer gender;
    private Integer age;
    private Double temperature;
    private Integer liveState;
    private Integer sickLevel;

    public Patient() {
    }

    public Patient(Integer sickBedID, String name, Integer gender, Integer age, Double temperature, Integer liveState, Integer sickLevel) {
        this.sickBedID = sickBedID;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.temperature = temperature;
        this.liveState = liveState;
        this.sickLevel = sickLevel;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getPatientID() {
        return patientID;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
    }

    public Integer getSickBedID() {
        return sickBedID;
    }

    public void setSickBedID(Integer sickBedID) {
        this.sickBedID = sickBedID;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientID=" + patientID +
                ", sickBedID=" + sickBedID +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", temperature=" + temperature +
                ", liveState=" + liveState +
                ", sickLevel=" + sickLevel +
                '}';
    }
}
