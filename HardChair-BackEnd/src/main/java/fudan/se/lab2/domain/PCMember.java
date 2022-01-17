package fudan.se.lab2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

@Entity
public class PCMember {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private Long  conferenceId;

    @ManyToMany(cascade ={CascadeType.MERGE},fetch=FetchType.EAGER)
    private Set<Topic> topics=new HashSet<>();

    @JsonIgnore
    @ManyToMany(cascade ={CascadeType.MERGE},fetch=FetchType.EAGER)
    private Set<Article> articles=new HashSet<>();

    @JsonIgnore
    @ManyToMany(cascade ={CascadeType.PERSIST, CascadeType.MERGE},fetch=FetchType.EAGER)
    private Set<Article> articlesHaveReviewed;

    public PCMember() {
    }

    public PCMember(Long userId, Long conferenceId){
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
        return this.conferenceId;
    }

    public void setMeetingId(Long conferenceId) {
        this.conferenceId = conferenceId;
    }

    public Set<Topic> getTopics() {
        return topics;
    }

    public void setTopics(Set<Topic> topics) {
        this.topics = topics;
    }

    public Long getId() {
        return id;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public Set<Article> getArticlesHaveReviewed() {
        return articlesHaveReviewed;
    }

    public void setArticlesHaveReviewed(Set<Article> articlesHaveReviewed) {
        this.articlesHaveReviewed = articlesHaveReviewed;
    }
}
