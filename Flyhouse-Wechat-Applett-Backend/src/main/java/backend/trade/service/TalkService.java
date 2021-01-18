package backend.trade.service;

import backend.trade.domain.Talk;
import backend.trade.domain.TalkAnswer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface TalkService {
    public Talk startTalk(@RequestBody Map<String, Object> talk);
    public ArrayList<Talk> viewTalk(@RequestBody Map<String, Object> talk);
    public Map<String,Object> showTalkDetail(@RequestBody Map<String, Object> talk);
    public TalkAnswer addTalkAnswer(@RequestBody Map<String, Object> talkAnswe);
    void praiseTalk(@RequestBody Map<String, Object> map);
    ArrayList<Talk> viewMyTalk(String openId);
}
