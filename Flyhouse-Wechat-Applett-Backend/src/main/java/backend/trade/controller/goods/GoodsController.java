package backend.trade.controller.goods;

import backend.trade.domain.Goods;
import backend.trade.domain.GoodsAnswer;
import backend.trade.domain.Talk;
import backend.trade.dto.MyResponse;
import backend.trade.serviceImpl.GoodsServiceImpl;
import backend.trade.serviceImpl.TalkServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: 泛海杯小程序
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-06-09 16:56
 **/
@CrossOrigin
@RestController
@RequestMapping(path = "/api")
public class GoodsController {

    private final GoodsServiceImpl goodsService;

    @Autowired
    public GoodsController(GoodsServiceImpl goodsService) {
        this.goodsService = goodsService;
    }

    @PostMapping(path = "/goods/test")
    public MyResponse test() {
        return MyResponse.success("Connect successfully.");
    }
    /**
     * @Description: 展示商品
     * @Param: [goods]
     * @return: backend.trade.dto.MyResponse
     * @Author: Shen Zhengyu
     * @Date: 2020/6/9
     */
    @PostMapping(path = "/goodDetail_onShow")
    public MyResponse showGoods(@RequestBody Map<String, Object> goods) {
        Map<String,Object> map = goodsService.showGoodsDetail(goods);
        return MyResponse.success("请求成功", map);
    }

    /**
     * @Description: 发布商品
     * @Param: [goods]
     * @return: backend.trade.dto.MyResponse
     * @Author: Shen Zhengyu
     * @Date: 2020/6/9
     */
    @PostMapping(path = "/release_releaseGood")
    public MyResponse releaseGoods(@RequestBody Map<String, Object> goods) {
        Goods goods1 = goodsService.releaseGoods(goods);
        HashMap<String,Object> map = new HashMap<>();
        if (null == goods1) {
            return MyResponse.fail("发布失败");
        } else {
            map.put("currentGood",goods1);
            return MyResponse.success("发布成功", map);
        }
    }

    /**
    * @Description: 添加流言
    * @Param: [answer]
    * @return: backend.trade.dto.MyResponse
    * @Author: Shen Zhengyu
    * @Date: 2020/6/9
    */
    @PostMapping(path = "/goodDetail_addAnswer")
    public MyResponse addGoodsAnswer(@RequestBody Map<String, Object> answer) {
        GoodsAnswer goodsAnswer = goodsService.addGoodsAnswer(answer);
        HashMap<String,Object> map = new HashMap<>();
        if (null == goodsAnswer) {
            return MyResponse.fail("发布失败");
        } else {
            map.put("goodsAnswer",goodsAnswer);
            return MyResponse.success("发布成功", map);
        }
    }

    @PostMapping(path = "/secondHand_praise")
    public MyResponse secondHand_praise(@RequestBody Map<String, Object> map) {
        goodsService.praiseGoods(map);
        return MyResponse.success("点赞成功");

    }

    @PostMapping(path = "/secondHand_view")
    public MyResponse secondHand_view(@RequestBody Map<String, Object> map) {
        ArrayList<Goods> currentGoods = goodsService.viewGoods(map);
        HashMap<String,Object> returnMap = new HashMap<>();
        if (currentGoods.isEmpty()){
            return MyResponse.success("暂时没有商品");
        }
        returnMap.put("currentGoods",currentGoods);
        return MyResponse.success("成功",returnMap);

    }

    @PostMapping(path = "/secondHand_search")
    public MyResponse secondHand_search(@RequestBody Map<String, Object> map) {
        ArrayList<Goods> currentGoods = goodsService.searchGoods(map);
        HashMap<String,Object> returnMap = new HashMap<>();
        if (currentGoods.isEmpty()){
            return MyResponse.success("暂时没有商品");
        }
        returnMap.put("currentGoods",currentGoods);
        return MyResponse.success("成功",returnMap);

    }



}