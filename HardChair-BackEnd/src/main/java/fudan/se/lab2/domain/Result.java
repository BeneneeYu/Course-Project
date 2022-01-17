package fudan.se.lab2.domain;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: lab2
 * @description: 记录审稿的信息
 * @author: Shen Zhengyu
 * @create: 2020-05-02 15:49
 **/
@Entity
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long conferenceID;
    private Long articleID;
    @OneToMany(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Evaluation> evaluations=new HashSet<>();

    public Result(){}
    public Result(Long conferenceID, Long articleID, Set<Evaluation> evaluations) {
        this.conferenceID = conferenceID;
        this.articleID = articleID;
        this.evaluations = evaluations;
    }

    public Set<Evaluation> getEvaluations() {
        return evaluations;
    }

    public Long getConferenceID() {
        return conferenceID;
    }

    public void setConferenceID(Long conferenceID) {
        this.conferenceID = conferenceID;
    }

    public Long getArticleID() {
        return articleID;
    }

    public void setArticleID(Long articleID) {
        this.articleID = articleID;
    }
}
