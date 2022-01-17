package fudan.se.lab2.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-09 21:53
 **/
@Controller
public class ConferenceIDRequest {
    private Long conference_id;

    @Autowired
    public ConferenceIDRequest(){

    }
    public ConferenceIDRequest(Long conference_id) {
        this.conference_id = conference_id;
    }

    public Long getConference_id() {
        return conference_id;
    }

    public void setConference_id(Long conference_id) {
        this.conference_id = conference_id;
    }
}
