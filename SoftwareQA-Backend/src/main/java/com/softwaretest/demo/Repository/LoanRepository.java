package com.softwaretest.demo.Repository;

import com.softwaretest.demo.Domain.Loan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository  extends CrudRepository<Loan,Long> {
    List<Loan> findByAccountId(Long accountId);

}
