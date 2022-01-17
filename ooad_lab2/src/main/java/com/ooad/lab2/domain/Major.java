package com.ooad.lab2.domain;

import com.sun.javafx.scene.traversal.Direction;
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
 * @create: 2022-01-07 12:03
 **/
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String majorCode;

    private String majorName;

    private Integer otherElectiveCredits;

    @ManyToMany(cascade = CascadeType.REMOVE, fetch=FetchType.EAGER)
    private Set<Course> basicCompulsoryCourses;

    private Integer basicCompulsoryCredit;

    @ManyToMany(cascade = CascadeType.REMOVE, fetch=FetchType.EAGER)
    private Set<Course> majorCompulsoryCourses;

    private Integer majorCompulsoryCredit;

    @ManyToMany(cascade = CascadeType.REMOVE, fetch=FetchType.EAGER)
    private Set<Course> majorElectiveCourses;

    private Integer majorElectiveCredit;

    @OneToMany(cascade = CascadeType.REMOVE, fetch=FetchType.EAGER)
    private Set<InstructingDirection> directions = new HashSet<>();

    @OneToMany(cascade = CascadeType.REMOVE, fetch=FetchType.EAGER)
    private Set<InstructingModule> modules = new HashSet<>();


}
