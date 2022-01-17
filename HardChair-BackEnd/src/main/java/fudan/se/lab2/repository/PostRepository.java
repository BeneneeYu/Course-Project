package fudan.se.lab2.repository;
import fudan.se.lab2.domain.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<Post,Long> {
   Post findByArticleID(Long articleID);
}
