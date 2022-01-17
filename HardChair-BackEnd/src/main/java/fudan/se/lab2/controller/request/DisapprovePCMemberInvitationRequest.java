package fudan.se.lab2.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class DisapprovePCMemberInvitationRequest {
    private String senderName;
    private String receiverName;
    private String relatedConferenceName;

    @Autowired
    public DisapprovePCMemberInvitationRequest(){

    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getRelatedConferenceName() {
        return relatedConferenceName;
    }

    public void setRelatedConferenceName(String relatedConferenceName) {
        this.relatedConferenceName = relatedConferenceName;
    }


}
