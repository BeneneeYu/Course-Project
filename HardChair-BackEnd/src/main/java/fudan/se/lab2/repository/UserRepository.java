package fudan.se.lab2.repository;

import fudan.se.lab2.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @author LBW
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
    List<User> findAllByFullNameContaining(String searchKey);
}
