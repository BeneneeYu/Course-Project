package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.*;
import fudan.se.lab2.domain.Article;
import fudan.se.lab2.repository.ArticleRepository;
import fudan.se.lab2.repository.UserRepository;
import fudan.se.lab2.security.jwt.JwtTokenUtil;
import fudan.se.lab2.service.ContributionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * @program: lab2
 * @description: 接收投稿
 * @author: Shen Zhengyu
 * @create: 2020-04-08 09:11
 **/
@CrossOrigin
@RestController
public class ContributionController {
    Logger logger = LoggerFactory.getLogger(ContributionController.class);


    @Autowired
    ContributionService contributionService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    ContributionController(ContributionService contributionService) {
        this.contributionService = contributionService;
    }

    String messageStr = "message";

    /**
     * @Description: 接收投稿请求
     * @Param: [httpServletRequest, contributionRequest]
     * @return: org.springframework.http.ResponseEntity<java.util.HashMap < java.lang.String, java.lang.Object>>
     * @Author: Shen Zhengyu
     * @Date: 2020/4/8
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/contribute")
    public ResponseEntity<HashMap<String, Object>> contribute(HttpServletRequest httpServletRequest, @RequestBody ContributionRequest contributionRequest) {
        logger.debug("Try to submit article");
        HashMap<String, Object> map;
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        map = contributionService.contribute(contributionRequest);
        if (!"重复投稿".equals(map.get(messageStr))){
            map.put("token",token);
        }
        return ResponseEntity.ok(map);
    }

    /**
     * @Description: 接收上传的稿件文件
     * @Param: [httpServletRequest, contributionRequest]
     * @return: org.springframework.http.ResponseEntity<java.util.HashMap < java.lang.String, java.lang.Object>>
     * @Author: Shen Zhengyu
     * @Date: 2020/4/8
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/upload")
    public ResponseEntity<HashMap<String, Object>> upload(HttpServletRequest request, @RequestParam("file") MultipartFile file,@RequestParam("conference_id") Long conferenceID,@RequestParam("title") String title) throws IOException, InterruptedException {
        logger.debug("Try to upload...");
        HashMap<String, Object> map = new HashMap();
        String token = request.getHeader("Authorization").substring(7);
        if (null == file ||file.isEmpty()){
            map.put(messageStr, "上传失败，文件为空");
            return ResponseEntity.ok(map);
        }
        else if (contributionService.findFile(conferenceID,title) == 0){
            map.put(messageStr, "重复投稿");
            return ResponseEntity.ok(map);
        }
        else{
            String fileName = saveFile(file, conferenceID);
            map.put(messageStr, "上传成功");
            map.put("存放路径", fileName);
            map.put("token", token);
            return ResponseEntity.ok(map);
            }
        }

    private String saveFile( MultipartFile file, Long conferenceID) throws IOException {
        String fileName = file.getOriginalFilename();
        String path;
        StringBuilder sb = new StringBuilder("/workplace/upload/");
        if (null == conferenceID) {
            sb.append("unknownConferenceID/");
        }else {
            sb.append(conferenceID);
            sb.append("/");
        }
        sb.append(fileName);
        path = sb.toString();
        System.out.println(path);
        mkdirAndFile(path);
        File dest = new File(path);
        file.transferTo(dest);
        return fileName;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/update")
    public ResponseEntity<HashMap<String, Object>> update(HttpServletRequest request, @RequestParam("file") MultipartFile file,@RequestParam("conference_id") Long conferenceID,@RequestParam("articleId") Long articleId)throws IOException {
        logger.debug("Try to update...");
        HashMap<String, Object> map = new HashMap<>();
        String token = request.getHeader("Authorization").substring(7);
        map.put("token", token);
        if (null == file ||file.isEmpty()){
            map.put(messageStr, "上传失败");
            return ResponseEntity.ok(map);
        }
        else {
            String fileName=saveFile(file,conferenceID);
            Article article = articleRepository.findById(articleId).orElse(null);
            if (null != article) {
                article.setFilename(fileName);
                articleRepository.save(article);
                map.put(messageStr, "重新上传成功");
                map.put("存放路径", fileName);
            }
            return ResponseEntity.ok(map);
        }
    }
    /**
    * @Description: 查看稿件的信息
    * @Param: [path]
    * @return: void
    * @Author: Shen Zhengyu
    * @Date: 2020/4/26
    */
    @CrossOrigin(origins = "*")
    @PostMapping("/reviewArticle")
    public ResponseEntity<HashMap<String, Object>> reviewArticle(HttpServletRequest request, @RequestBody ReviewArticleRequest reviewArticleRequest) throws IOException {
        logger.debug("Try to review article...");
        String token = request.getHeader("Authorization").substring(7);
        HashMap<String,Object> message = contributionService.reviewArticle(reviewArticleRequest);
        message.put("token",token);
        return ResponseEntity.ok(message);
    }
    /**
    * @Description: 提交审稿信息
    * @Param: [request, showContributionModificationRequest]
    * @return: org.springframework.http.ResponseEntity<java.util.HashMap<java.lang.String,java.lang.Object>>
    * @Author: Shen Zhengyu
    * @Date: 2020/5/2
    */
    @CrossOrigin(origins = "*")
    @PostMapping("/submitReviewResult")
    public ResponseEntity<HashMap<String, Object>> submitReviewResult(HttpServletRequest request, @RequestBody SubmitReviewResultRequest submitReviewResultRequest){
        logger.debug("Try to submit review result...");
        HashMap<String, Object> answer = new HashMap<>();
        String token = request.getHeader("Authorization").substring(7);
        String message = contributionService.submitReviewResult(submitReviewResultRequest);
        answer.put("token",token);
        answer.put(messageStr,message);
        return ResponseEntity.ok(answer);
    }

