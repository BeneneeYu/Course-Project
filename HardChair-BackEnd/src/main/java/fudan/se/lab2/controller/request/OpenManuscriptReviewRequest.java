package fudan.se.lab2.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class OpenManuscriptReviewRequest {
    private Long conference_id;
    private Integer allocationStrategy;

    @Autowired
    public OpenManuscriptReviewRequest(){

    }

    public Long getConference_id() {
        return conference_id;
    }

    public void setConference_id(Long conference_id) {
        this.conference_id = conference_id;
    }

    public Integer getAllocationStrategy() {
        return allocationStrategy;
    }

    public void setAllocationStrategy(Integer allocationStrategy) {
        this.allocationStrategy = allocationStrategy;
    }
}
