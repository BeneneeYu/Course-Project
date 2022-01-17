package fudan.se.lab2.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @program: lab2
 * @description: 回复贴
 * @author: Shen Zhengyu
 * @create: 2020-05-28 15:50
 **/
@Entity
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //回复者的ID
    private Long ownerID;
    private String ownerFullName;
    //帖子的内容
    private String words;

    private Long floorNumber;
    private Long replyToFloorNumber;
    public Reply() {
    }

    public Reply(Long ownerID,String words,Long floorNumber) {
        this.ownerID = ownerID;
        this.words = words;
        this.replyToFloorNumber = (long)(-1);
        this.floorNumber = floorNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Long ownerID) {
        this.ownerID = ownerID;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public Long getReplyToFloorNumber() {
        return replyToFloorNumber;
    }

    public void setReplyToFloorNumber(Long replyToFloorNumber) {
        this.replyToFloorNumber = replyToFloorNumber;
    }

    public String getOwnerFullName() {
        return ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }

    public Long getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Long floorNumber) {
        this.floorNumber = floorNumber;
    }
}
