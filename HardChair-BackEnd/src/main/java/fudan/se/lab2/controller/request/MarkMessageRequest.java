package fudan.se.lab2.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/*
将消息标记为已读
 */
@Controller
public class MarkMessageRequest {
    private Long id;

    @Autowired
    public MarkMessageRequest(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
