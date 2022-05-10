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
 * @create: 2022-01-08 10:23
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseOutput {

    @JSONField(name = "课程", ordinal = 1)
    String courseName;

    @JSONField(name = "学分", ordinal = 2)
    Integer credits;

    @JSONField(name = "备注", ordinal = 3)
    String remarks;
}
