package com.fudan.se.database.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-24 15:35
 **/
@Entity
public class EmergencyNurse implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer emergencyNurseID;
    private Integer staffID;

    public EmergencyNurse() {
    }

    public EmergencyNurse(Integer emergencyNurseID, Integer staffID) {
        this.emergencyNurseID = emergencyNurseID;
        this.staffID = staffID;
    }

    public Integer getEmergencyNurseID() {
        return emergencyNurseID;
    }

    public void setEmergencyNurseID(Integer emergencyNurseID) {
        this.emergencyNurseID = emergencyNurseID;
    }

    public Integer getStaffID() {
        return staffID;
    }

    public void setStaffID(Integer staffID) {
        this.staffID = staffID;
    }
}
