package backend.trade.repository;

import backend.trade.domain.Talk;
import backend.trade.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface TalkRepository  extends CrudRepository<Talk, Integer> {
    /**
     * @param talkClaassification
     * @return 一个分类下的List
     */
    ArrayList<Talk> findAllByTalkClassification(String talkClaassification);
    ArrayList<Talk> findAllByUserOpenId(String userOpenId);

}
