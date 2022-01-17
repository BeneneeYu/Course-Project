package fudan.se.lab2.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-06-11 10:29
 **/
@Controller
public class ReplyPostRequest {
    private Long ownerID;
    private String words;
    private Long floorNumber;

    @Autowired
    public ReplyPostRequest() {};

    public ReplyPostRequest(Long ownerID, String words, Long floorNumber) {
        this.ownerID = ownerID;
        this.words = words;
        this.floorNumber = floorNumber;
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

    public Long getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Long floorNumber) {
        this.floorNumber = floorNumber;
    }
}
