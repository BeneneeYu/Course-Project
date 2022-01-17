package fudan.se.lab2.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-09 21:56
 **/
@Controller
public class ConferenceIDAndUserIDRequest {
    private Long conference_id;
    private Long userId;
    @Autowired
    public  ConferenceIDAndUserIDRequest(){

    }

    public ConferenceIDAndUserIDRequest(Long conference_id, Long userId) {
        this.conference_id = conference_id;
        this.userId = userId;
    }

    public Long getConference_id() {
        return conference_id;
    }

    public void setConference_id(Long conference_id) {
        this.conference_id = conference_id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
