package com.fudan.se.database.repository;
import com.fudan.se.database.domain.TreatArea;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

@Repository
public interface TreatAreaRepository extends CrudRepository<TreatArea,Integer> {
    HashSet<TreatArea> findAll();
    TreatArea findByTreatAreaID(Integer id);
}
