package com.ooad.lab2.repository;

import com.ooad.lab2.domain.InstructingPlan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface InstructingPlanRepository extends CrudRepository<InstructingPlan, Long> {
}
