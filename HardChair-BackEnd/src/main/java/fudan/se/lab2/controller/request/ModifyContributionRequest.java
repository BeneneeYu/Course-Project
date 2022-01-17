package fudan.se.lab2.controller.request;

import fudan.se.lab2.controller.request.componment.WriterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-04 22:48
 **/
@Controller
public class ModifyContributionRequest {
    private String originalTitle;

    private Long conference_id;


    private String title;

    private String articleAbstract;

    private List<WriterRequest> writers;

    private Set<String> topics;

    @Autowired
    public ModifyContributionRequest() {

    }

    public ModifyContributionRequest(String originalTitle, Long conference_id, String title, String articleAbstract, List<WriterRequest> writers, Set<String> topics) {
        this.originalTitle = originalTitle;
        this.conference_id = conference_id;
        this.title = title;
        this.articleAbstract = articleAbstract;
        this.writers = writers;
        this.topics = topics;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Long getConference_id() {
        return conference_id;
    }

    public void setConference_id(Long conference_id) {
        this.conference_id = conference_id;
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

    public Set<String> getTopics() {
        return topics;
    }

    public void setTopics(Set<String> topics) {
        this.topics = topics;
    }
}