    /**
    * @Description: 修改评审结果
    * @Param: [request, submitReviewResultRequest]
    * @return: org.springframework.http.ResponseEntity<java.util.HashMap<java.lang.String,java.lang.Object>>
    * @Author: Shen Zhengyu
    * @Date: 2020/5/28
    */
    @CrossOrigin(origins = "*")
    @PostMapping("/modifyReviewResult")
    public ResponseEntity<HashMap<String, Object>> modifyReviewResult(HttpServletRequest request, @RequestBody SubmitReviewResultRequest submitReviewResultRequest){
        logger.debug("Try to modify review result...");
        HashMap<String, Object> answer = new HashMap<>();
        String token = request.getHeader("Authorization").substring(7);
        String message = contributionService.modifyReviewResult(submitReviewResultRequest);
        answer.put("token",token);
        answer.put(messageStr,message);
        return ResponseEntity.ok(answer);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/confirmReviewResult/{conference_id}/{articleID}/{userId}")
    public ResponseEntity<HashMap<String, Object>> confirmReviewResult(HttpServletRequest request, @PathVariable("userId") Long userId,@PathVariable("articleID") Long articleID,@PathVariable("conference_id") Long conference_id){
        logger.debug("Try to confirm review result...");
        HashMap<String, Object> answer = new HashMap<>();
        String token = request.getHeader("Authorization").substring(7);
        String message = contributionService.confirmReviewResult(userId,articleID,conference_id);
        answer.put("token",token);
        answer.put(messageStr,message);
        return ResponseEntity.ok(answer);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/modifyContribution")
    public ResponseEntity<HashMap<String, Object>> modifyContribution(HttpServletRequest request, @RequestBody ModifyContributionRequest modifyContributionRequest) {
        logger.debug("Try to modify contribution...");
        String token = request.getHeader("Authorization").substring(7);
        HashMap<String,Object> message = contributionService.modifyContribution(modifyContributionRequest);
        message.put("token",token);
        return ResponseEntity.ok(message);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/findArticle/{articleID}")
    public ResponseEntity<HashMap<String, Object>> findArticle(HttpServletRequest request, @PathVariable(name = "articleID") Long articleID) {
        logger.debug("Try to find Article");
        String token = request.getHeader("Authorization").substring(7);
        HashMap<String,Object> message = new HashMap<>();
        message.put("article",contributionService.findArticle(articleID));
        message.put("token",token);
        message.put(messageStr,"查找成功");
        return ResponseEntity.ok(message);
    }

        public void mkdirAndFile(String path) {
        //变量不需赋初始值，赋值后永远不会读取变量，在下一个变量读取之前，该值总是被另一个赋值覆盖
        File f;
        try {
            f = new File(path);
            if (!f.exists()) {
                boolean i = f.getParentFile().mkdirs();
                if (i) {
                    System.out.println("层级文件夹创建成功！");
                } else {
                    System.out.println("层级文件夹创建失败！");
                }
            }
            boolean b = f.createNewFile();
            if (b) {
                System.out.println("文件创建成功！");
            } else {
                System.out.println("文件创建失败！");
            }
        } catch (Exception e) {
            logger.error("error:" + e.getMessage() + e);
        }
    }
}

