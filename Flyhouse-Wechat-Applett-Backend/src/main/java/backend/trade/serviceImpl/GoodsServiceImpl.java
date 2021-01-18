package backend.trade.serviceImpl;

import backend.trade.domain.Goods;
import backend.trade.domain.GoodsAnswer;
import backend.trade.domain.Talk;
import backend.trade.domain.User;
import backend.trade.repository.UserRepository;
import backend.trade.repository.goods.GoodsAnswerRepository;
import backend.trade.repository.goods.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: 泛海杯小程序
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-06-09 17:01
 **/
@Service
public class GoodsServiceImpl {

    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GoodsAnswerRepository goodsAnswerRepository;
    public Goods releaseGoods(@RequestBody Map<String, Object> goods){

        User user = userRepository.findTopByOpenid((String) goods.get("userOpenId"));
        if (null == user){
            return null;
        }else {
            Goods goods1 = new Goods();
            goods1.setGoodSellerId(user.getId());
            goods1.setGoodIntroduction((String) goods.get("goodIntroduction"));
            goods1.setGoodReleaseTime(new Date().toString());
            Double price = Double.parseDouble((String)goods.get("goodPrice"));
            goods1.setGoodPrice(price );
            goods1.setGoodName((String) goods.get("goodName"));
            goods1.setUserName((String) goods.get("userName"));
            goodsRepository.save(goods1);
            return goods1;
        }

    }

    public Map<String,Object> showGoodsDetail(@RequestBody Map<String, Object> goods){
        HashMap<String,Object> map = new HashMap<>();
        Goods goods1 = goodsRepository.findByGoodId((Integer) goods.get("currentGoodId"));
        goods1.setBrowseNum(goods1.getBrowseNum() +1);
        goodsRepository.save(goods1);
        map.put("currentGoodInformation",goods1);
        map.put("goodAnswer",goodsAnswerRepository.findAllByGoodId(goods1.getGoodId()));
        return map;
    }

    public GoodsAnswer addGoodsAnswer(@RequestBody Map<String, Object> answer){
        HashMap<String,Object> map = new HashMap<>();
        Goods goods1 = goodsRepository.findByGoodId((Integer)answer.get("currentGoodId"));
        User user = userRepository.findTopByOpenid((String) answer.get("userOpenId"));
        if (null == goods1 || null == user){
            return null;
        }else{
            GoodsAnswer goodsAnswer = new GoodsAnswer();
            goodsAnswer.setAnswerContent((String) answer.get("answerContent"));
            goodsAnswer.setGoodId(goods1.getGoodId());
            goodsAnswer.setAnswerPublisher((String) answer.get("userName"));
            goodsAnswerRepository.save(goodsAnswer);
            return goodsAnswer;
        }

    }


    public void praiseGoods(Map<String, Object> map) {
        Goods goods1 = goodsRepository.findByGoodId((Integer)map.get("currentGoodId"));
        if(null != goods1) {
            goods1.setPraiseNum(goods1.getPraiseNum() + 1);
            goodsRepository.save(goods1);
        }
    }

    public  ArrayList<Goods> viewGoods(@RequestBody Map<String, Object> map){
        User user = userRepository.findTopByOpenid((String) map.get("userOpenId"));
        ArrayList<Goods> goods = new ArrayList<>();

            if(null == map.get("currentSubPage") || "recommend".equals(map.get("currentSubPage"))) {
                goods = ((ArrayList<Goods>) goodsRepository.findAll());
                goods.sort(
                        (o1, o2) -> {
                            if (o1.getPraiseNum().equals(o2.getPraiseNum())){
                                return 0;
                            }
                            return o1.getPraiseNum() < o2.getPraiseNum() ? 1 : -1;
                        }

                );
            }else if ("hotRank".equals(map.get("currentSubPage"))) {
                goods = ((ArrayList<Goods>) goodsRepository.findAll());
                goods.sort(
                        (o1, o2) -> {
                            if (o1.getBrowseNum().equals(o2.getBrowseNum())){
                                return 0;
                            }
                            return o1.getBrowseNum() < o2.getBrowseNum() ? 1 : -1;
                        }

                );
            }
            else
            {
                goods = goodsRepository.findAllByGoodClassification((String) map.get("currentSubPage"));
            }


        return goods;
    }

    public ArrayList<Goods> searchGoods(@RequestBody Map<String, Object> map){
        ArrayList<Goods> goods = goodsRepository.findAllByGoodNameContains(map.get("searchValue").toString());
        return goods;
    }

    public ArrayList<Goods> viewMyGoods(String openId) {
        Long goodSellerId = userRepository.findTopByOpenid(openId).getId();
        return goodsRepository.findAllByGoodSellerId(goodSellerId);
    }
}
