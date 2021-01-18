package backend.trade.repository.goods;

import backend.trade.domain.Goods;
import backend.trade.domain.Talk;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface GoodsRepository extends CrudRepository<Goods, Integer> {

    /**
     * @param goodId
     * @return 商品
     */
    Goods findByGoodId(Integer goodId);
    ArrayList<Goods> findAllByGoodClassification(String GoodClassification);
    ArrayList<Goods> findAllByGoodNameContains(String co);
    ArrayList<Goods> findAllByGoodSellerId(Long goodSellerId);

}
