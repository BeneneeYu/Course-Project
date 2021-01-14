package com.fudan.se.database.repository;
import com.fudan.se.database.domain.SickBed;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

@Repository
public interface SickBedRepository extends CrudRepository<SickBed,Integer> {
    HashSet<SickBed> findAllBySickRoomID(Integer id);
    SickBed findBySickBedID(Integer id);
}
