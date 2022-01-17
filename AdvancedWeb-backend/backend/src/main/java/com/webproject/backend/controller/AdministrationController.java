package com.webproject.backend.controller;

import com.webproject.backend.Response.Message;
import com.webproject.backend.security.JwtTokenUtil;
import com.webproject.backend.service.AdministrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @program: backend
 * @description: 后台的后端功能实现
 * @author: Shen Zhengyu
 * @create: 2021-06-24 19:41
 **/
@RestController
public class AdministrationController {
    private AdministrationService administrationService;
    @Autowired
    public AdministrationController(AdministrationService administrationService){
        this.administrationService = administrationService;
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok(administrationService.getAllUsers());
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getStatistics(){
        HashMap<String,Object> result = new HashMap<String, Object>();
        result.put("ageDistribution",administrationService.getAgeDistributions());
        result.put("maleProportion",administrationService.getSexDistributions());
        return ResponseEntity.ok(result);
    }
}
