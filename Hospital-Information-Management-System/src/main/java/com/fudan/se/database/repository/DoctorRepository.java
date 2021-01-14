package com.fudan.se.database.repository;

import com.fudan.se.database.domain.Doctor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.print.Doc;
import java.util.HashSet;
import java.util.Set;

@Repository
public interface DoctorRepository extends CrudRepository<Doctor,Integer> {
    HashSet<Doctor> findAll();
    Doctor findByStaffID(Integer id);
    Doctor findDoctorByTreatAreaID(Integer id);
}
