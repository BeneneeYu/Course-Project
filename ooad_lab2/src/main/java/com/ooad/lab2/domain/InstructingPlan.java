package com.ooad.lab2.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2022-01-07 12:09
 **/
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InstructingPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Major major;



    @ManyToMany
    private Set<Course> majorElectiveCourses;

    @ManyToMany(cascade = CascadeType.REMOVE)
    private Set<Course> moduleCourses;

}
