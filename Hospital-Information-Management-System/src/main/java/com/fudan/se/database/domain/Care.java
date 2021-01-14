package com.fudan.se.database.domain;

import javax.persistence.*;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-24 16:35
 **/
@Entity
public class Care {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer careID;
    private Integer patientID;
    private Integer roomNurseID;

    public Care() {
    }

    public Care(Integer patientID, Integer roomNurseID) {
        this.patientID = patientID;
        this.roomNurseID = roomNurseID;
    }

    public Integer getPatientID() {
        return patientID;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
    }

    public Integer getRoomNurseID() {
        return roomNurseID;
    }

    public void setRoomNurseID(Integer roomNurseID) {
        this.roomNurseID = roomNurseID;
    }
}
