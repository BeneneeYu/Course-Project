package backend.trade.serviceImpl;

import backend.trade.domain.*;
import backend.trade.repository.TalkAnswerRepository;
import backend.trade.repository.TalkRepository;
import backend.trade.repository.UserRepository;
import backend.trade.service.TalkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: 泛海杯小程序
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-06-09 00:07
 **/
@Service
public class TalkServiceImpl implements TalkService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TalkRepository talkRepository;
    @Autowired
    TalkAnswerRepository talkAnswerRepository;
    @Override
    public Talk startTalk(@RequestBody Map<String, Object> talkMap) {
        User user = userRepository.findTopByOpenid((String) talkMap.get("userOpenId"));
        Talk talk = new Talk();
        talk.setStarterId(user.getId());
        talk.setTalkClassification((String) talkMap.get("talkClassification"));
        talk.setTalkIntroduction((String) talkMap.get("talkIntroduction"));
        talk.setTalkName((String) talkMap.get("talkName"));
        talk.setUserOpenId((String) talkMap.get("userOpenId"));
        talk.setTalkPublisher((String) talkMap.get("userName"));
        talkRepository.save(talk);
        return talk;
    }

    @Override
    public ArrayList<Talk> viewTalk(@RequestBody Map<String, Object> talkMap) {
        User user = userRepository.findTopByOpenid((String) talkMap.get("userOpenId"));
        ArrayList<Talk> talks = new ArrayList<>();
            if(null == talkMap.get("currentSubPage") || "recommend".equals(talkMap.get("currentSubPage"))) {
                talks = ((ArrayList<Talk>) talkRepository.findAll());
                talks.sort(
                        (o1, o2) -> {
                            if (o1.getPraiseNum().equals(o2.getPraiseNum())){
                                return 0;
                            }
                            return o1.getPraiseNum() < o2.getPraiseNum() ? 1 : -1;
                        }

                );
            }else if ("hotRank".equals(talkMap.get("currentSubPage"))) {
                talks = ((ArrayList<Talk>) talkRepository.findAll());
                talks.sort(
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
                    talks = talkRepository.findAllByTalkClassification((String) talkMap.get("currentSubPage"));
                }

            return talks;
        }


    @Override
    public Map<String, Object> showTalkDetail(Map<String, Object> talk) {
        HashMap<String,Object> map = new HashMap<>();
        Talk talk1 = talkRepository.findById((Integer) talk.get("currentTalkId")).orElse(null);
        if (null != talk1) {
            talk1.setBrowseNum(talk1.getBrowseNum()+1);
            map.put("currentTalkInformation", talk1);
            map.put("talkAnswers", talkAnswerRepository.findAllByTalkId(talk1.getId()));
            talkRepository.save(talk1);
            return map;
        }else{
            return null;
        }
    }

    @Override
    public TalkAnswer addTalkAnswer(Map<String, Object> talkAnswe) {
        Talk talk = talkRepository.findById((Integer) talkAnswe.get("currentTalkId")).orElse(null);
        User user = userRepository.findTopByOpenid((String) talkAnswe.get("userOpenId"));
        if (null == talk  || null == user){
            return null;
        }else{
            TalkAnswer talkAnswer = new TalkAnswer();
            talkAnswer.setAnswerContent((String) talkAnswe.get("answerContent"));
            talkAnswer.setAnswerPublisher(user.getId().toString());
            talkAnswer.setTalkId(talk.getId());
            talkAnswer.setAnswerPublisher((String) talkAnswe.get("userName"));

            talkAnswerRepository.save(talkAnswer);
            return talkAnswer;
        }
    }

    @Override
    public void praiseTalk(Map<String, Object> map) {
        Talk talk = talkRepository.findById((Integer) map.get("currentTalkId")).orElse(null);
        if(null != talk) {
            talk.setPraiseNum(talk.getPraiseNum() + 1);
            talkRepository.save(talk);
        }
    }

    @Override
    public ArrayList<Talk> viewMyTalk(String openId) {
        return talkRepository.findAllByUserOpenId(openId);
    }
}
