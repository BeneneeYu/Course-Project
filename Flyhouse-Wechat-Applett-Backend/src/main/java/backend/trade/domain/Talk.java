package backend.trade.domain;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @program: 泛海杯小程序
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-06-09 00:04
 **/
@Entity
public class Talk{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String userOpenId;

    private Long starterId;

    private String talkName;

    private String talkIntroduction;

    private String talkClassification;

    private Integer browseNum = 0;

    private Integer praiseNum = 0;
    private String talkPublisher;

    public Talk() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserOpenId() {
        return userOpenId;
    }

    public void setUserOpenId(String userOpenId) {
        this.userOpenId = userOpenId;
    }

    public Long getStarterId() {
        return starterId;
    }

    public void setStarterId(Long starterId) {
        this.starterId = starterId;
    }

    public String getTalkName() {
        return talkName;
    }

    public void setTalkName(String talkName) {
        this.talkName = talkName;
    }

    public String getTalkIntroduction() {
        return talkIntroduction;
    }

    public void setTalkIntroduction(String talkIntroduction) {
        this.talkIntroduction = talkIntroduction;
    }

    public String getTalkClassification() {
        return talkClassification;
    }

    public void setTalkClassification(String talkClassification) {
        this.talkClassification = talkClassification;
    }

    public Integer getBrowseNum() {
        return browseNum;
    }

    public void setBrowseNum(Integer browseNum) {
        this.browseNum = browseNum;
    }

    public Integer getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(Integer praiseNum) {
        this.praiseNum = praiseNum;
    }

    public String getTalkPublisher() {
        return talkPublisher;
    }

    public void setTalkPublisher(String talkPublisher) {
        this.talkPublisher = talkPublisher;
    }
}
