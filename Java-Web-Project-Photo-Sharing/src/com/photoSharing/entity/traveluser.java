package com.photoSharing.entity;

import java.util.Date;

/**
 * @program: Project
 * @description: 用户的实体类
 * @author: Shen Zhengyu
 * @create: 2020-07-13 15:16
 **/
public class traveluser {
    private int UID;
    private String Email;
    private String UserName;
    private String Pass;
    private int State;
    private Date DateJoined;
    private Date DateLastModified;

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public Date getDateJoined() {
        return DateJoined;
    }

    public void setDateJoined(Date dateJoined) {
        DateJoined = dateJoined;
    }

    public Date getDateLastModified() {
        return DateLastModified;
    }

    public void setDateLastModified(Date dateLastModified) {
        DateLastModified = dateLastModified;
    }

    @Override
    public String toString() {
        return "traveluser{" +
                "UID=" + UID +
                ", Email='" + Email + '\'' +
                ", UserName='" + UserName + '\'' +
                ", Pass='" + Pass + '\'' +
                ", State=" + State +
                ", DateJoined=" + DateJoined +
                ", DateLastModified=" + DateLastModified +
                '}';
    }

}
