package fudan.se.lab2.repository;

import fudan.se.lab2.domain.Article;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface ArticleRepository extends CrudRepository<Article,Long> {
    //考虑到可能有标题相同的文章，要用ConferenceID区分
    Article findByTitleAndConferenceID(String title,Long conferenceId);
    List<Article> findByConferenceID(Long conferenceID);
    List<Article> findByContributorIDAndConferenceID(Long ContributorID,Long ConferenceID);
    List<Article> findByConferenceIDAndIsAccepted(Long conferenceID,Integer isAccepted);
}
