package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.AllPapersForConferenceRequest;
import fudan.se.lab2.controller.request.OpenManuscriptReviewRequest;
import fudan.se.lab2.controller.request.OpenSubmissionRequest;
import fudan.se.lab2.controller.request.ShowSubmissionRequest;
import fudan.se.lab2.controller.response.AllConferenceResponse;
import fudan.se.lab2.controller.response.ConferenceForChairResponse;
import fudan.se.lab2.controller.response.ShowSubmissionResponse;
import fudan.se.lab2.domain.Article;
import fudan.se.lab2.domain.Conference;
import fudan.se.lab2.repository.UserRepository;
import fudan.se.lab2.security.jwt.JwtTokenUtil;
import fudan.se.lab2.service.MyRelatedConferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@CrossOrigin()
@RestController
public class MyRelatedConferenceController {
    @Autowired
    private MyRelatedConferenceService myRelatedConferenceService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;



    Logger logger = LoggerFactory.getLogger(MyRelatedConferenceController.class);
    public MyRelatedConferenceController(){
    }
    String messageStr = "message";
    String auth = "Authorization";
    String successMes = "开启投稿成功";
    String tokenStr = "token";
    private final String allocationSuccessMes="稿件分配成功";
    /**
     * @Description: 直接展示所有会议，只需要token即可
     * @Param: [httpServletRequest]
     * @return: org.springframework.http.ResponseEntity<java.util.HashMap<java.lang.String,java.lang.Object>>
     * @Author: Shen Zhengyu
     * @Date: 2020/4/8
     **/
    @CrossOrigin(origins = "*")
    @PostMapping("/AllConferences")
    public ResponseEntity<HashMap<String,Object>> showAllConference(HttpServletRequest httpServletRequest){
        logger.debug("Show all the conferences");
        String token = httpServletRequest.getHeader(auth).substring(7);
        HashMap<String,Object> map = new HashMap<>();
        List<Conference> conferences = myRelatedConferenceService.showAllConference();
        List<AllConferenceResponse> responseConferences =myRelatedConferenceService.getResponses(conferences);
        map.put(messageStr,"获取所有会议申请成功");
        map.put(tokenStr,token);
        map.put("meetings",responseConferences);
        return ResponseEntity.ok(map);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/ConferenceForChair")
    public ResponseEntity<HashMap<String,Object>> showAllConferenceForChair(HttpServletRequest httpServletRequest){
        logger.debug("Show all the conferences for chair");
        //首先加载所有已申请的会议
        String token = httpServletRequest.getHeader(auth).substring(7);
        HashMap<String,Object> map = new HashMap<>();
        List<ConferenceForChairResponse> conferences = myRelatedConferenceService.showAllConferenceForChair();
        //再加载所有申请过的会议
        //开始合并
        List<AllConferenceResponse> responseConferences = myRelatedConferenceService.getResponses2(conferences);
        map.put(messageStr,"获取所有我主持的会议申请成功");
        map.put(tokenStr,token);
        map.put("meetings",responseConferences);
        return ResponseEntity.ok(map);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/ConferenceForPCMember")
    public ResponseEntity<HashMap<String,Object>> showAllConferenceForPCMember(HttpServletRequest httpServletRequest) {
        logger.debug("Show all the conferences for PC Member");
        String token = httpServletRequest.getHeader(auth).substring(7);
        HashMap<String,Object> map = new HashMap<>();
        List<Conference> conferences = myRelatedConferenceService.showAllConferenceForPCMember();
        if(conferences==null){
            map.put(messageStr,"获取所有我审稿的会议申请失败");
            map.put(tokenStr,token);
            return ResponseEntity.ok(map);
        }
        else{
            List<AllConferenceResponse> responseConferences = myRelatedConferenceService.getResponses(conferences);
            map.put(messageStr,"获取所有我审稿的会议申请成功");
            map.put(tokenStr,token);
            map.put("meetings",responseConferences);
            return ResponseEntity.ok(map);
        }

    }

    @CrossOrigin(origins = "*")
    @PostMapping("/ConferenceForAuthor")
    public ResponseEntity<HashMap<String,Object>> showAllConferenceForAuthor(HttpServletRequest httpServletRequest) {
        logger.debug("Show all the conferences for Author");
        String token = httpServletRequest.getHeader(auth).substring(7);
        HashMap<String,Object> map = new HashMap<>();
        List<Conference> conferences = myRelatedConferenceService.showAllConferenceForAuthor();
        if(conferences==null){
            map.put(messageStr,"获取所有我主持的会议申请失败");
            return ResponseEntity.ok(map);
        }
        List<AllConferenceResponse> responseConferences = myRelatedConferenceService.getResponses(conferences);
        map.put(messageStr,"获取所有我投稿的会议申请成功");
        map.put(tokenStr,token);
        map.put("meetings",responseConferences);
        return ResponseEntity.ok(map);
    }
    @CrossOrigin(origins = "*")
    @PostMapping("/openSubmission")
    public ResponseEntity<HashMap<String,Object>> openSubmission(HttpServletRequest httpServletRequest, @RequestBody OpenSubmissionRequest openSubmissionRequest) {
        logger.debug("Open submission: "+ openSubmissionRequest.toString());
        String token = httpServletRequest.getHeader(auth).substring(7);
        String chairName = userRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(token)).getFullName();

        HashMap<String,Object> map = new HashMap<>();
        boolean status = myRelatedConferenceService.openSubmission(openSubmissionRequest.getFull_name());
        if(status){
            map.put(messageStr,"开启成功");
            map.put(tokenStr,token);
        }else{
            map.put(messageStr,"开启失败");
        }
        map.put("chairName",chairName);
        return ResponseEntity.ok(map);
    }

    @CrossOrigin("*")
    @PostMapping("/showMySubmission")
    public ResponseEntity<HashMap<String,Object>> showMySubmission(@RequestBody ShowSubmissionRequest request,HttpServletRequest httpServletRequest){
        logger.debug("showMySubmission: "+request.toString());
        String token = httpServletRequest.getHeader(auth).substring(7);
        HashMap<String,Object> map=new HashMap<>();
        List<ShowSubmissionResponse> responses=myRelatedConferenceService.showSubmission(request);
        map.put(messageStr,"success");
        map.put(tokenStr,token);
        map.put("submissions",responses);
        return ResponseEntity.ok(map);
    }

    @CrossOrigin("*")
    @PostMapping("/openManuscriptReview")
    public ResponseEntity<HashMap<String,Object>> openManuscriptReview(HttpServletRequest httpServletRequest,@RequestBody OpenManuscriptReviewRequest request){
        logger.debug("openManuscriptReview"+request.toString());
        String token = httpServletRequest.getHeader(auth).substring(7);
        HashMap<String,Object> map=new HashMap<>();
        String message=myRelatedConferenceService.openManuscriptReview(request);
        if(allocationSuccessMes.equals(message)){
            map.put(tokenStr,token);
        }
        map.put(messageStr,message);
        return ResponseEntity.ok(map);

    }
    @CrossOrigin("*")
    @PostMapping("/showAllArticlesForChair")
    public ResponseEntity<HashMap<String,Object>> showAllArticlesForChair(HttpServletRequest httpServletRequest, @RequestBody AllPapersForConferenceRequest request){
        logger.debug("showAllArticlesForChair"+request.toString());
        String token=httpServletRequest.getHeader(auth).substring(7);
        HashMap<String,Object> map=new HashMap<>();
        if(!myRelatedConferenceService.isChair(request.getConference_id())){
            map.put(messageStr,"你没有权限访问该页面");
            return ResponseEntity.ok(map);
        }
        List<Article> allArticles=myRelatedConferenceService.getAllArticles(request.getConference_id());
        if(allArticles==null){
            map.put(messageStr,"请求错误");
            return ResponseEntity.ok(map);
        }
        map.put(messageStr,"success");
        map.put(tokenStr,token);
        map.put("allArticles",allArticles);
        return ResponseEntity.ok(map);
    }

    @CrossOrigin("*")
    @PostMapping("/showAcceptedArticlesForChair")
    public ResponseEntity<HashMap<String,Object>> showAcceptedArticlesForChair(HttpServletRequest httpServletRequest, @RequestBody AllPapersForConferenceRequest request){
        logger.debug("showAllArticlesForChair"+request.toString());
        String token=httpServletRequest.getHeader(auth).substring(7);
        HashMap<String,Object> map=new HashMap<>();
        List<Article> acceptedArticles=myRelatedConferenceService.getAllArticlesAccepted(request.getConference_id());
        if(acceptedArticles==null){
            map.put(messageStr,"请求错误");
            return ResponseEntity.ok(map);
        }
        map.put(tokenStr,token);
        map.put(messageStr,"success");
        map.put("acceptedArticles",acceptedArticles);
        return ResponseEntity.ok(map);
    }


}
