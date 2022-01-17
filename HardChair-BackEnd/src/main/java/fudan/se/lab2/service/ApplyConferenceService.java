package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.ApplyMeetingRequest;
import fudan.se.lab2.controller.request.ReviewConferenceRequest;
import fudan.se.lab2.domain.*;
import fudan.se.lab2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ApplyConferenceService {
    private final String RESPONSE="ApplyConferenceResponse";

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired PCMemberRepository pcMemberRepository;

    @Autowired
    public  ApplyConferenceService(ConferenceRepository conferenceRepository,UserRepository userRepository,MessageRepository messageRepository){
        this.conferenceRepository=conferenceRepository;
        this.userRepository=userRepository;
        this.messageRepository=messageRepository;
    }

    public Conference applyMeeting(ApplyMeetingRequest request){
        String userName= SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userRepository.findByUsername(userName);
        Long id=user.getId();
        if(null!=conferenceRepository.findByFullName(request.getFullName())){
            System.out.println("会议全称重复");
            return null;
        }
        Conference conference = new Conference(id,request.getAbbreviation(),request.getFullName(),request.getHoldingPlace(),request.getHoldingTime(),request.getSubmissionDeadline(),request.getReviewReleaseDate());
        Set<Topic> topics = new HashSet<>();
        for (String topicName : request.getTopics()) {
            Topic topic=topicRepository.findByTopic(topicName);
            if(topic!=null){
                System.out.println("测试时topicName是: "+topicName);
            }
            if(topic==null){
                topic=new Topic(topicName);
                topicRepository.save(topic);
                topic=topicRepository.findByTopic(topicName);
            }
            topics.add(topic);
        }
        conference.setTopics(topics);
        conferenceRepository.save(conference);
        return conference;
    }

    public Conference approveConference(ReviewConferenceRequest request) {
        Conference conference = conferenceRepository.findByFullName(request.getFullName());
        if (conference == null) {
            System.out.println("会议申请表中没有此会议");
            //会议申请表中没有此会议
            return null;
        }
        conference.setReviewStatus(2);
        conferenceRepository.save(conference);
        //会议通过，置为2
        User user=userRepository.findById(conference.getChairId()).orElse(null);
        //错误：没有这个用户
        if(user==null){
            System.out.println("在生成消息时没有找到用户");
            return null;
        }


        //保存消息
        Message message=new Message("admin",user.getUsername(),conference.getFullName(),"管理员通过了你的会议请求",RESPONSE,1);
        messageRepository.save(message);

        //使chair成为PCMember
        PCMember pcMember=new PCMember(user.getId(),conference.getId());
        Set<Topic> topics = new HashSet<>(conference.getTopics());
        pcMember.setTopics(topics);
        pcMemberRepository.save(pcMember);

        return conference;
    }

    public String disapproveConference(ReviewConferenceRequest request){
        Conference conference = conferenceRepository.findByFullName(request.getFullName());
        if (conference == null) {
            //会议申请表中没有此会议
            return "error";
        } else {
            conference.setReviewStatus(3);
            conferenceRepository.save(conference);
            User user=userRepository.findById(conference.getChairId()).orElse(null);
            //错误：没有这个用户
            if(user==null){
                System.out.println("在生成消息时没有找到用户");
                return "error";
            }
            Message message=new Message("admin",user.getUsername(),conference.getFullName(),"管理员拒绝了你的会议请求",RESPONSE,1);
            messageRepository.save(message);
            return "success";
        }
    }

    public void  reviewConference(List<Conference> conferences){
        List<Conference> temp=conferenceRepository.findAllByReviewStatus(1);
        conferences.addAll(temp);

    }

}
