package com.photoSharing.entity;
import java.util.Date;

/**
 * @program: Project
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-07-16 12:03
 **/
public class chathistory {
    private int HistoryID;
    private int SenderID;
    private int ReceiverID;
    private Date Date;
    private String Content;

    public int getHistoryID() {
        return HistoryID;
    }

    public void setHistoryID(int historyID) {
        HistoryID = historyID;
    }

    public int getSenderID() {
        return SenderID;
    }

    public void setSenderID(int senderID) {
        SenderID = senderID;
    }

    public int getReceiverID() {
        return ReceiverID;
    }

    public void setReceiverID(int receiverID) {
        ReceiverID = receiverID;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public chathistory() {
    }

    @Override
    public String toString() {
        return "chathistory{" +
                "HistoryID=" + HistoryID +
                ", SenderID=" + SenderID +
                ", ReceiverID=" + ReceiverID +
                ", Date=" + Date +
                ", Content='" + Content + '\'' +
                '}';
    }
}
