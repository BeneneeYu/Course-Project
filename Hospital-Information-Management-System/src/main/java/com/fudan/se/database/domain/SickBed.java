package com.fudan.se.database.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-24 16:34
 **/
@Entity
public class SickBed {
    @Id
    private Integer sickBedID;
    private Integer sickBedCount;
    private Integer sickRoomID;

    public Integer getSickBedID() {
        return sickBedID;
    }

    public void setSickBedID(Integer sickBedID) {
        this.sickBedID = sickBedID;
    }

    public Integer getSickBedCount() {
        return sickBedCount;
    }

    public void setSickBedCount(Integer sickBedCount) {
        this.sickBedCount = sickBedCount;
    }

    public Integer getSickRoomID() {
        return sickRoomID;
    }

    public void setSickRoomID(Integer sickRoomID) {
        this.sickRoomID = sickRoomID;
    }
}
