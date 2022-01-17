package com.ooad.lab2.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2022-01-08 10:13
 **/
@Getter
@Setter
public class SingleProgressOutput {


    @JSONField(name = "课程类型", ordinal = 1)
    private String type;

    @JSONField(name = "已修学分", ordinal = 2)
    private Integer credits;

    @JSONField(name = "已修课程数量", ordinal = 3)
    private Integer courses;

    @JSONField(name = "要求学分/课程数量", ordinal = 4)
    private Integer required;

    @JSONField(name = "进度情况", ordinal = 5)
    private String percentage;

    public SingleProgressOutput() {
        this.credits = 0;
        this.courses = 0;
    }
}
