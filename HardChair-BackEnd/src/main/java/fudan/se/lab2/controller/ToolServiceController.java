package fudan.se.lab2.controller;

import fudan.se.lab2.domain.User;
import fudan.se.lab2.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-28 16:26
 **/
@CrossOrigin
@Controller
public class ToolServiceController {
    Logger logger = LoggerFactory.getLogger(ToolServiceController.class);

    @Autowired
    UserRepository userRepository;

    String tokenStr = "token";
    @CrossOrigin("*")
    @RequestMapping("/findUser/{userID}")
    public ResponseEntity<HashMap<String,Object>> findUser(HttpServletRequest httpServletRequest,@PathVariable(name = "userID") Long userID){
        logger.debug("Find user " + userID);
        HashMap<String,Object> map = new HashMap();
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        User user = userRepository.findById(userID).orElse(null);
        if(null == user){
            map.put("message","查不到该用户");
            map.put(tokenStr,token);
            map.put("user",null);

        }
        else{
            map.put("message","请求成功");
            map.put(tokenStr,token);
            map.put("user",user);
        }
        return ResponseEntity.ok(map);
    }


}
