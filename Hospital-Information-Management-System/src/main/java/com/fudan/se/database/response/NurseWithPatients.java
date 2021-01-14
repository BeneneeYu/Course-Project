package com.fudan.se.database.response;

import com.fudan.se.database.domain.Patient;
import com.fudan.se.database.domain.Staff;

import java.util.HashSet;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-28 14:32
 **/
public class NurseWithPatients {
    private Integer id;

    private String name;
    private String password;
    private Integer age;
    private Integer gender;
    private Integer job;
    private HashSet<Patient> patients;

    public NurseWithPatients(Staff staff, HashSet<Patient> patients) {
        this.age = staff.getAge();
        this.password = staff.getPassword();
        this.gender = staff.getGender();
        this.id = staff.getId();
        this.job = staff.getJob();
        this.name = staff.getName();
        this.patients = patients;
    }
}
