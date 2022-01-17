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
 * @create: 2022-01-07 12:28
 **/
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InstructingDirection {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Integer credits;

    @ManyToMany(cascade = CascadeType.REMOVE, fetch=FetchType.EAGER)
    private Set<Course> courses;

}
