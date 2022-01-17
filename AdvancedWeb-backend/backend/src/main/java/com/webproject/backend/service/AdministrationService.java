package com.webproject.backend.service;

import com.webproject.backend.entity.User;
import com.webproject.backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @program: backend
 * @description: 后台功能
 * @author: Shen Zhengyu
 * @create: 2021-06-24 19:42
 **/
@Service
public class AdministrationService {
    private UserMapper userMapper;

    @Autowired
    public AdministrationService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Set<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    public int[] getAgeDistributions(){
        Set<User> users = userMapper.getAllUsers();
        int result[] = new int[10];
        for (User user : users) {
            result[(int)user.getAge()/10] += 1;
        }
        return result;
    }

    public double getSexDistributions(){
        Set<User> users = userMapper.getAllUsers();
        double all = 0;
        double male = 0;
        for (User user : users) {
            if (user.getGender().equals("男")){
                male++;
            }
            all++;
        }
        return ((0.0 + male)/all);
    }
}
