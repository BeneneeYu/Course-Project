package com.softwaretest.demo.Controller;

import com.softwaretest.demo.Controller.Request.AllWMPsRequest;
import com.softwaretest.demo.Controller.Request.BenifitsDetailRequest;
import com.softwaretest.demo.Controller.Request.BuyWMPRequest;
import com.softwaretest.demo.Controller.Response.BenifitsDetailResponse;
import com.softwaretest.demo.Service.WMPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WMPController {
    @Autowired
    private WMPService wmpService;

    @PostMapping("/wmp/buy")
    public ResponseEntity<HashMap<String,Object>> buyWMP(@RequestBody BuyWMPRequest request){
        HashMap<String,Object> map = new HashMap<>();
        map.put("success", wmpService.buyWMP(request));
        return ResponseEntity.ok(map);
    }

    @PostMapping("/wmp/allwmps")
    public ResponseEntity<Object> allWMPs(@RequestBody AllWMPsRequest request){
        wmpService.updateWMPs(request.getAccountId());
        return ResponseEntity.ok(wmpService.allWMPs(request.getAccountId()));
    }

    @PostMapping("/wmp/benefits")
    public ResponseEntity<Object> benifitsDetail(@RequestBody BenifitsDetailRequest request){
        HashMap<String, List<BenifitsDetailResponse>> map = new HashMap<>();
        map.put("benifits",wmpService.benifitsDetail(request.getWmpId()));
        return ResponseEntity.ok(map);
    }
}
