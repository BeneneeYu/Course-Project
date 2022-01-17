package fudan.se.lab2.repository;
import fudan.se.lab2.domain.Contributor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface AuthorRepository extends CrudRepository<Contributor,Long> {
    List<Contributor> findAllByUserId(Long userId);
}
