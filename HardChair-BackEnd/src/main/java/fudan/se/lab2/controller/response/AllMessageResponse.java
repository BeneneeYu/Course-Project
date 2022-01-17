package fudan.se.lab2.controller.response;

import java.util.*;

public class AllMessageResponse {
    private Long id;
    private String senderName;
    private String receiverName;
    private String relatedConferenceName;
    private String message;
    private String messageCategory;
    private Integer isRead;
    private List<String> topics;

    public AllMessageResponse(Long id,String senderName,String receiverName,String relatedConferenceName,String message,String messageCategory,Integer isRead,List<String> topics){
        this.id=id;
        this.senderName=senderName;
        this.receiverName=receiverName;
        this.message=message;
        this.relatedConferenceName=relatedConferenceName;
        this.messageCategory=messageCategory;
        this.isRead=isRead;
        this.topics=topics;
    }


    public Long getId() {
        return id;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getRelatedConferenceName() {
        return relatedConferenceName;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageCategory() {
        return messageCategory;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public List<String> getTopics() {
        return topics;
    }
}
