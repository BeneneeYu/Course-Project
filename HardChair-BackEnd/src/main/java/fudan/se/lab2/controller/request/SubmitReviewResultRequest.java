package fudan.se.lab2.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-02 15:47
 **/
@Controller
public class SubmitReviewResultRequest {
    private Long conference_id;
    private Long userId;
    private Long articleId;
    private Integer score;
    private String comment;
    private Integer confidence;

    @Autowired
    public SubmitReviewResultRequest(){}
    public SubmitReviewResultRequest(Long conference_id, Long userId, Long articleId, Integer score, String comment, Integer confidence) {
        this.conference_id = conference_id;
        this.userId = userId;
        this.articleId = articleId;
        this.score = score;
        this.comment = comment;
        this.confidence = confidence;
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

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
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
}
