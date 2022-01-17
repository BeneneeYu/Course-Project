package com.softwaretest.demo.Repository;

import com.softwaretest.demo.Domain.WMP;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WMPRepository extends CrudRepository<WMP,Long> {
    WMP findByWmpId(Long id);
    List<WMP> findByAccountIdAndType(Long accountId,Integer type);
}
