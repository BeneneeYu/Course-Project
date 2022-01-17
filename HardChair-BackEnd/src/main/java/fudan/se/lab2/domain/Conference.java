package fudan.se.lab2.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Conference {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long chairId;

    private String abbreviation;

    @Column(unique = true)
    private String fullName;
    private String holdingPlace;
    private String holdingTime;
    private String submissionDeadline;
    private String reviewReleaseDate;
    private Integer isOpenSubmission;
    private Integer reviewStatus;
    @ManyToMany(cascade ={CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<Topic> topics=new HashSet<>();

    public Conference(){}

    public Conference(Long chairId, String abbreviation, String fullName, String holdingPlace, String holdingTime, String submissionDeadline, String reviewReleaseDate){
        this.chairId=chairId;
        this.abbreviation=abbreviation;
        this.fullName=fullName;
        this.holdingPlace=holdingPlace;
        this.holdingTime=holdingTime;
        this.submissionDeadline=submissionDeadline;
        this.reviewReleaseDate=reviewReleaseDate;
        this.isOpenSubmission = 1;//1-5
        //1：审核通过，但尚未开启投稿
        //2：开始投稿
        //3：截稿，开始发布submissionDeadline
        //4：评审结果已第一次发布reviewReleaseDate
        //5：评审结果已第二次发布
        //6：会议开始holdingTime
        //记录会议审核状态
        this.reviewStatus = 1;//1刚提交和2通过了3是没通过

    }

    public Long getId() {
        return id;
    }


    public Long getChairId() {
        return chairId;
    }

    public void setChairId(Long chairId) {
        this.chairId = chairId;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getHoldingPlace() {
        return holdingPlace;
    }

    public void setHoldingPlace(String holdingPlace) {
        this.holdingPlace = holdingPlace;
    }

    public String getHoldingTime() {
        return holdingTime;
    }

    public void setHoldingTime(String holdingTime) {
        this.holdingTime = holdingTime;
    }

    public String getSubmissionDeadline() {
        return submissionDeadline;
    }

    public void setSubmissionDeadline(String submissionDeadline) {
        this.submissionDeadline = submissionDeadline;
    }

    public String getReviewReleaseDate() {
        return reviewReleaseDate;
    }

    public void setReviewReleaseDate(String reviewReleaseDate) {
        this.reviewReleaseDate = reviewReleaseDate;
    }

    public Integer getIsOpenSubmission() {
        return isOpenSubmission;
    }


    public void setIsOpenSubmission(Integer isOpenSubmission) {
        this.isOpenSubmission = isOpenSubmission;
    }

    public Integer getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(Integer reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public Set<Topic> getTopics() {
        return topics;
    }

    public void setTopics(Set<Topic> topics) {
        this.topics = topics;
    }
}
