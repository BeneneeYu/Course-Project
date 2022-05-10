package com.ooad.lab2.repository;

import com.ooad.lab2.domain.InstructingModule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructingModuleRepository extends CrudRepository<InstructingModule, Long> {
}
