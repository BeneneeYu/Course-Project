package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.*;
import fudan.se.lab2.controller.request.componment.WriterRequest;
import fudan.se.lab2.controller.response.ArticleForPCMemberResponse;
import fudan.se.lab2.controller.response.ResultResponse;
import fudan.se.lab2.domain.*;
import fudan.se.lab2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * @program: lab2
 * @description: 提供上传稿件、查看稿件的服务
 * @author: Shen Zhengyu
 * @create: 2020-04-08 09:20
 **/
@Service
public class ContributionService {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PCMemberRepository pcMemberRepository;


    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private EvaluationModifyRequestRepository evaluationModifyRequestRepository;
    public HashMap<String,Object> contribute(ContributionRequest contributionRequest){
       Article article = articleRepository.findByTitleAndConferenceID(contributionRequest.getTitle(),contributionRequest.getConference_id());
        HashMap<String,Object> hashMap = new HashMap<>();
       if (article!=null){
           hashMap.put("message","重复投稿");
           return  hashMap;
        }
       hashMap.put("message","投稿成功");

        return saveContribution(contributionRequest);
    }

    private HashMap<String,Object> saveContribution(ContributionRequest contributionRequest){
        List<Writer> writers=new ArrayList<>();
        HashMap<String,Object> hashMap = new HashMap<>();
        for(WriterRequest writerRequest:contributionRequest.getWriters()){
            Writer writer=writerRepository.findByWriterNameAndEmail(writerRequest.getWriterName(),writerRequest.getEmail());
            if(writer==null){
                Writer temp=new Writer(writerRequest.getWriterName(),writerRequest.getEmail(),writerRequest.getInstitution(),writerRequest.getCountry());
                writerRepository.save(temp);
                writer=temp;
            }
                writers.add(writer);
        }
        for(int i=0;i<writers.size();i++){
            for(int j=i+1;j<writers.size();j++){
                if(writers.get(i).equals(writers.get(j))){
                    hashMap.put("message","填写了重复作者");
                    return hashMap;
                }
            }
        }
        Article article = new Article(contributionRequest.getConference_id(),contributionRequest.getContributorID(),contributionRequest.getFilename(),contributionRequest.getTitle(),contributionRequest.getArticleAbstract(),writers);

        Set<Topic> topics=new HashSet<>();
        for(String topicName:contributionRequest.getTopics()){
            Topic topic=topicRepository.findByTopic(topicName);
            if(topic==null){
                hashMap.put("message","投稿失败");
                return hashMap;
            }
            topics.add(topic);
        }
        article.setTopics(topics);
        articleRepository.save(article);
        Contributor contributor = new Contributor(contributionRequest.getContributorID(),contributionRequest.getConference_id());
        authorRepository.save(contributor);
        hashMap.put("message","投稿成功");
        hashMap.put("articleId",article.getId());
        return hashMap;
    }

    /**
    * @Description: 思路：查找一个PCMember所有的分配到的稿件
    * @Param: [reviewArticleRequest]
    * @return: java.util.HashMap<java.lang.String,java.lang.Object>
    * @Author: Shen Zhengyu
    * @Date: 2020/5/6
    */

    public HashMap<String,Object> reviewArticle(ReviewArticleRequest reviewArticleRequest){
        PCMember pcMember = pcMemberRepository.findByUserIdAndConferenceId(reviewArticleRequest.getUserId(),reviewArticleRequest.getConference_id());
        HashMap<String, Object> hashMap = new HashMap<>();
        if (null != pcMember) {
            Set<Topic> topicSet = pcMember.getTopics();
            Set<Article> articleSet = pcMember.getArticles();

            List<Topic> topics = new ArrayList<>(topicSet);
            hashMap.put("message","请求成功");
            hashMap.put("topics",topics);
            Set<Article> articlesHaveReviewed = pcMember.getArticlesHaveReviewed();
            List<ArticleForPCMemberResponse> articleForPCMemberResponses = new ArrayList<>();
            for (Article article : articleSet) {
                ArticleForPCMemberResponse articleForPCMemberResponse = new ArticleForPCMemberResponse();
                articleForPCMemberResponse.setArticleAbstract(article.getArticleAbstract());
                articleForPCMemberResponse.setWriters(article.getWriters());
                for (Article article1 : articlesHaveReviewed) {
                    if (article1.getConferenceID().equals(reviewArticleRequest.getConference_id()) && article1.getTitle().equals(article.getTitle())){
                        //已经审稿
                        articleForPCMemberResponse.setStatus(1);
                        break;
                    }else{
                        articleForPCMemberResponse.setStatus(0);
                    }
                }
                articleForPCMemberResponse.setArticleId(article.getId());
                articleForPCMemberResponse.setTitle(article.getTitle());
                articleForPCMemberResponse.setCanPost(article.getCanPost());
                articleForPCMemberResponses.add(articleForPCMemberResponse);
            }
            hashMap.put("articles",articleForPCMemberResponses);
        }else{
            hashMap.put("message","请求失败");
        }
        return hashMap;
    }


