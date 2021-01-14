package com.fudan.se.database.repository;

import com.fudan.se.database.domain.NucleicAcidTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;

@Repository
public interface NucleicAcidTestRepository extends CrudRepository<NucleicAcidTest,Integer> {
    ArrayList<NucleicAcidTest> findAll();
    ArrayList<NucleicAcidTest> findAllByPatientID(Integer id);
    void deleteAllByPatientID(Integer id);
}
