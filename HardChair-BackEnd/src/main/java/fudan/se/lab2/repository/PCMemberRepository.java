package fudan.se.lab2.repository;

import fudan.se.lab2.domain.PCMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
public interface PCMemberRepository extends CrudRepository<PCMember,Long> {
    PCMember findByUserIdAndConferenceId(Long userId,Long conferenceId);
    List<PCMember> findAllByUserId(Long userId);
    List<PCMember> findAllByConferenceId(Long conferenceId);
}
