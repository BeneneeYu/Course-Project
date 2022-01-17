package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.ConferenceIDAndUserIDRequest;
import fudan.se.lab2.controller.request.ConferenceIDRequest;
import fudan.se.lab2.service.ContributionService;
import fudan.se.lab2.service.MyRelatedConferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-06 14:05
 **/
@CrossOrigin()
@RestController
public class ResultController {

    @Autowired
    MyRelatedConferenceService myRelatedConferenceService;

    @Autowired
    ContributionService contributionService;

    String tokenStr = "token";
    String messageStr = "message";
    String auth = "Authorization";
    String openSuc = "开启成功";
    @CrossOrigin("*")
    @PostMapping("/releaseReviewResult")
    public ResponseEntity<HashMap<String, Object>> releaseReviewResult(HttpServletRequest httpServletRequest, @RequestBody ConferenceIDRequest conferenceIDRequest) {
        String token = httpServletRequest.getHeader(auth).substring(7);
        HashMap<String,Object> hashMap = new HashMap<>();
        String result = myRelatedConferenceService.releaseReviewResult(conferenceIDRequest.getConference_id());
        if(result.equals(openSuc)){
            hashMap.put(tokenStr,token);
        }
        hashMap.put(messageStr,result);
        return ResponseEntity.ok(hashMap);
    }

    @CrossOrigin("*")
    @PostMapping("/releaseFinalReviewResult")
    public ResponseEntity<HashMap<String, Object>> releaseFinalReviewResult(HttpServletRequest httpServletRequest, @RequestBody ConferenceIDRequest conferenceIDRequest) {
        String token = httpServletRequest.getHeader(auth).substring(7);
        HashMap<String,Object> hashMap = new HashMap<>();
        String result = myRelatedConferenceService.releaseFinalReviewResult(conferenceIDRequest.getConference_id());

        if(result.equals(openSuc)){
            hashMap.put(tokenStr,token);
        }
        hashMap.put(messageStr,result);
        return ResponseEntity.ok(hashMap);
    }
    @CrossOrigin("*")
    @PostMapping("/viewReviewResult")
    public ResponseEntity<HashMap<String, Object>> viewReviewResult(HttpServletRequest httpServletRequest, @RequestBody ConferenceIDAndUserIDRequest conferenceIDAndUserIDRequest) {
        String token = httpServletRequest.getHeader(auth).substring(7);
        HashMap<String,Object> hashMap = contributionService.viewReviewResult(conferenceIDAndUserIDRequest.getConference_id(),conferenceIDAndUserIDRequest.getUserId());
        hashMap.put(tokenStr,token);
        return ResponseEntity.ok(hashMap);
    }


    }
