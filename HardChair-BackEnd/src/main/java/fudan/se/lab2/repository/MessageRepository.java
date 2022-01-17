package fudan.se.lab2.repository;

import fudan.se.lab2.domain.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message,Long> {
    List<Message> findAllByReceiverName(String receiverName);
    Message findAllByReceiverNameAndRelatedConferenceNameAndMessageCategoryAndIsRead(String receiverName,String relatedConferenceName,String messageCategory,Integer isRead);
    Message findBySenderNameAndReceiverNameAndRelatedConferenceNameAndMessageCategoryAndIsRead(String senderName, String receiverName, String conferenceName, String messageCategory, Integer isRead);
}
