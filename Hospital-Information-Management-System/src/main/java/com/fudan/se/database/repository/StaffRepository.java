package com.fudan.se.database.repository;

import com.fudan.se.database.domain.Staff;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends CrudRepository<Staff,Integer> {
    Staff findStaffById(Integer id);
    Staff findStaffByName(String name);
    List<Staff> findAll();
    List<Staff> findAllByJob(Integer job);
    void deleteById(Integer id);
}
