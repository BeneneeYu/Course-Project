package com.ooad.lab2.repository;

import com.ooad.lab2.domain.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {
    Student findByStudentNumber(String sn);
}
