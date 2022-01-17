package fudan.se.lab2.repository;

import fudan.se.lab2.domain.Evaluation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationRepository extends CrudRepository<Evaluation,Long> {
    Evaluation findByPCMemberID(Long PCMemberID);
}
