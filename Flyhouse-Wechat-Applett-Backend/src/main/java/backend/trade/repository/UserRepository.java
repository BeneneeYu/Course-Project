package backend.trade.repository;

import backend.trade.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findTopByOpenid(String openid);
}
