package backend.trade.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @program: 泛海杯小程序
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-06-09 17:45
 **/
@Entity
public class TalkAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String answerPublisher;
    private String answerContent;
    private Integer talkId;

    public TalkAnswer() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnswerPublisher() {
        return answerPublisher;
    }

    public void setAnswerPublisher(String answerPublisher) {
        this.answerPublisher = answerPublisher;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public Integer getTalkId() {
        return talkId;
    }

    public void setTalkId(Integer talkId) {
        this.talkId = talkId;
    }
}
