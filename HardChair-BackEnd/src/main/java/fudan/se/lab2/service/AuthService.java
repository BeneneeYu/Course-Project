package fudan.se.lab2.service;

import fudan.se.lab2.domain.Authority;
import fudan.se.lab2.domain.User;
import fudan.se.lab2.repository.UserRepository;
import fudan.se.lab2.controller.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;

/**
 * @author LBW
 */
@Service
public class AuthService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder=passwordEncoder;
    }


    public User register(@RequestBody RegisterRequest request) {
        //用户名重复的情况
        if (null != userRepository.findByUsername(request.getUsername())){
            System.out.println("用户名重复");
            return null;
        }
        System.out.println("注册成功！");
        String password = passwordEncoder.encode(request.getPassword());
        HashSet<Authority> set = new HashSet<>();
        User user = new User(request.getUsername(),password,request.getFullName(),request.getEmail(),request.getInstitution(),request.getCountry(),set);
        userRepository.save(user);
        return user;
    }


//    public User modifyInformation(@RequestBody ModifyInformationRequest request) {
//        //用户名重复的情况
//        if (null != userRepository.findByUsername(request.getUsername())){
//            System.out.println("要修改的用户名重复");
//            return null;
//        }else{
//            System.out.println("修改成功！");
//            String password = passwordEncoder.encode(request.getPassword());
//            HashSet<Authority> set = new HashSet<>();
//            Authority authority = authorityRepository.findByAuthority("user");
//            set.add(authority);
//        }
//        return null;
//    }


}
