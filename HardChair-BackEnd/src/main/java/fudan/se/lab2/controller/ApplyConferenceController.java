package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.ApplyMeetingRequest;
import fudan.se.lab2.controller.request.ReviewConferenceRequest;
import fudan.se.lab2.domain.Conference;
import fudan.se.lab2.repository.UserRepository;
import fudan.se.lab2.security.jwt.JwtTokenUtil;
import fudan.se.lab2.service.ApplyConferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@CrossOrigin()
@RestController
public class ApplyConferenceController {
    @Autowired
    private ApplyConferenceService applyConferenceService;

    Logger logger = LoggerFactory.getLogger(ApplyConferenceController.class);



    String messageStr = "message";
    String auth = "Authorization";
    String errorStr = "error";
    String succStr = "success";
    @CrossOrigin(origins = "*")
    @RequestMapping("/ApplyConference")
    public ResponseEntity<HashMap<String,Object>> applyMeeting(HttpServletRequest httpServletRequest, @RequestBody ApplyMeetingRequest request){
        String token= httpServletRequest.getHeader(auth).substring(7);
        logger.debug("ApplyMeetingForm: " + request.toString());
        HashMap<String,Object> map = new HashMap();
        Conference conference = applyConferenceService.applyMeeting(request);
        if (null == conference){
            map.put(messageStr,"会议申请失败，已有该会议");
            return ResponseEntity.ok(map);
        }else {
            map.put("token",token);
            map.put(messageStr,succStr);
            map.put("user id",conference.getChairId());
            map.put("short_name",conference.getAbbreviation());
            map.put("full_name",conference.getFullName());
            map.put("holdingTime",conference.getHoldingTime());
            map.put("holdingPlace",conference.getHoldingPlace());
            map.put("submissionDeadline",conference.getSubmissionDeadline());
            map.put("reviewReleaseDate",conference.getReviewReleaseDate());
            map.put("reviewStatus",conference.getReviewStatus());
            return ResponseEntity.ok(map);
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/ReviewConference")
    public ResponseEntity<HashMap<String,Object>> reviewConference(HttpServletRequest httpServletRequest){
        logger.debug("reviewConference");
        HashMap<String,Object> map=new HashMap<>();
        List<Conference> conferences=new LinkedList<>();
        applyConferenceService.reviewConference(conferences);
        String token= httpServletRequest.getHeader(auth).substring(7);
            map.put(messageStr,"拉取待审核会议成功");
            map.put("meetings",conferences);
            map.put("token",token);

        return ResponseEntity.ok(map);

    }
    /*
      accept conference request from frontend
   */
    @CrossOrigin(origins = "*")
    @PostMapping("/ApproveConference")
    public ResponseEntity<HashMap<String,Object>> approveConference(@RequestBody ReviewConferenceRequest request, HttpServletRequest httpServletRequest){ ;
        String token= httpServletRequest.getHeader(auth).substring(7);
        logger.debug("approve conference"+request.toString());
        HashMap<String,Object> map = new HashMap<>();
        System.out.println("request: "+request.getFullName());
        Conference conference = applyConferenceService.approveConference(request);
        if(null == conference){
            map.put(messageStr,"批准会议申请失败，会议表找不到该会议");
        }
        else{
            map.put("token",token);
            map.put(messageStr,"批准会议成功");
        }
        return ResponseEntity.ok(map);
    }

    /*
    receive reject conference request from frontend
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/DisapproveConference")
    public ResponseEntity<HashMap<String,Object>> disapproveConference(@RequestBody ReviewConferenceRequest request,HttpServletRequest httpServletRequest){
        logger.debug("disapprove conference"+request.toString());
        HashMap<String,Object> map=new HashMap<>();
        String message = applyConferenceService.disapproveConference(request);
        String token= httpServletRequest.getHeader(auth).substring(7);
        if(errorStr.equals(message)){
            map.put("token",token);
            map.put(messageStr,"驳回会议申请失败");
        }
        else{
            map.put(messageStr,"驳回会议申请成功");
            map.put("token",token);
        }
        return ResponseEntity.ok(map);
    }

}
