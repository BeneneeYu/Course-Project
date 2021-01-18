package backend.trade.controller.mine;

import backend.trade.domain.Goods;
import backend.trade.domain.Talk;
import backend.trade.dto.MyResponse;
import backend.trade.serviceImpl.GoodsServiceImpl;
import backend.trade.serviceImpl.MineServiceImpl;
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
 * @create: 2020-06-15 13:27
 **/
@CrossOrigin
@RestController
@RequestMapping(path = "/api")
public class MineController {

    @Autowired
    GoodsServiceImpl goodsService;

    @Autowired
    TalkServiceImpl talkService;

    @Autowired
    MineServiceImpl mineService;

    @PostMapping(path = "/mine_issue_onShow")
    public MyResponse showMyIssue(@RequestBody Map<String, Object> map) {
        HashMap<String,Object> returnMap = new HashMap<>();
        ArrayList<Goods> goodsArrayList = goodsService.viewMyGoods((String) map.get("userOpenId"));
        ArrayList<Talk> talkArrayList = talkService.viewMyTalk((String) map.get("userOpenId"));

        if (goodsArrayList.isEmpty() && talkArrayList.isEmpty()){
            return MyResponse.success("暂时没有发布");
        }
        returnMap.put("issuedGoods",goodsArrayList);
        returnMap.put("issuedTalks",talkArrayList);

        return MyResponse.success("成功",returnMap);

    }

    @PostMapping(path = "/mine_credit_onShow")
    public MyResponse showMyCredit(@RequestBody Map<String, Object> map) {
        HashMap<String,Object> returnMap = new HashMap<>();
        Integer credit = mineService.showMyCredit((String) map.get("userOpenId"));
        returnMap.put("creditValue",credit);
        return MyResponse.success("成功",returnMap);

    }

}
