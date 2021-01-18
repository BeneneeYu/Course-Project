package backend.trade.repository;

import backend.trade.domain.Talk;
import backend.trade.domain.TalkAnswer;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface TalkAnswerRepository extends CrudRepository<TalkAnswer, Integer> {
    ArrayList<TalkAnswer> findAllByTalkId(Integer talkId);
}
