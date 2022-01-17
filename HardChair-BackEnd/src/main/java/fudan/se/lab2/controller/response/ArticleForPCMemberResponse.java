package fudan.se.lab2.controller.response;

import fudan.se.lab2.domain.Writer;

import java.util.List;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-06 13:11
 **/
public class ArticleForPCMemberResponse {
    private Long articleId;
    private String title;
    private String articleAbstract;
    private List<Writer> writers;
    private Integer status;
    private Integer canPost;
    public ArticleForPCMemberResponse(){}
    public ArticleForPCMemberResponse(String title, String articleAbstract, List<Writer> writers, Integer status) {
        this.title = title;
        this.articleAbstract = articleAbstract;
        this.writers = writers;
        this.status = status;
        this.canPost = 1;
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

    public List<Writer> getWriters() {
        return writers;
    }

    public void setWriters(List<Writer> writers) {
        this.writers = writers;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Integer getCanPost() {
        return canPost;
    }

    public void setCanPost(Integer canPost) {
        this.canPost = canPost;
    }
}
