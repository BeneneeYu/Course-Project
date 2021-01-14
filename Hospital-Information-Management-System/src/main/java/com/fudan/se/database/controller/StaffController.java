package com.fudan.se.database.controller;

import com.fudan.se.database.domain.Staff;
import com.fudan.se.database.repository.StaffRepository;
import com.fudan.se.database.request.LoginRequest;
import com.fudan.se.database.request.ModifyStaffRequest;
import com.fudan.se.database.request.RegisterRequest;
import com.fudan.se.database.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @program: database
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-11-25 13:34
 **/
@RestController()
public class StaffController {
    StaffService staffService;
    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    private final Logger logger = LoggerFactory.getLogger(StaffController.class);

    @PostMapping("/register")
    public ResponseEntity<HashMap<String,Object>> register(HttpServletRequest httpServletRequest,@RequestBody RegisterRequest registerRequest){
        String name = registerRequest.getName();
        String password = registerRequest.getPassword();
        Integer age = registerRequest.getAge();
        Integer gender = registerRequest.getGender();
        Integer job = registerRequest.getJob();
        HashMap<String,Object> hashMap = new HashMap<>();
        Staff staff = staffService.register(name,password,age,gender,job);
        hashMap.put("staff",staff);
        logger.info("register:" + staff.toString());
        return ResponseEntity.ok(hashMap);
    }


    @PostMapping("/login")
    public ResponseEntity<HashMap<String,Object>> login(HttpServletRequest httpServletRequest, @RequestBody LoginRequest loginRequest){
        HashMap<String,Object> hashMap = new HashMap<>();
        Integer id = loginRequest.getId();
        String password = loginRequest.getPassword();
        Staff staff = staffService.login(id,password);
        hashMap.put("staff",staff);
        if(null != staff) {
            logger.info("login:" + staff.toString());
        }
        return ResponseEntity.ok(hashMap);
    }

    @PostMapping("/modifyStaff")
    public ResponseEntity<HashMap<String,Object>> modifyStaff(HttpServletRequest httpServletRequest, @RequestBody ModifyStaffRequest modifyStaffRequest){
        HashMap<String,Object> hashMap = new HashMap<>();
        Integer staffId = modifyStaffRequest.getStaffId();
        Integer age = modifyStaffRequest.getAge();
        Integer gender= modifyStaffRequest.getGender();
        String name = modifyStaffRequest.getName();
        String password = modifyStaffRequest.getPassword();
        Staff staff = staffService.modifyStaff(staffId,password,name,age,gender);
        hashMap.put("staff",staff);
        if(null != staff) {
            logger.info("modify:" + staff.toString());
        }
        return ResponseEntity.ok(hashMap);
    }
}
