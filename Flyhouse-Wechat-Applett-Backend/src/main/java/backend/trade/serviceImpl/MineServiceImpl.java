package backend.trade.serviceImpl;

import backend.trade.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: 泛海杯小程序
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-06-15 16:54
 **/
@Service
public class MineServiceImpl {
    @Autowired
    UserRepository userRepository;

    public Integer showMyCredit(String userOpenId){
        return userRepository.findTopByOpenid(userOpenId).getCreditValue();
    }
}
