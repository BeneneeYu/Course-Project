package com.ooad.lab2.repository;

import com.ooad.lab2.domain.StudyRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository

public interface StudyRecordRepository extends CrudRepository<StudyRecord, Long> {

    StudyRecord findByCourseCodeAndStudentNumber(String courseCode, String studentNumber);
    Set<StudyRecord> findAllByStudentNumber(String studentNumber);
}
