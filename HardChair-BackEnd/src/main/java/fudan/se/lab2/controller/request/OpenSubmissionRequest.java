package fudan.se.lab2.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @program: lab2
 * @description: Open submission
 * @author: Shen Zhengyu
 * @create: 2020-04-11 14:36
 **/
@Controller
public class OpenSubmissionRequest {
    String full_name;

    @Autowired
    public OpenSubmissionRequest() {
    }

    public OpenSubmissionRequest(String full_name) {
        this.full_name = full_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
}