    /**
    * @Description: 修改投稿
    * @Param:
    * @return:
    * @Author: Shen Zhengyu
    * @Date: 2020/5/4
    */
    public HashMap<String,Object> modifyContribution(ModifyContributionRequest modifyContributionRequest) {
        HashMap<String,Object> hashMap = new HashMap<>();
        Article article = articleRepository.findByTitleAndConferenceID(modifyContributionRequest.getOriginalTitle(),modifyContributionRequest.getConference_id());
        if (null == article){
             hashMap.put("message","没有该文章");
            return hashMap;
        }
        article.setArticleAbstract(modifyContributionRequest.getArticleAbstract());
        article.setTitle(modifyContributionRequest.getTitle());
        Set<Topic> topics = new HashSet<>();
        for (String topicName : modifyContributionRequest.getTopics()) {
            Topic topic=topicRepository.findByTopic(topicName);
            if(topic==null){
                hashMap.put("message","修改失败");
                return hashMap;
            }
            topics.add(topic);
        }
        article.setTopics(topics);
        List<Writer> writers=new LinkedList<>();
        for(WriterRequest writerRequest:modifyContributionRequest.getWriters()){
            Writer writer=writerRepository.findByWriterNameAndEmail(writerRequest.getWriterName(),writerRequest.getEmail());
            if(writer==null){
                Writer temp=new Writer(writerRequest.getWriterName(),writerRequest.getEmail(),writerRequest.getInstitution(),writerRequest.getCountry());
                writerRepository.save(temp);
                writer=temp;
            }
            else{
                writer.setInstitution(writerRequest.getInstitution());
                writer.setCountry(writerRequest.getCountry());
            }
            writers.add(writer);
        }
        article.setWriters(writers);
        articleRepository.save(article);
        hashMap.put("message","修改成功");
        return hashMap;
    }


        /**
        * @Description: 添加新平均
        * @Param: [submitReviewResultRequest]
        * @return: java.lang.String
        * @Author: Shen Zhengyu
        * @Date: 2020/5/2
        */

    public String submitReviewResult(SubmitReviewResultRequest submitReviewResultRequest){
        //单人的评价
        Article article = articleRepository.findById(submitReviewResultRequest.getArticleId()).orElse(null);
        if (article == null){
            return "NOT FOUND";
        }        //总评价
        Result result = resultRepository.findByArticleIDAndConferenceID(submitReviewResultRequest.getArticleId(),submitReviewResultRequest.getConference_id());
        Evaluation evaluation = new Evaluation(submitReviewResultRequest.getUserId(),submitReviewResultRequest.getScore(),submitReviewResultRequest.getComment(),submitReviewResultRequest.getConfidence());
        if(isDuplicatedReview(result,evaluation)){
            return "请不要重复审稿";
        }
        evaluationRepository.save(evaluation);

        if(null == result){
            Set<Evaluation> evaluations = new HashSet<>();
            evaluations.add(evaluation);
            resultRepository.save(new Result(submitReviewResultRequest.getConference_id(),submitReviewResultRequest.getArticleId(),evaluations));
        }else{
            Set<Evaluation> evaluations = result.getEvaluations();
            evaluations.add(evaluation);
            resultRepository.save(result);
        }
        PCMember pcMember = pcMemberRepository.findByUserIdAndConferenceId(submitReviewResultRequest.getUserId(),submitReviewResultRequest.getConference_id());
        Set<Article> articles = pcMember.getArticlesHaveReviewed();
        Set<Article> articlesToUpdate = new HashSet<>(articles);
        articlesToUpdate.add(article);
        pcMember.setArticlesHaveReviewed(articlesToUpdate);
        pcMemberRepository.save(pcMember);

        article.setHowManyPeopleHaveReviewed(article.getHowManyPeopleHaveReviewed()+1);
        if (article.getHowManyPeopleHaveReviewed() == 3){
            article.setStatus((long)1);
        }
        articleRepository.save(article);
        return"successful contribution";
    }

    private boolean isDuplicatedReview(Result result,Evaluation evaluation){
        if(result==null){
            return false;
        }
        Set<Evaluation> evaluationList=result.getEvaluations();
        for(Evaluation temp:evaluationList){
            if(temp.getPCMemberID().equals(evaluation.getPCMemberID())){
                return true;
            }
        }
        return false;
    }

