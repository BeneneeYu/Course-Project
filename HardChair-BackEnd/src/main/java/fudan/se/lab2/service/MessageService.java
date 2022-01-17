package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.MarkMessageRequest;
import fudan.se.lab2.controller.response.AllMessageResponse;
import fudan.se.lab2.domain.Conference;
import fudan.se.lab2.domain.Message;
import fudan.se.lab2.domain.Topic;
import fudan.se.lab2.repository.ConferenceRepository;
import fudan.se.lab2.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private ConferenceRepository conferenceRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository,ConferenceRepository conferenceRepository){
        this.messageRepository=messageRepository;
        this.conferenceRepository=conferenceRepository;
    }

    /**
     *
     * @param userName
     * @return List<Message>
     * @description 得到所有未读的消息列表
     * @Author guo taian
     */
    public List<AllMessageResponse> getAllMessage(String userName){
        List<Message> messages=messageRepository.findAllByReceiverName(userName);
        if(messages==null){
            return null;
        }
        List<AllMessageResponse> responses=new ArrayList<>();
        for(Message message:messages){
            Conference conference=conferenceRepository.findByFullName(message.getRelatedConferenceName());
            List<String> topicNames=new ArrayList<>();
            Set<Topic> topicSet=conference.getTopics();
            for(Topic topic:topicSet){
                topicNames.add(topic.getTopic());
            }
            responses.add(new AllMessageResponse(
                    message.getId(),
                    message.getSenderName(),
                    message.getReceiverName(),
                    message.getRelatedConferenceName(),
                    message.getMessage(),
                    message.getMessageCategory(),
                    message.getIsRead(),
                    topicNames
            ));
        }
        return responses;

    }

    /**
     *
     * @param request
     * @description 标记消息为已读
     * @return
     */
    public boolean markRead(MarkMessageRequest request){
        Message messages=messageRepository.findById(request.getId()).orElse(null);
        if(messages==null){
            return false;
        }
        else{
            messages.setIsRead(2);
            messageRepository.save(messages);
            return true;
        }
    }
}
