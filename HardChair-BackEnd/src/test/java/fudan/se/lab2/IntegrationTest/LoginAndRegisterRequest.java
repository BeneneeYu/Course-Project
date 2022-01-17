package fudan.se.lab2.IntegrationTest;

import fudan.se.lab2.Lab2Application;
import fudan.se.lab2.controller.AuthController;
import fudan.se.lab2.controller.request.LoginRequest;
import fudan.se.lab2.controller.request.RegisterRequest;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.HashMap;

@SpringBootTest(classes = Lab2Application.class)
public class LoginAndRegisterRequest {
    @Autowired
    private AuthController authController;

    @Test
    void register() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFullName("guotaian");
        registerRequest.setAuthorities(null);
        registerRequest.setCountry("China");
        registerRequest.setEmail("123@qq.com");
        registerRequest.setInstitution("FD");
        registerRequest.setPassword("123456");
        registerRequest.setUsername("testA");
        ResponseEntity<HashMap<String,Object>> responseEntity = authController.register(registerRequest);
        Assert.isTrue(responseEntity.getBody().get("message").equals("success"));
    }

    @Test
    void login() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("123");
        Assert.isTrue(authController.login(loginRequest).getBody().get("message").equals("密码错误"));
        loginRequest.setUsername((new Date()).toString()+ Math.random());
        Assert.isTrue(authController.login(loginRequest).getBody().get("message").equals("用户不存在"));
        loginRequest.setUsername("admin");
        loginRequest.setPassword("password");
        Assert.isTrue(authController.login(loginRequest).getBody().get("message").equals("success"));

    }
}
