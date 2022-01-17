package fudan.se.lab2.controller.response;
import fudan.se.lab2.domain.Writer;

import java.util.List;
import java.util.Set;

public class ShowSubmissionResponse {
    private String conferenceName;
    private String filename;
    private String title;
    private String articleAbstract;
    private Long status;
    private Set<String> topics;
    private List<Writer> writers;
    private Long articleID;

    public Long getArticleID() {
        return articleID;
    }

    public void setArticleID(Long articleID) {
        this.articleID = articleID;
    }

    public ShowSubmissionResponse(String conferenceName, String filename, String title, String articleAbstract, Long status){
        this.conferenceName=conferenceName;
        this.filename=filename;
        this.title=title;
        this.articleAbstract=articleAbstract;
        this.status=status;
    }

    public String getConferenceName() {
        return conferenceName;
    }

    public void setConferenceName(String conferenceName) {
        this.conferenceName = conferenceName;
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

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Set<String> getTopics() {
        return topics;
    }

    public void setTopics(Set<String> topics) {
        this.topics = topics;
    }

    public List<Writer> getWriters() {
        return writers;
    }

    public void setWriters(List<Writer> writers) {
        this.writers = writers;
    }
}
