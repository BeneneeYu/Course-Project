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
public class SickRoom {
    @Id
    private Integer sickRoomID;

    private Integer sickRoomCount;
    private Integer treatAreaID;

    public Integer getSickRoomID() {
        return sickRoomID;
    }

    public void setSickRoomID(Integer sickRoomID) {
        this.sickRoomID = sickRoomID;
    }

    public Integer getTreatAreaID() {
        return treatAreaID;
    }

    public void setTreatAreaID(Integer treatAreaID) {
        this.treatAreaID = treatAreaID;
    }

    public Integer getSickRoomCount() {
        return sickRoomCount;
    }

    public void setSickRoomCount(Integer sickRoomCount) {
        this.sickRoomCount = sickRoomCount;
    }
}
