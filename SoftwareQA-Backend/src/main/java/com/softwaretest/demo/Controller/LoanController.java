package com.softwaretest.demo.Controller;

import com.softwaretest.demo.Controller.Request.*;
import com.softwaretest.demo.Controller.Response.AccountDetailsResponse;
import com.softwaretest.demo.Controller.Response.AccountResponse;
import com.softwaretest.demo.Domain.Flow;
import com.softwaretest.demo.Service.LoanService;
import com.softwaretest.demo.Service.PayLoanAutoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Controller
public class LoanController {
    @Autowired
    private LoanService loanService;


    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private PayLoanAutoService payLoanAutoService;
    /*
    @Description : 在查看账号之前需要先验证身份
     */
    @PostMapping("/account/check")
    public ResponseEntity<HashMap<String,Object>>  checkIdentity(@RequestBody CheckIdentityRequest request) {
        HashMap<String,Object> responseMap = new HashMap<>();

        List<AccountResponse> responses = loanService.getAccounts(request.getIdNumber());
        if(responses == null){
            responseMap.put("success",false);
        }
        else{
            responseMap.put("success",true);
            responseMap.put("accounts",responses);
        }
        return ResponseEntity.ok(responseMap);
    }

    @CrossOrigin("*")
    @PostMapping("/account/loan/details")
    public ResponseEntity<HashMap<String,Object>> getLoanDetails(@RequestBody LoanDetailRequest request){
        HashMap<String,Object> map = new HashMap<>();
        List<AccountDetailsResponse> responses = loanService.getLoans(request.getAccountId());
        map.put("loans",responses);
        map.put("success",true);
        return ResponseEntity.ok(map);
    }

    /*
    @Description : 缴纳罚金，罚金缴纳具有原子性，只能全部缴纳或者余额不足退出
     */

    @CrossOrigin("*")
    @PostMapping("/account/loan/payment/fine")
    public ResponseEntity<HashMap<String,Object>>  payFine(@RequestBody FinePaymentRequest request){
        HashMap<String,Object> map = new HashMap<>();
        boolean result = loanService.payFine(request.getLoanId(),request.getFine());
        map.put("success",result);
        return ResponseEntity.ok(map);
    }

    @CrossOrigin("*")
    @PostMapping("/account/loan/payment/repayment")
    public ResponseEntity<HashMap<String,Object>> loanRepay(@RequestBody LoanRepaymentRequest request){
        HashMap<String,Object> map = new HashMap<>();
        String message = loanService.repay(request.getAccountId(),request.getLoanId(),request.getIndex(),request.getAmount());
        if("success".equals(message)){
            map.put("success",true);
            map.put("message","success");
        }
        else{
            map.put("success",false);
            map.put("message",message);
        }
        Deque<Integer> lists= new LinkedList<>();
        return ResponseEntity.ok(map);
    }

    @CrossOrigin("*")
  @PostMapping("/account/loan/payLoanAutomatically")
  public ResponseEntity<HashMap<String,Object>>  payLoanAutomatically(){
    HashMap<String,Object> map = new HashMap<>();
    HashSet<Flow> flows = new HashSet<>(payLoanAutoService.payLoanAutomatically());
    map.put("success",!(flows.isEmpty()));
    map.put("flows",flows);
      for (Flow flow : flows) {
        System.out.println(flow.toString());
      }
    return ResponseEntity.ok(map);
  }
    /*
    @Description: 前端发送请求进行登录，后端调用服务器登录接口
    @Param
       code 表示请求服务器的响应状态码
     */

    @CrossOrigin("*")
    @RequestMapping("/login")
    public ResponseEntity<HashMap<String,Object>> login(@RequestBody LoginRequest request){
        HashMap<String,Object> responseMap = new HashMap<>();

//        Map<String,Object> requestMap = new HashMap<>();
//        requestMap.put("username",request.getUsername());
//        requestMap.put("password",request.getPassword());
//        ResponseEntity<LoginSuccessResponse> loginResponse = restTemplate.postForEntity("http://10.176.122.174:8012/sys/login/restful",requestMap,LoginSuccessResponse.class);
//        LoginSuccessResponse responseBody = loginResponse.getBody();
//        responseMap.put("code",loginResponse.getStatusCodeValue());
//        if(responseBody!=null){
//            responseMap.put("expireTime",responseBody.getExpireTime());
//            responseMap.put("token",responseBody.getToken());
//        }
        responseMap.put("code",200);
        responseMap.put("token","faketoken");
        responseMap.put("expireTime","never");
        return ResponseEntity.ok(responseMap);
    }

    @CrossOrigin("*")
    @RequestMapping("/logout")
    public ResponseEntity<HashMap<String,Object>> logout(@RequestBody LogoutRequest request){
        HashMap<String,Object> responseMap = new HashMap<>();
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("login-token",request.getToken());
        ResponseEntity<String> logoutResponse = restTemplate.getForEntity("http://10.176.122.174:8080:sys/logout",String.class,requestMap);
        responseMap.put("code",logoutResponse.getStatusCodeValue());
        return ResponseEntity.ok(responseMap);
    }
    
    /*
    @Description : 测试用Demo
     */
    @CrossOrigin("*")
    @RequestMapping("/hello")
    public String hello(){
        return "redirect:hello.html";
    }

}

