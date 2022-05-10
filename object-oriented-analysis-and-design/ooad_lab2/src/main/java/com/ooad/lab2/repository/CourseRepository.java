package com.ooad.lab2.repository;

import com.ooad.lab2.domain.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {
    Course findByCourseCode(String courseCode);
    Course findCourseBySubCourseCode(String sub);
}
