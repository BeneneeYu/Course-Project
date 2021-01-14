package com.fudan.se.database.request;

import org.springframework.stereotype.Controller;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-26 12:35
 **/
@Controller
public class TreatAreaRequest {
    private Integer treatArea;

    public Integer getTreatArea() {
        return treatArea;
    }

    public void setTreatArea(Integer treatArea) {
        this.treatArea = treatArea;
    }
}
