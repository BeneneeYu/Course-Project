package fudan.se.lab2.repository;

import fudan.se.lab2.domain.EvaluationModifyRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author Shenzhengyu
 */
@Repository
public interface EvaluationModifyRequestRepository extends CrudRepository<EvaluationModifyRequest,Long> {
    Set<EvaluationModifyRequest> findAllByConferenceID(Long conferenceID);
    Set<EvaluationModifyRequest> findAllByConferenceIDAndArticleID(Long conferenceID,Long articleID);

}
