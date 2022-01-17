package com.softwaretest.demo.Repository;

import com.softwaretest.demo.Domain.Flow;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowRepository extends CrudRepository<Flow,Long> {

    @Query("From Flow where accountId=?1 order by amount desc ")
    List<Flow> findByAccountIdOrderByAmountDesc(Long accountId);

    @Query("From Flow where accountId=?1  order by amount asc ")
    List<Flow> findByAccountIdOrderByAmountAsc(Long accountId);

    @Query("From Flow where accountId=?1 order by date desc ")
    List<Flow> findByAccountIdOrderByDateDesc(Long accountId);

    @Query("From Flow where accountId=?1 order by date asc ")
    List<Flow> findByAccountIdOrderByDateAsc(Long accountId);

    @Query("From Flow order by amount desc ")
    List<Flow> findAllOrderByAmountDesc();

    @Query("From Flow order by amount asc ")
    List<Flow> findAllOrderByAmountAsc();

    @Query("From Flow  order by date desc ")
    List<Flow> findAllOrderByDateDesc();

    @Query("From Flow  order by date asc ")
    List<Flow> findAllOrderByDateAsc();

    List<Flow> findByAccountId(Long accountId);

    @Query("From Flow")
    List<Flow> findAll();


}
