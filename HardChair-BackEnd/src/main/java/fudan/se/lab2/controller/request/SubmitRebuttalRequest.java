package fudan.se.lab2.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-06-09 23:28
 **/
@Controller
public class SubmitRebuttalRequest {
    private Long authorID;
    private Long articleID;
    private String words;

    public SubmitRebuttalRequest(Long authorID, Long articleID, String words) {
        this.authorID = authorID;
        this.articleID = articleID;
        this.words = words;
    }
    @Autowired
    public SubmitRebuttalRequest() {
    }

    public Long getAuthorID() {
        return authorID;
    }

    public void setAuthorID(Long authorID) {
        this.authorID = authorID;
    }

    public Long getArticleID() {
        return articleID;
    }

    public void setArticleID(Long articleID) {
        this.articleID = articleID;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }
}
