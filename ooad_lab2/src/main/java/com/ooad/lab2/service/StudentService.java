package com.ooad.lab2.service;

import com.ooad.lab2.domain.Major;
import com.ooad.lab2.domain.Student;
import com.ooad.lab2.domain.StudyRecord;
import com.ooad.lab2.repository.MajorRepository;
import com.ooad.lab2.repository.StudentRepository;
import com.ooad.lab2.repository.StudyRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2022-01-07 16:47
 **/
@Transactional
@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudyRecordRepository studyRecordRepository;

    @Autowired
    private MajorRepository majorRepository;

    public Student createStudent(Student student){
        if (null != student.getId()) return null;
        Set<StudyRecord> studyRecords = student.getStudyRecords();
        if (null != studyRecords){
            for (StudyRecord studyRecord : studyRecords) {
                studyRecordRepository.save(studyRecord);
            }
        }
        Student newStudent = studentRepository.save(student);
        return newStudent;
    }


    public Student changeStudentMajor(String stuNumber, String maj){
        Student student = studentRepository.findByStudentNumber(stuNumber);
        if (null == student) return null;
        Major major = majorRepository.findMajorByMajorName(maj);
        if (null == major) return null;
        if (major == student.getMajor()) return null;
        student.setMajor(major);
        studentRepository.save(student);
        return student;
    }
}
