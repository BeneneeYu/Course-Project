package fudan.se.lab2.controller.response;
import java.util.*;

public class AllConferenceResponse {
    private Long conference_id;
    private String full_name;
    private String short_name;
    private String place;
    private String start_date;
    private String deadline_date;
    private String release_date;
    private Integer status;
    private String chair_username;
    private Integer is_open_submission;
    private Integer can_release;
    private List<String> topics;

    public Integer getCan_release() {
        return can_release;
    }

    public void setCan_release(Integer can_release) {
        this.can_release = can_release;
    }

    public AllConferenceResponse(Long conference_id, String full_name, String short_name, String place, String start_date, String deadline_date, String release_date, Integer status, String chair_username, Integer is_open_submission, List<String> topics){
        this.conference_id = conference_id;
        this.full_name = full_name;
        this.short_name = short_name;
        this.place = place;
        this.start_date = start_date;
        this.deadline_date = deadline_date;
        this.release_date = release_date;
        this.status = status;
        this.chair_username = chair_username;
        this.is_open_submission = is_open_submission;
        this.topics=topics;
    }

    public Long getConference_id() {
        return conference_id;
    }

    public void setConference_id(Long conference_id) {
        this.conference_id = conference_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getDeadline_date() {
        return deadline_date;
    }

    public void setDeadline_date(String deadline_date) {
        this.deadline_date = deadline_date;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIs_open_submission() {
        return is_open_submission;
    }

    public void setIs_open_submission(Integer is_open_submission) {
        this.is_open_submission = is_open_submission;
    }

    public String getChair_username() {
        return chair_username;
    }

    public void setChair_username(String chair_name) {
        this.chair_username = chair_name;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
}
