package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.ConferenceIDAndUserIDRequest;
import fudan.se.lab2.controller.request.ConferenceIDRequest;
import fudan.se.lab2.controller.request.LoginRequest;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

@SpringBootTest
public class ResultControllerTest {
    @Autowired
    private ResultController resultController;

    @Autowired
    private AuthController authController;

    private MockHttpServletRequest request;

    @Test
    public void reviewReleaseResult(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("password");
        String token=(String)authController.login(loginRequest).getBody().get("token");
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization","Bearer " + token);
        ConferenceIDAndUserIDRequest conferenceIDAndUserIDRequest=new ConferenceIDAndUserIDRequest();
        conferenceIDAndUserIDRequest.setConference_id((long)1);
        conferenceIDAndUserIDRequest.setUserId((long)1);
        ConferenceIDRequest conferenceIDRequest=new ConferenceIDRequest();
        conferenceIDRequest.setConference_id((long)1);
        String message=(String)resultController.releaseReviewResult(request,conferenceIDRequest).getBody().get("message");
        Assert.isTrue(message.equals("开启失败"));
    }
}
