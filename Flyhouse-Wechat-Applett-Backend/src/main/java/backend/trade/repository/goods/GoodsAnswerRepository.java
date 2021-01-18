package backend.trade.repository.goods;

import backend.trade.domain.Goods;
import backend.trade.domain.GoodsAnswer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
@Repository
public interface GoodsAnswerRepository extends CrudRepository<GoodsAnswer, Integer> {
    /**
     * @param goodId
     * @return 所有回复
     */
    ArrayList<GoodsAnswer> findAllByGoodId(Integer goodId);
}
