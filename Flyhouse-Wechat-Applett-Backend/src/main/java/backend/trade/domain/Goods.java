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
 * @create: 2020-06-09 16:57
 **/
@Entity
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer goodId;

    private String goodName;

    private Double goodPrice;

    private String goodReleaseTime;

    private Long goodSellerId;

    private String goodIntroduction;

    private String goodImg = "";

    private String goodClassification;

    private Integer browseNum = 0;

    private Integer praiseNum = 0;

    private String goodSeller;

    public Goods() {
    }

    public Integer getGoodId() {
        return goodId;
    }

    public void setGoodId(Integer goodId) {
        this.goodId = goodId;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public Double getGoodPrice() {
        return goodPrice;
    }

    public void setGoodPrice(Double goodPrice) {
        this.goodPrice = goodPrice;
    }

    public String getGoodClassification() {
        return goodClassification;
    }

    public void setGoodClassification(String goodClassification) {
        this.goodClassification = goodClassification;
    }

    public String getGoodReleaseTime() {
        return goodReleaseTime;
    }

    public void setGoodReleaseTime(String goodReleaseTime) {
        this.goodReleaseTime = goodReleaseTime;
    }

    public Long getGoodSellerId() {
        return goodSellerId;
    }

    public void setGoodSellerId(Long goodSellerId) {
        this.goodSellerId = goodSellerId;
    }

    public String getGoodIntroduction() {
        return goodIntroduction;
    }

    public void setGoodIntroduction(String goodIntroduction) {
        this.goodIntroduction = goodIntroduction;
    }

    public String getGoodImg() {
        return goodImg;
    }

    public void setGoodImg(String goodImg) {
        this.goodImg = goodImg;
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

    public String getUserName() {
        return goodSeller;
    }

    public void setUserName(String userName) {
        this.goodSeller = userName;
    }
}
