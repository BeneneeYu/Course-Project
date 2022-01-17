package fudan.se.lab2.controller;

import fudan.se.lab2.config.CorsConfig;
import fudan.se.lab2.controller.request.SubmitRebuttalRequest;
import fudan.se.lab2.domain.Post;
import fudan.se.lab2.domain.Reply;
import fudan.se.lab2.repository.PostRepository;
import fudan.se.lab2.repository.ReplyRepository;
import fudan.se.lab2.security.jwt.JwtTokenUtil;
import fudan.se.lab2.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author 86460
 */
@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
public class RebuttalController {
    @Autowired
    private PostService postService;

    Logger logger = LoggerFactory.getLogger(RebuttalController.class);


    @Autowired
    public RebuttalController() {
    }

    String tokenStr = "token";
    String messageStr = "message";
    String auth = "Authorization";
    String error = "error";
    String successMes = "success";

    /**
    * @Description: 查看所有与个人有关的帖子（自己有权限查看）
    * @Param: [httpServletRequest, userID]
    * @return: org.springframework.http.ResponseEntity<java.util.HashMap<java.lang.String,java.lang.Object>>
    * @Author: Shen Zhengyu
    * @Date: 2020/5/28
    */
//    @CrossOrigin(origins = "*",allowCredentials = "true")

    /**
    * @Description: 查看某一文章对应的帖子（bijection）
    * @Param: [httpServletRequest, articleID]
    * @return: org.springframework.http.ResponseEntity<java.util.HashMap<java.lang.String,java.lang.Object>>
    * @Author: Shen Zhengyu
    * @Date: 2020/5/28
    */
    @CrossOrigin(origins = "*")
    @RequestMapping("/browsePostOnArticle/{articleID}")
    public ResponseEntity<HashMap<String,Object>> browsePostOnArticle(HttpServletRequest httpServletRequest,@PathVariable(name = "articleID") Long articleID){
        logger.debug("browsePostOnArticle:" + articleID);
        HashMap<String,Object> map = new HashMap<>();
        String token = httpServletRequest.getHeader(auth).substring(7);
        Post post = postService.browsePostsOnArticle(articleID);
        if(post == null){
            map.put(messageStr,"暂无帖子");
            map.put(tokenStr,token);
            map.put("post",null);
            return ResponseEntity.ok(map);

        }
        else{
            map.put(messageStr,"请求成功");
            map.put(tokenStr,token);
            if(null == post.getReplyList()){
                post.setReplyList(new ArrayList<>());
            }
            map.put("post",post);

            return ResponseEntity.ok(map);
        }
    }

    /**
    * @Description: 针对某一个文章发起讨论
    * @Param: [httpServletRequest, articleID, ownerID, words]
    * @return: org.springframework.http.ResponseEntity<java.util.HashMap<java.lang.String,java.lang.Object>>
    * @Author: Shen Zhengyu
    * @Date: 2020/5/28
    */
    @CrossOrigin(origins = "*")
    @PostMapping("/postOnArticle/{articleID}/{ownerID}/{words}")
    public ResponseEntity<HashMap<String,Object>> postOnArticle(HttpServletRequest httpServletRequest,@PathVariable(name = "articleID") Long articleID,@PathVariable(name = "ownerID") Long ownerID,@PathVariable(name = "words") String words){
        logger.debug("Post:" +ownerID + "on" + articleID);
        HashMap<String,Object> map = new HashMap<>();
        String token = httpServletRequest.getHeader(auth).substring(7);
        Long postID = postService.postOnArticle(articleID,ownerID,words);
        if(postID == (long)(-1)){
            map.put(messageStr,"发送失败");
            map.put(tokenStr,token);
            map.put("postID",postID);
            return ResponseEntity.ok(map);

        }else if (postID == (long)(-255)){
            map.put(messageStr,"重复发送");
            map.put(tokenStr,token);
            map.put("postID",postID);
            return ResponseEntity.ok(map);
        }
        else{
            map.put(messageStr,"发送成功");
            map.put(tokenStr,token);
            map.put("postID",postID);
            return ResponseEntity.ok(map);
        }
    }


    /**
    * @Description: 针对某一个主题帖进行回帖
    * @Param: [httpServletRequest, postID, ownerID, words, floorNumber]
    * @return: org.springframework.http.ResponseEntity<java.util.HashMap<java.lang.String,java.lang.Object>>
    * @Author: Shen Zhengyu
    * @Date: 2020/5/28
    */
    @CrossOrigin(origins = "*")
    @PostMapping("/replyPost/{postID}/{ownerID}/{floorNumber}/{words}")
    public ResponseEntity<HashMap<String,Object>> replyPost(HttpServletRequest request, @PathVariable(name = "postID") Long postID, @PathVariable(name = "ownerID") Long ownerID,@PathVariable(name = "floorNumber") Long floorNumber,@PathVariable(name = "words") String words){
        logger.debug("replyPost");
        HashMap<String,Object> map = new HashMap<>();
        String token = request.getHeader(auth).substring(7);
        System.out.println(ownerID);
        System.out.println(floorNumber);
        System.out.println(words);
        System.out.println(postID);

        Reply reply = postService.replyPost(postID,ownerID,words,floorNumber);
        String message = null == reply?"提交失败":"提交成功";
        map.put(message,message);
            map.put(tokenStr,token);
            map.put("reply",reply);
            return ResponseEntity.ok(map);

    }

    /**
    * @Description: author对评审结果的质疑进行解释
    * @Param: [httpServletRequest, postID, authorID, words, articleID]
    * @return: org.springframework.http.ResponseEntity<java.util.HashMap<java.lang.String,java.lang.Object>>
    * @Author: Shen Zhengyu
    * @Date: 2020/5/28
    */

    @CrossOrigin(origins = "*",allowCredentials = "true")
    @PostMapping(value = "/submitRebuttal")
    public ResponseEntity<HashMap<String,Object>> submitRebuttal(HttpServletRequest httpServletRequest, @RequestBody SubmitRebuttalRequest submitRebuttalRequest, HttpServletResponse response){
        logger.debug(submitRebuttalRequest.getAuthorID() + "submitRebuttal on " + submitRebuttalRequest.getArticleID());
        HashMap<String,Object> map = new HashMap();
        String token = httpServletRequest.getHeader(auth).substring(7);

        Reply reply = postService.submitRebuttal(submitRebuttalRequest.getArticleID(),submitRebuttalRequest.getWords(),submitRebuttalRequest.getAuthorID());
        String message = null == reply?"提交失败":"提交成功";
        map.put(messageStr,message);
        map.put(tokenStr,token);
        map.put("reply",reply);
        response.setContentType("application/json;charset=UTF-8");
        return ResponseEntity.ok(map);

    }

}
