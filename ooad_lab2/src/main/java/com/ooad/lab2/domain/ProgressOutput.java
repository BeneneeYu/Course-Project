package com.ooad.lab2.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2022-01-08 10:06
 **/
@Getter
@Setter
public class ProgressOutput {
    @JSONField(name = "学号", ordinal = 1)
    private String studentNumber;

    @JSONField(name = "学生名", ordinal = 2)
    private String name;

    @JSONField(name = "专业", ordinal = 3)
    private String major;

    @JSONField(name = "方向", ordinal = 4)
    private String direction;

    @JSONField(name = "进度汇总", ordinal = 5)
    private ArrayList<SingleProgressOutput> progressOutputs;

    @JSONField(name = "进度详情", ordinal = 6)
    private HashMap<String, List<Object>> progressConclusion;

    public ProgressOutput(){
        progressOutputs = new ArrayList<>();
        progressConclusion = new HashMap<>();
    }
}
