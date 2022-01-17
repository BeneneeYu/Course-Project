package com.ooad.lab2.repository;

import com.ooad.lab2.domain.InstructingDirection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructingDirectionRepository extends CrudRepository<InstructingDirection, Long> {
}
