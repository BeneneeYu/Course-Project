package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.MarkMessageRequest;
import fudan.se.lab2.controller.response.AllMessageResponse;
import fudan.se.lab2.security.jwt.JwtTokenUtil;
import fudan.se.lab2.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@CrossOrigin()
@Controller
public class MessageController {
    private Logger logger = LoggerFactory.getLogger(MessageController.class);
    private MessageService messageService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    public MessageController(MessageService messageService){
        this.messageService=messageService;
    }

    String messageStr = "message";

    @CrossOrigin("*")
    @PostMapping("/mailCenter")
    public ResponseEntity<HashMap<String,Object>> openMailCenter(HttpServletRequest httpServletRequest){
        logger.debug("gettingAllMessages");
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String userName=jwtTokenUtil.getUsernameFromToken(token);
        List<AllMessageResponse> messages=messageService.getAllMessage(userName);
        HashMap<String,Object> map=new HashMap();
        map.put("token",token);
        map.put("messages",messages);
        return ResponseEntity.ok(map);
    }

    @CrossOrigin("*")
    @PostMapping("/markRead")
    public ResponseEntity<HashMap<String,Object>> markRead(@RequestBody MarkMessageRequest request,HttpServletRequest httpServletRequest){
        logger.debug("markingMessages "+request.toString());
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        HashMap<String,Object> map=new HashMap();
        boolean mark=messageService.markRead(request);
        if(!mark){
            map.put(messageStr,"标记失败");
            return ResponseEntity.ok(map);
        }
        map.put(messageStr,"success");
        map.put("token",token);
        return ResponseEntity.ok(map);
    }


}
