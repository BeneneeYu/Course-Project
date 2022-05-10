package com.ooad.lab2.repository;

import com.ooad.lab2.domain.Progress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgressRepository extends CrudRepository<Progress, Long> {
}
