package com.ooad.lab2.repository;

import com.ooad.lab2.domain.Major;
import com.ooad.lab2.domain.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorRepository extends CrudRepository<Major, Long> {
    Major findMajorById(Long id);

    Major findMajorByMajorName(String name);


}
