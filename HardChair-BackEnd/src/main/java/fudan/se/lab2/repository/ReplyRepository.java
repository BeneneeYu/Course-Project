package fudan.se.lab2.repository;

import fudan.se.lab2.domain.Reply;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends CrudRepository<Reply,Long> {
}
