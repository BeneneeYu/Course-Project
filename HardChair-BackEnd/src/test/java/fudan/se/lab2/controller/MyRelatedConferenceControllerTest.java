package fudan.se.lab2.controller;

import fudan.se.lab2.Lab2Application;
import fudan.se.lab2.controller.request.AllPapersForConferenceRequest;
import fudan.se.lab2.controller.request.LoginRequest;
import fudan.se.lab2.controller.request.OpenManuscriptReviewRequest;
import fudan.se.lab2.controller.request.OpenSubmissionRequest;
import fudan.se.lab2.domain.Conference;
import fudan.se.lab2.repository.ConferenceRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import java.util.HashMap;

@SpringBootTest(classes= Lab2Application.class)
public class MyRelatedConferenceControllerTest {
    @Autowired
    private AuthController authController;

    @Autowired
    private MyRelatedConferenceController myRelatedConferenceController;


    @Autowired
    private ConferenceRepository conferenceRepository;

    private MockHttpServletRequest request;

    @Test
    void openSubmissionTest(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testA");
        loginRequest.setPassword("123456");
        String token=(String)authController.login(loginRequest).getBody().get("token");
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization","Bearer " + token);
        OpenSubmissionRequest openSubmissionRequest=new OpenSubmissionRequest();
        openSubmissionRequest.setFull_name("testMeeting1");
        ResponseEntity<HashMap<String,Object>> responseEntity1=myRelatedConferenceController.openSubmission(request,openSubmissionRequest);
        Assert.isTrue(responseEntity1.getBody().get("message").equals("开启成功"));
        ResponseEntity<HashMap<String,Object>> responseEntity2=myRelatedConferenceController.openSubmission(request,openSubmissionRequest);
        Assert.isTrue(responseEntity2.getBody().get("message").equals("开启失败"));
    }

   @Test
  void openManuscriptReview(){
       LoginRequest loginRequest = new LoginRequest();
       loginRequest.setUsername("testA");
       loginRequest.setPassword("123456");
       String token=(String)authController.login(loginRequest).getBody().get("token");
       request = new MockHttpServletRequest();
       request.setCharacterEncoding("UTF-8");
       request.addHeader("Authorization","Bearer " + token);
       Conference conference=conferenceRepository.findByFullName("testMeeting1");
       if(conference==null){
           return;
       }
       OpenManuscriptReviewRequest openManuscriptReviewRequest=new OpenManuscriptReviewRequest();
       openManuscriptReviewRequest.setConference_id(conference.getId());
       openManuscriptReviewRequest.setAllocationStrategy(2);
       String message=(String)myRelatedConferenceController.openManuscriptReview(request,openManuscriptReviewRequest).getBody().get("message");
       Assert.isTrue(message.equals("邀请的PCMember不符合条件导致无法分配"));
    }

    @Test
    void showAcceptedArticles(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testA");
        loginRequest.setPassword("123456");
        String token=(String)authController.login(loginRequest).getBody().get("token");
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization","Bearer " + token);
        Conference conference=conferenceRepository.findByFullName("testMeeting1");
        if(conference==null){
            return;
        }
        AllPapersForConferenceRequest allPapersForConferenceRequest=new AllPapersForConferenceRequest();
        allPapersForConferenceRequest.setConference_id(conference.getId());
        String message=(String)myRelatedConferenceController.showAcceptedArticlesForChair(request,allPapersForConferenceRequest).getBody().get("message");
        Assert.isTrue(message.equals("success"));
    }

    @Test
    void showAllArticles(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testA");
        loginRequest.setPassword("123456");
        String token=(String)authController.login(loginRequest).getBody().get("token");
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization","Bearer " + token);
        Conference conference=conferenceRepository.findByFullName("testMeeting1");
        if(conference==null){
            return;
        }
        AllPapersForConferenceRequest allPapersForConferenceRequest=new AllPapersForConferenceRequest();
        allPapersForConferenceRequest.setConference_id(conference.getId());
        String message=(String)myRelatedConferenceController.showAllArticlesForChair(request,allPapersForConferenceRequest).getBody().get("message");
        Assert.isTrue(message.equals("success"));
    }

    @Test
    void showAllConferenceForChair(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testA");
        loginRequest.setPassword("123456");
        String token=(String)authController.login(loginRequest).getBody().get("token");
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization","Bearer " + token);
        String message=(String)myRelatedConferenceController.showAllConferenceForChair(request).getBody().get("message");
        Assert.isTrue(message.equals("获取所有我主持的会议申请成功"));
    }

}
