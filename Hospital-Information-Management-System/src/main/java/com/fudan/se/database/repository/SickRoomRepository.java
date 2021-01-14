package com.fudan.se.database.repository;
import com.fudan.se.database.domain.SickRoom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

@Repository
public interface SickRoomRepository extends CrudRepository<SickRoom,Integer> {
    HashSet<SickRoom> findAllByTreatAreaID(Integer id);
    SickRoom findBySickRoomID(Integer id);

}
