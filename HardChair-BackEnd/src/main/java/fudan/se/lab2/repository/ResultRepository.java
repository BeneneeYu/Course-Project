package fudan.se.lab2.repository;

import fudan.se.lab2.domain.Result;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends CrudRepository<Result,Long> {
        Result findByArticleIDAndConferenceID(Long articleID,Long conferenceID);
}
