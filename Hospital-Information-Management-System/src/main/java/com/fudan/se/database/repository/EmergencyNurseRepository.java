package com.fudan.se.database.repository;
import com.fudan.se.database.domain.EmergencyNurse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyNurseRepository extends CrudRepository<EmergencyNurse,Integer> {
}
