package com.ooad.lab2.service;

import com.ooad.lab2.domain.Major;
import com.ooad.lab2.domain.Student;
import com.ooad.lab2.repository.MajorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentServiceTest {
    @Autowired
    MajorService majorService;
    @Autowired
    StudentService studentService;
    @Autowired
    MajorRepository majorRepository;
    @Test
    void create(){

    }

    @Test
    void changeMajor(){

    }

}