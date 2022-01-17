package fudan.se.lab2.controller.request;

import fudan.se.lab2.controller.request.componment.WriterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * @program: lab2
 * @description: 投稿请求
 * @author: Shen Zhengyu
 * @create: 2020-04-08 09:11
 **/
@Controller
public class ContributionRequest {
    private Long conference_id;

    private Long contributorID;

    private String filename;

    private String title;

    private String articleAbstract;


    private List<WriterRequest> writers;

    private List<String> topics;
    @Autowired
    public ContributionRequest(){

    }

    public ContributionRequest(Long conference_id, Long authorID, String filename, String title, String articleAbstract, List<WriterRequest> writers, List<String> topics){
        this.conference_id = conference_id;
        this.contributorID = authorID;
        this.filename = filename;
        this.title = title;
        this.articleAbstract = articleAbstract;
        this.writers = writers;
        this.topics=topics;
    }

    public Long getConference_id() {
        return conference_id;
    }

    public void setConference_id(Long conference_id) {
        this.conference_id = conference_id;
    }

    public Long getContributorID() {
        return contributorID;
    }

    public void setContributorID(Long contributorID) {
        this.contributorID = contributorID;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticleAbstract() {
        return articleAbstract;
    }

    public void setArticleAbstract(String articleAbstract) {
        this.articleAbstract = articleAbstract;
    }

    public List<WriterRequest> getWriters() {
        return writers;
    }

    public void setWriters(List<WriterRequest> writers) {
        this.writers = writers;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
}

