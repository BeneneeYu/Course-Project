package com.fudan.se.database.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-24 15:36
 **/
@Entity
public class RoomNurse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer roomNurseID;
    private Integer staffID;
    private Integer treatAreaID;

    public RoomNurse() {
    }

    public RoomNurse(Integer roomNurseID, Integer id, Integer treatAreaID) {
        this.roomNurseID = roomNurseID;
        this.staffID = id;
        this.treatAreaID = treatAreaID;
    }

    public Integer getRoomNurseID() {
        return roomNurseID;
    }

    public void setRoomNurseID(Integer roomNurseID) {
        this.roomNurseID = roomNurseID;
    }


    public Integer getStaffID() {
        return staffID;
    }

    public void setStaffID(Integer staffID) {
        this.staffID = staffID;
    }

    public Integer getTreatAreaID() {
        return treatAreaID;
    }

    public void setTreatAreaID(Integer treatAreaID) {
        this.treatAreaID = treatAreaID;
    }
}
