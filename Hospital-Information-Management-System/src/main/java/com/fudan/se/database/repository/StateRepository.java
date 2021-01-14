package com.fudan.se.database.repository;
import com.fudan.se.database.domain.State;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Repository
public interface StateRepository extends CrudRepository<State,Integer>{
    ArrayList<State> findAllByPatientID(Integer id);
    void deleteAllByPatientID(Integer id);
}
