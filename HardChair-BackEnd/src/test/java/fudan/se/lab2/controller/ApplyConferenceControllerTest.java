package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.ApplyMeetingRequest;
import fudan.se.lab2.controller.request.LoginRequest;
import fudan.se.lab2.controller.request.ReviewConferenceRequest;
import fudan.se.lab2.repository.TopicRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.*;

import java.util.Date;
import java.util.HashMap;

@SpringBootTest
class ApplyConferenceControllerTest {

    private MockHttpServletRequest request;

    @Autowired
    MyRelatedConferenceController myRelatedConferenceController;

    @Autowired
     ApplyConferenceController applyConferenceController;


    @Autowired
     AuthController authController;


    /**
    * @Description: 测试获取全部会议是否成功
    * @Param: []
    * @return: void
    * @Author: Shen Zhengyu
    * @Date: 2020/4/12
    */
    @Test
    void showAllConference() {
        String token;
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("password");
        token=(String)authController.login(loginRequest).getBody().get("token");
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization","Bearer " +token);
        ResponseEntity<HashMap<String, Object>> responseEntity = myRelatedConferenceController.showAllConference(request);
        Assert.isTrue(responseEntity.getBody().get("message").equals("获取所有会议申请成功"));
    }


    /**
    * @Description: 测试申请会议
    * @Param: []
    * @return: void
    * @Author: Shen Zhengyu
    * @Date: 2020/4/12
    */
    @Test
    void applyMeeting() {

        String token;
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testA");
        loginRequest.setPassword("123456");
        token=(String)authController.login(loginRequest).getBody().get("token");
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization","Bearer "+token);
        ApplyMeetingRequest applyMeetingRequest = new ApplyMeetingRequest();
        applyMeetingRequest.setFullName("testMeeting1");
        applyMeetingRequest.setHoldingTime("2020-6-13 23:00");
        applyMeetingRequest.setSubmissionDeadline("2020-6-13 23:00");
        applyMeetingRequest.setAbbreviation("tA");
        applyMeetingRequest.setHoldingPlace("Shanghai");
        Set<String> topics=new HashSet<>();
        topics.add("自然语言处理");
        topics.add("机器学习");
        applyMeetingRequest.setTopics(topics);
        ResponseEntity<HashMap<String, Object>> responseEntity1 = applyConferenceController.applyMeeting(request,applyMeetingRequest);
        Assert.isTrue(responseEntity1.getBody().get("message").equals("success"));
        ResponseEntity<HashMap<String, Object>> responseEntity2 = applyConferenceController.applyMeeting(request,applyMeetingRequest);
        Assert.isTrue(responseEntity2.getBody().get("message").equals("会议申请失败，已有该会议"));
    }

    /**
    * @Description: 测试拉取待审核
    * @Param: []
    * @return: void
    * @Author: Shen Zhengyu
    * @Date: 2020/4/12
    */
    @Test
    void reviewConference() {
        String token;
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("password");
        token=(String)authController.login(loginRequest).getBody().get("token");
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization","Bear "+token);
        ResponseEntity<HashMap<String, Object>> responseEntity = applyConferenceController.reviewConference(request);
        Assert.isTrue( responseEntity.getBody().get("message").equals("拉取待审核会议成功") );

    }

    /**
    * @Description: 同意会议
    * @Param: []
    * @return: void
    * @Author: Shen Zhengyu
    * @Date: 2020/4/12
    */
    @Test
    void approveConference() {
        String token;
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("password");
        token=(String)authController.login(loginRequest).getBody().get("token");
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization","Bear "+token);
        ReviewConferenceRequest approveConferenceRequest = new ReviewConferenceRequest();;
        approveConferenceRequest.setFullName("testMeeting1");
        ResponseEntity<HashMap<String, Object>> responseEntity1 = applyConferenceController.approveConference(approveConferenceRequest,request);
        Assert.isTrue(responseEntity1.getBody().get("message").equals("批准会议成功"));
    }

    /**
    * @Description: 拒绝会议
    * @Param: []
    * @return: void
    * @Author: Shen Zhengyu
    * @Date: 2020/4/12
    */
    @Test
    void disapproveConference() {
        String token;
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testA");
        loginRequest.setPassword("123456");
        token=(String)authController.login(loginRequest).getBody().get("token");
        Date date = new Date();
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization","Bearer "+ token);
        ApplyMeetingRequest applyMeetingRequest = new ApplyMeetingRequest();
        applyMeetingRequest.setFullName("rejectedConference");
        applyMeetingRequest.setHoldingTime((date).toString());
        applyMeetingRequest.setSubmissionDeadline((date).toString());
        applyMeetingRequest.setAbbreviation((date).toString());
        applyMeetingRequest.setHoldingPlace("Shanghai");
        Set<String> topics=new HashSet<>();
        topics.add("自然语言处理");
        applyMeetingRequest.setTopics(topics);
        applyConferenceController.applyMeeting(request,applyMeetingRequest);
        ReviewConferenceRequest disapproveConferenceRequest = new ReviewConferenceRequest();
        disapproveConferenceRequest.setFullName("rejectedConference");
        ResponseEntity<HashMap<String, Object>> responseEntity = applyConferenceController.disapproveConference(disapproveConferenceRequest,request);
        Assert.isTrue(responseEntity.getBody().get("message").equals("驳回会议申请成功"));
        disapproveConferenceRequest.setFullName((new Date()).toString() + "231231231");
        ResponseEntity<HashMap<String, Object>> responseEntity1 = applyConferenceController.disapproveConference(disapproveConferenceRequest,request);
        Assert.isTrue(responseEntity1.getBody().get("message").equals("驳回会议申请失败"));
    }
}