package com.ooad.lab2.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2022-01-07 12:07
 **/
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String studentNumber;

    private String courseCode;
}
