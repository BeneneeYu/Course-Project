package com.fudan.se.database.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-24 16:35
 **/
@Entity
public class TreatArea {
    @Id
    private Integer treatAreaID;

    public Integer getTreatAreaID() {
        return treatAreaID;
    }

    public void setTreatAreaID(Integer treatAreaID) {
        this.treatAreaID = treatAreaID;
    }
}
