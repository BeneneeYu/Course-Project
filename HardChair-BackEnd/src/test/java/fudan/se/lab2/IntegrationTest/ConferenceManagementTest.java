package fudan.se.lab2.IntegrationTest;

import fudan.se.lab2.controller.ApplyConferenceController;
import fudan.se.lab2.controller.AuthController;
import fudan.se.lab2.controller.MyRelatedConferenceController;
import fudan.se.lab2.controller.PCMemberController;
import fudan.se.lab2.controller.request.*;
import fudan.se.lab2.controller.response.SearchResponse;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.*;

@SpringBootTest
public class ConferenceManagementTest {
    @Autowired
    private AuthController authController;

    @Autowired
    private ApplyConferenceController applyConferenceController;

    @Autowired
    private PCMemberController pcMemberController;

    @Autowired
    private MyRelatedConferenceController myRelatedConferenceController;

    private MockHttpServletRequest request;

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

    @Test
    void searchAndInvite(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFullName("guotaian");
        registerRequest.setAuthorities(null);
        registerRequest.setCountry("China");
        registerRequest.setEmail("18302010041@fudan.edu.cn");
        registerRequest.setInstitution("FD");
        registerRequest.setPassword("123456");
        registerRequest.setUsername("testB");
        authController.register(registerRequest);
        registerRequest.setFullName("guotaian");
        registerRequest.setAuthorities(null);
        registerRequest.setCountry("China");
        registerRequest.setEmail("18302010042@fudan.edu.cn");
        registerRequest.setInstitution("FD");
        registerRequest.setPassword("123456");
        registerRequest.setUsername("testC");
        authController.register(registerRequest);
        registerRequest.setFullName("guotaian");
        registerRequest.setAuthorities(null);
        registerRequest.setCountry("China");
        registerRequest.setEmail("18302010043@fudan.edu.cn");
        registerRequest.setInstitution("FUDAN");
        registerRequest.setPassword("123456");
        registerRequest.setUsername("testD");
        authController.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testA");
        loginRequest.setPassword("123456");
        String token=(String)authController.login(loginRequest).getBody().get("token");
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization","Bearer " + token);
        SearchUserRequest searchUserRequest=new SearchUserRequest();
        searchUserRequest.setFull_name("testMeeting1");
        searchUserRequest.setSearch_key("guo");
        ResponseEntity<HashMap<String,Object>> responseEntity=pcMemberController.search(searchUserRequest,request);
        List<SearchResponse> responseList=(List<SearchResponse>)responseEntity.getBody().get("users");
        Assert.isTrue(responseList.size()==3);
        InvitePCMemberRequest invitePCMemberRequest=new InvitePCMemberRequest();
        List<String> users=new LinkedList<>();
        users.add("testB");
        users.add("testC");
        users.add("testD");
        invitePCMemberRequest.setUsers(users);
        invitePCMemberRequest.setFullName("testMeeting1");
        String message=(String)pcMemberController.invitePCMember(invitePCMemberRequest,request).getBody().get("message");
        Assert.isTrue(message.equals("success"));
    }

    @Test
    void approve(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testB");
        loginRequest.setPassword("123456");
        String token=(String)authController.login(loginRequest).getBody().get("token");
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization","Bearer " + token);
        ApprovePCMemberInvitationRequest approvePCMemberInvitationRequest=new ApprovePCMemberInvitationRequest();
        approvePCMemberInvitationRequest.setSenderName("testB");
        approvePCMemberInvitationRequest.setReceiverName("testA");
        approvePCMemberInvitationRequest.setRelatedConferenceName("testMeeting1");
        List<String> topics=new LinkedList<>();
        topics.add("自然语言处理");
        approvePCMemberInvitationRequest.setTopics(topics);
        String message=(String)pcMemberController.approvePCMember(approvePCMemberInvitationRequest,request).getBody().get("message");
        Assert.isTrue(message.equals("success"));

        loginRequest.setUsername("testC");
        loginRequest.setPassword("123456");
        token=(String)authController.login(loginRequest).getBody().get("token");
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization","Bearer " + token);
        approvePCMemberInvitationRequest.setSenderName("testC");
        message=(String)pcMemberController.approvePCMember(approvePCMemberInvitationRequest,request).getBody().get("message");
        Assert.isTrue(message.equals("success"));

    }

    @Test
    void disapprove(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testD");
        loginRequest.setPassword("123456");
        String token=(String)authController.login(loginRequest).getBody().get("token");
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization","Bearer " + token);
        DisapprovePCMemberInvitationRequest disapprovePCMemberInvitationRequest=new DisapprovePCMemberInvitationRequest();
        disapprovePCMemberInvitationRequest.setSenderName("testD");
        disapprovePCMemberInvitationRequest.setReceiverName("testA");
        disapprovePCMemberInvitationRequest.setRelatedConferenceName("testMeeting1");
        String message=(String)pcMemberController.disapprovePCMember(disapprovePCMemberInvitationRequest,request).getBody().get("message");
        Assert.isTrue(message.equals("success"));
    }
}
