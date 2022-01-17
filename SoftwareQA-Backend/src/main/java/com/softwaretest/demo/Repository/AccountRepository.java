package com.softwaretest.demo.Repository;

import com.softwaretest.demo.Domain.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AccountRepository extends CrudRepository<Account,Long> {
    List<Account> findByIdNumber(String idNumber);
    Account findAccountByAccountId(Long accountId);
}
