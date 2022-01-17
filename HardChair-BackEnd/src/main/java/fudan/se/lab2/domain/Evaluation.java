package fudan.se.lab2.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-02 15:52
 **/
@Entity
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long PCMemberID;
    private Integer score;
    private String comment;
    private Integer confidence;
    private Integer timesCanBeModified;
    public Evaluation(){}
    public Evaluation(Long PCMemberID, Integer score, String comment, Integer confidence) {
        this.PCMemberID = PCMemberID;
        this.score = score;
        this.comment = comment;
        this.confidence = confidence;
        this.timesCanBeModified = 1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPCMemberID() {
        return PCMemberID;
    }

    public void setPCMemberID(Long PCMemberID) {
        this.PCMemberID = PCMemberID;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getConfidence() {
        return confidence;
    }

    public void setConfidence(Integer confidence) {
        this.confidence = confidence;
    }

    public Integer getTimesCanBeModified() {
        return timesCanBeModified;
    }

    public void setTimesCanBeModified(Integer timesCanBeModified) {
        this.timesCanBeModified = timesCanBeModified;
    }

    public boolean canBeModified(){
        return timesCanBeModified > 0;
    }
}