    /**
    * @Description: 更改评价
    * @Param: [submitReviewResultRequest]
    * @return: java.lang.String
    * @Author: Shen Zhengyu
    * @Date: 2020/5/28
    */
    public String modifyReviewResult(SubmitReviewResultRequest submitReviewResultRequest) {
        Article article = articleRepository.findById(submitReviewResultRequest.getArticleId()).orElse(null);
        if (article == null){
            return "NOT FOUND";
        }
        Result result = resultRepository.findByArticleIDAndConferenceID(submitReviewResultRequest.getArticleId(),submitReviewResultRequest.getConference_id());
        Set<Evaluation> evaluationSet = result.getEvaluations();
        Evaluation evaluationToModify = null;
        for (Evaluation evaluation : evaluationSet) {
            if (evaluation.getPCMemberID().equals(submitReviewResultRequest.getUserId())){
                evaluationToModify = evaluation;
                break;
            }
        }
        if (null == evaluationToModify) {
            return "fail";
        }else if(evaluationToModify.getTimesCanBeModified() == 0){
            return "已无修改机会";
        }else {
            article.setNumberToBeConfirmed(article.getNumberToBeConfirmed()-1);
            EvaluationModifyRequest evaluationModifyRequest = new EvaluationModifyRequest(evaluationToModify);
            evaluationModifyRequest.setComment(submitReviewResultRequest.getComment());
            evaluationModifyRequest.setConfidence(submitReviewResultRequest.getConfidence());
            evaluationModifyRequest.setScore(submitReviewResultRequest.getScore());
            evaluationModifyRequest.setArticleID(result.getArticleID());
            evaluationModifyRequest.setConferenceID(result.getConferenceID());
            evaluationToModify.setTimesCanBeModified(0);
            articleRepository.save(article);
            evaluationRepository.save(evaluationToModify);
            resultRepository.save(result);
            evaluationModifyRequestRepository.save(evaluationModifyRequest);
            return "修改成功";
        }

    }

    public String confirmReviewResult(Long userId,Long articleID, Long conference_id){
        Article article = articleRepository.findById(articleID).orElse(null);
        if (article == null){
            return "NOT FOUND";
        }
        Result result = resultRepository.findByArticleIDAndConferenceID(articleID,conference_id);
        HashSet<Evaluation> evaluationSet = new HashSet<>(result.getEvaluations());
        Evaluation evaluationToModify = new Evaluation();
        for (Evaluation evaluation : evaluationSet) {
            if (evaluation.getPCMemberID().equals(userId)){
                evaluationToModify = evaluation;
                break;
            }
        }
        if (evaluationToModify.getTimesCanBeModified() >0) {
            evaluationToModify.setTimesCanBeModified(0);
            article.setNumberToBeConfirmed(article.getNumberToBeConfirmed() - 1);
            evaluationRepository.save(evaluationToModify);
            articleRepository.save(article);
            resultRepository.save(result);
            return "确认成功";
        }else{
            return "已确认过";
        }
    }


        /**
        * @Description: 作为作者，查看自己投稿所获得的评价
        * @Param: [conference_id, userId]
        * @return: java.util.HashMap<java.lang.String,java.lang.Object>
        * @Author: Shen Zhengyu
        * @Date: 2020/5/7
        */
    public HashMap<String,Object> viewReviewResult(@RequestParam("conference_id") Long conference_id,@RequestParam("userId") Long userId) {
        //一个作者可能参与了多个会议，投了多次稿件
        //首先获得所有它参与过的投稿
        HashMap<String,Object> hashMap = new HashMap<>();
        Set<ResultResponse> resultResponses = new HashSet<>();
        List<Article> articles = articleRepository.findByContributorIDAndConferenceID(userId,conference_id);
        if(articles==null){
            hashMap.put("message","error");
            return hashMap;
        }
        for (Article article : articles) {
            Result result = resultRepository.findByArticleIDAndConferenceID(article.getId(),article.getConferenceID());
            ResultResponse resultResponse = new ResultResponse(article,result);
            resultResponses.add(resultResponse);
        }
        hashMap.put("resultResponses",resultResponses);
        hashMap.put("message","请求成功");
        return hashMap;
    }

    public Article findArticle(Long articleID){
        Article article = articleRepository.findById(articleID).orElse(null);
        return article;
    }

    public int findFile(Long conferenceID,String title){
        //此时已投稿成功
        Article article = articleRepository.findByTitleAndConferenceID(title,conferenceID);
        if (null != article){
            //上传成功了
            return 1;
        }else{
            return 0;
        }
    }

}
