package com.softwaretest.demo.Repository;

import com.softwaretest.demo.Domain.Installment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstallmentRepository extends CrudRepository<Installment,Long> {

}
