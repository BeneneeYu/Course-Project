package com.fudan.se.database.repository;
import com.fudan.se.database.domain.RoomNurse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;

@Repository
public interface RoomNurseRepository extends CrudRepository<RoomNurse,Integer> {
    HashSet<RoomNurse> findAllByTreatAreaID(Integer id);
    RoomNurse findByStaffID(Integer id);
    void deleteByStaffID(Integer id);
}
