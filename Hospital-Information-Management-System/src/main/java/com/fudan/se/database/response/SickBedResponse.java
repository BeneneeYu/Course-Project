package com.fudan.se.database.response;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-27 16:43
 **/
public class SickBedResponse {
    private Integer id;
    private Integer patientId;
    private Integer sickBedCount;
    private Integer sickRoomID;

    public SickBedResponse(Integer id, Integer patientId, Integer sickBedCount, Integer sickRoomID) {
        this.id = id;
        this.patientId = patientId;
        this.sickBedCount = sickBedCount;
        this.sickRoomID = sickRoomID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
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
