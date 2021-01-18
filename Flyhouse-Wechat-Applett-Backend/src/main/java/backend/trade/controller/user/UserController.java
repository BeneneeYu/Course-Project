package backend.trade.controller.user;

import backend.trade.config.AppProperties;
import backend.trade.domain.User;
import backend.trade.dto.MyResponse;
import backend.trade.repository.UserRepository;
import backend.trade.service.WXOpenService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * @program: trade
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-24 09:26
 **/
@RestController
@Slf4j
@RequestMapping(path = "/user")
@CrossOrigin
public class UserController {
    private final AppProperties appProperties;
    private final UserRepository userRepository;
    private final WXOpenService wxOpenService;

    @Autowired
    public UserController(UserRepository userRepository, AppProperties appProperties, WXOpenService wxOpenService) {
        this.userRepository = userRepository;
        this.appProperties = appProperties;
        this.wxOpenService = wxOpenService;
    }

    @GetMapping(path = "/test")
    public MyResponse test() {
        return MyResponse.success("Connect successfully.");
    }

    @GetMapping(path = "/login")
    public MyResponse login(@RequestParam final String code) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            final String baseUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appProperties.getAppId() + "&secret=" + appProperties.getAppSecret() + "&js_code=" + code + "&grant_type=authorization_code";
            URI uri = new URI(baseUrl);

            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseEntity.getBody());

            if (root.path("errcode").asInt() == 0) {
                // get openid successfully.
                String openid = root.path("openid").asText();
                User user = userRepository.findTopByOpenid(openid);
                if (user == null) {
                    user = new User();
                    user.setOpenid(openid);
                    userRepository.save(user);
                    log.info("Add a new user: " + user);
                }
                return MyResponse.success("登陆成功", responseEntity.getBody());
            } else {
                // get openid failed
                return MyResponse.fail("登陆失败", root.path("errmsg").asText());
            }
        } catch (Exception e) {
            return MyResponse.fail("登陆失败", e);
        }

    }


    @GetMapping(path = "/loginBetter")
    public MyResponse login2(@RequestParam final String code) {
        String openid = wxOpenService.getOpenId(code);
        User user = userRepository.findTopByOpenid(openid);
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            userRepository.save(user);
            log.info("Add a new user: " + user);
        }
        return MyResponse.success("登陆成功", openid);
    }

    @GetMapping(path = "/notification")
    public MyResponse getNotification(@RequestParam final String openId) {
        User user = userRepository.findTopByOpenid(openId);
        if (user == null) {
            return MyResponse.fail("openId 无效");
        }
        return MyResponse.success("", user.getNotification());
    }

    @GetMapping(path = "/clearNotification")
    public MyResponse clearNotification(@RequestParam final String openId) {
        User user = userRepository.findTopByOpenid(openId);
        if (user == null) {
            return MyResponse.fail("openId 无效");
        }
        user.setNotification(0);
        userRepository.save(user);
        return MyResponse.success("成功清空Notification");
    }
}
