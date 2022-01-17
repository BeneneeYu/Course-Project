package fudan.se.lab2.controller.response;

import fudan.se.lab2.domain.Conference;


/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-06 13:44
 **/
public class ConferenceForChairResponse {
    private Conference conference;
    private Integer canRelease;

    public ConferenceForChairResponse(){}
    public ConferenceForChairResponse(Conference conference, Integer canRelease) {
        this.conference = conference;
        this.canRelease = canRelease;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public Integer getCanRelease() {
        return canRelease;
    }

    public void setCanRelease(Integer canRelease) {
        this.canRelease = canRelease;
    }
}
