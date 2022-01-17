package com.softwaretest.demo.Controller;

import com.softwaretest.demo.Controller.Request.FlowRequest;
import com.softwaretest.demo.Controller.Response.FlowResponse;
import com.softwaretest.demo.Service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
public class FlowController {

    @Autowired
    private FlowService flowService;

    public FlowController(){

    }

    @CrossOrigin("*")
    @PostMapping("/account/flow")
    public ResponseEntity<HashMap<String,Object>> getFlows(@RequestBody FlowRequest request){
        HashMap<String,Object> map = new HashMap<>();
        List<FlowResponse>  responses = null;
        if(request.getOption() == null&& request.getAccountId()== null){
            responses = flowService.findAll();
        }
        else if("amount".equals(request.getOption())){
            if(request.getAccountId()!=null){
                responses = flowService.findByAccountIdOrderByAmount(request.getAccountId(),request.getOrder());
            }
            else{
                responses = flowService.findAllOrderByAmount(request.getOrder());
            }
        }
        else if("date".equals(request.getOption())){
            if(request.getAccountId()!=null){
                responses = flowService.findByAccountIdOrderByDate(request.getAccountId(),request.getOrder());
            }
            else{
                responses = flowService.findAllOrderByDate(request.getOrder());
            }
        }
        else{
            responses = flowService.findByAccountIdOrderByAmount(request.getAccountId(),request.getOrder());
        }
        map.put("flows",responses);
        map.put("success",true);
        return ResponseEntity.ok(map);
    }
}
