package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.*;
import fudan.se.lab2.controller.response.SearchResponse;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

//import javax.xml.ws.Response;

@SpringBootTest
public class PCMemberControllerTest {
    @Autowired
    PCMemberController pcMemberController;
    private MockHttpServletRequest request;


    @Autowired
    private AuthController authController;


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
