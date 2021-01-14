package com.fudan.se.database.repository;

import com.fudan.se.database.domain.NurseLeader;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

@Repository

public interface NurseLeaderRepository extends CrudRepository<NurseLeader,Integer> {
    HashSet<NurseLeader> findAll();
    NurseLeader findNurseLeaderByTreatAreaID(Integer id);
    NurseLeader findByStaffID(Integer id);
}
