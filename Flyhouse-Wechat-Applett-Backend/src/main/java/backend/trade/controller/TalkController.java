package backend.trade.controller;

import backend.trade.domain.Talk;
import backend.trade.domain.TalkAnswer;
import backend.trade.dto.MyResponse;
import backend.trade.serviceImpl.TalkServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: 泛海杯小程序
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-06-09 00:05
 **/
@CrossOrigin()
@RestController
@RequestMapping(path = "/api")
public class TalkController {

    TalkServiceImpl talkService;

    @Autowired
    public TalkController(TalkServiceImpl talkService){
        this.talkService=talkService;
    }
    @PostMapping(path = "/release_releaseTalk")
    public MyResponse startTalk(@RequestBody Map<String, Object> talk){
        Talk  talkReturned = talkService.startTalk(talk);
        return MyResponse.success("发帖成功",talkReturned);
    }

    @PostMapping(path = "/schoolTalk_view")
    public MyResponse viewTalk(@RequestBody Map<String, Object> talk){
        ArrayList<Talk> talks = talkService.viewTalk(talk);
        return MyResponse.success("请求成功",talks);
    }

    @PostMapping(path = "/talkDetail_onShow")
    public MyResponse showGoods(@RequestBody Map<String, Object> talk) {
        Map<String,Object> map = talkService.showTalkDetail(talk);
        if (map != null) {
            return MyResponse.success("请求成功", map);
        }else{
            return MyResponse.fail("请求失败");
        }
    }

    @PostMapping(path = "/publishTalkAnswer_addAnswer")
    public MyResponse addTalkAnswer(@RequestBody Map<String, Object> talkAnswe) {
        TalkAnswer talkAnswer = talkService.addTalkAnswer(talkAnswe);
        if (null == talkAnswer) {
            return MyResponse.fail("发布失败");
        } else {
            return MyResponse.success("发布成功", talkAnswer);
        }
    }

    @PostMapping(path = "/schoolTalk_praise")
    public MyResponse schoolTalk_praise(@RequestBody Map<String, Object> map) {
        talkService.praiseTalk(map);
        return MyResponse.success("点赞成功");

    }

}
