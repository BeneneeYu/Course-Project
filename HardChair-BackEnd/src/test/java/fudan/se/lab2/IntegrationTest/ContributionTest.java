package fudan.se.lab2.IntegrationTest;

import fudan.se.lab2.controller.AuthController;
import fudan.se.lab2.controller.ContributionController;
import fudan.se.lab2.controller.MyRelatedConferenceController;
import fudan.se.lab2.controller.request.ContributionRequest;
import fudan.se.lab2.controller.request.LoginRequest;
import fudan.se.lab2.controller.request.ModifyContributionRequest;
import fudan.se.lab2.controller.request.ShowSubmissionRequest;
import fudan.se.lab2.controller.request.componment.WriterRequest;
import fudan.se.lab2.domain.Conference;
import fudan.se.lab2.domain.User;
import fudan.se.lab2.repository.ConferenceRepository;
import fudan.se.lab2.repository.UserRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.*;

@SpringBootTest
public class ContributionTest {
    @Autowired
    ContributionController contributionController;

    @Autowired
    MyRelatedConferenceController myRelatedConferenceController;

    @Autowired
    ConferenceRepository conferenceRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthController authController;
    private MockHttpServletRequest request;


    /**
     * @Description: 测试投稿
     * @Param: []
     * @return: void
     * @Author: Shen Zhengyu
     * @Date: 2020/4/12
     */
    @Test
    void contribute() {
        Conference conference=conferenceRepository.findByFullName("testMeeting1");
        if(conference==null){
            return;
        }
        User user=userRepository.findByUsername("testD");
        if(user==null){
            return;
        }
        Long conferenceID=conference.getId();
        Long userID=user.getId();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testD");
        loginRequest.setPassword("123456");
        String token=(String)authController.login(loginRequest).getBody().get("token");
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization","Bearer " + token);
        ContributionRequest contributionRequest = new ContributionRequest();
        contributionRequest.setArticleAbstract("123");
        contributionRequest.setContributorID(userID);
        contributionRequest.setConference_id(conferenceID);
        contributionRequest.setFilename("1.pdf");
        contributionRequest.setTitle("article1");
        WriterRequest writerRequest=new WriterRequest();
        writerRequest.setCountry("China");
        writerRequest.setEmail("18302010041@fudan.edu.cn");
        writerRequest.setInstitution("fudan");
        writerRequest.setWriterName("guotaian");
        List<WriterRequest> writerRequests=new LinkedList<>();
        writerRequests.add(writerRequest);
        contributionRequest.setWriters(writerRequests);
        List<String> topics=new LinkedList<>();
        topics.add("机器学习");
        contributionRequest.setTopics(topics);
        Assert.isTrue(contributionController.contribute(request,contributionRequest).getBody().get("message").equals("投稿成功"));
    }

    @Test
    void modifyContribution(){
        Conference conference=conferenceRepository.findByFullName("testMeeting1");
        if(conference==null){
            return;
        }
        Long conferenceID=conference.getId();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testD");
        loginRequest.setPassword("123456");
        String token=(String)authController.login(loginRequest).getBody().get("token");
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization","Bearer " + token);
        ModifyContributionRequest modifyContributionRequest=new ModifyContributionRequest();
        modifyContributionRequest.setOriginalTitle("article1");
        modifyContributionRequest.setTitle("article12");
        modifyContributionRequest.setArticleAbstract("abstract");
        modifyContributionRequest.setConference_id(conferenceID);
        WriterRequest writerRequest=new WriterRequest();
        writerRequest.setCountry("China");
        writerRequest.setEmail("18302010047@fudan.edu.cn");
        writerRequest.setInstitution("fudan university");
        writerRequest.setWriterName("guotaian");
        List<WriterRequest> writerRequests=new LinkedList<>();
        writerRequests.add(writerRequest);
        modifyContributionRequest.setWriters(writerRequests);
        Set<String> topics=new HashSet<>();
        topics.add("机器学习");
        modifyContributionRequest.setTopics(topics);
        Assert.isTrue(contributionController.modifyContribution(request,modifyContributionRequest).getBody().get("message").equals("修改成功"));
    }

    @Test
    void showSubmission(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("password");
        String token=(String)authController.login(loginRequest).getBody().get("token");
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization","Bearer " + token);
        ShowSubmissionRequest showSubmissionRequest=new ShowSubmissionRequest();
        showSubmissionRequest.setConference_id((long)1);
        ResponseEntity<HashMap<String,Object>> responseEntity=myRelatedConferenceController.showMySubmission(showSubmissionRequest,request);
        Assert.isTrue(responseEntity.getBody().get("message").equals("success"));
    }
}
