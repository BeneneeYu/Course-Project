package backend.trade.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @program: 泛海杯小程序
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-06-09 17:18
 **/
@Entity
public class GoodsAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;

    private String answerPublisher;

    private String answerContent;

    private Integer goodId;


    public GoodsAnswer() {
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
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

    public Integer getGoodId() {
        return goodId;
    }

    public void setGoodId(Integer goodId) {
        this.goodId = goodId;
    }
}
