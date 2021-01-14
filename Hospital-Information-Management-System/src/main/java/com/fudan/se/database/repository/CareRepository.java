package com.fudan.se.database.repository;
import com.fudan.se.database.domain.Care;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

@Repository
public interface CareRepository extends CrudRepository<Care,Integer> {
    HashSet<Care> findAllByRoomNurseID(Integer id);
    Care findByPatientID(Integer id);
    void deleteByPatientID(Integer id);
    void deleteAllByPatientID(Integer id);
    void deleteByRoomNurseIDAndPatientID(Integer roomnurse,Integer patient);
}
