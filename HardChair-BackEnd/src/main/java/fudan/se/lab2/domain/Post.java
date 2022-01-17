package fudan.se.lab2.domain;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: lab2
 * @description: 主题帖
 * @author: Shen Zhengyu
 * @create: 2020-05-28 15:50
 **/
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //楼主的id
    private Long ownerID;
    private String ownerFullName;
    //针对哪篇文章
    private Long articleID;
    private String articleTitle;
    private Long replyNumber;
    //言论内容
    private String words;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Reply>  replyList;


    public Post() {
    }

    public Post(Long ownerID, Long articleID, String words) {
        this.ownerID = ownerID;
        this.articleID = articleID;
        this.replyList = new ArrayList<>();
        this.words = words;
        this.replyNumber = (long)0;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Long ownerID) {
        this.ownerID = ownerID;
    }

    public Long getArticleID() {
        return articleID;
    }

    public void setArticleID(Long articleID) {
        this.articleID = articleID;
    }

    public List<Reply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Reply> replyList) {
        this.replyList = replyList;
    }

    public void addReply(Reply reply){
        this.replyList.add(reply);
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public Long getReplyNumber() {
        return replyNumber;
    }

    public void setReplyNumber(Long replyNumber) {
        this.replyNumber = replyNumber;
    }



    public String getOwnerFullName() {
        return ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }


}
