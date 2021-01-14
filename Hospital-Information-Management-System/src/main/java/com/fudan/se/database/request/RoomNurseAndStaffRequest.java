package com.fudan.se.database.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-28 15:37
 **/
@Controller
public class RoomNurseAndStaffRequest {
    private Integer staffId;
    private Integer roomNurseId;

    @Autowired
    public RoomNurseAndStaffRequest() {
    }

    public RoomNurseAndStaffRequest(Integer staffId, Integer roomNurseId) {
        this.staffId = staffId;
        this.roomNurseId = roomNurseId;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getRoomNurseId() {
        return roomNurseId;
    }

    public void setRoomNurseId(Integer roomNurseId) {
        this.roomNurseId = roomNurseId;
    }
}
