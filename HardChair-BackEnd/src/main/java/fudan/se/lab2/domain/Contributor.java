package fudan.se.lab2.domain;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.*;

@Entity
public class Contributor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    private Long conferenceId;



    public Contributor(){}

    public Contributor(Long userId, Long conferenceId){
        this.userId=userId;
        this.conferenceId=conferenceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(Long conferenceId) {
        this.conferenceId = conferenceId;
    }


    public Long getId() {
        return id;
    }
}
