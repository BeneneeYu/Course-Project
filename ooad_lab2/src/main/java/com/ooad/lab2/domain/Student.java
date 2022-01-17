package com.ooad.lab2.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2022-01-07 12:01
 **/
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String studentNumber;

    @OneToOne
    private Major major;

    @OneToMany(cascade = CascadeType.REMOVE)
    private Set<StudyRecord> studyRecords  = new HashSet<>();

    @OneToOne
    private Progress progress;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", studentNumber='" + studentNumber + '\'' +
                ", major=" + major +
                ", studyRecords=" + studyRecords +
                ", progress=" + progress +
                '}';
    }
}
