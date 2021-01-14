package com.fudan.se.database.repository;

import com.fudan.se.database.domain.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

@Repository
public interface PatientRepository extends CrudRepository<Patient,Integer> {
    Patient findPatientByPatientID(Integer id);
    Patient findPatientBySickBedID(Integer id);
    void deleteByPatientID(Integer id);
    HashSet<Patient> findAllBySickLevelAndLiveState(Integer sickLevel,Integer liveState);
    HashSet<Patient> findAllBySickBedIDAndSickLevel(Integer sickbed,Integer sicklevel);
}
