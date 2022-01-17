package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.OpenManuscriptReviewRequest;
import fudan.se.lab2.controller.request.ShowSubmissionRequest;
import fudan.se.lab2.controller.response.AllConferenceResponse;
import fudan.se.lab2.controller.response.ConferenceForChairResponse;
import fudan.se.lab2.controller.response.ShowSubmissionResponse;
import fudan.se.lab2.domain.*;
import fudan.se.lab2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;

@Service
public class MyRelatedConferenceService {
    private ConferenceRepository conferenceRepository;

    private PCMemberRepository pcMemberRepository;

    private AuthorRepository authorRepository;

    private ArticleRepository articleRepository;

    private UserRepository userRepository;

    private EvaluationModifyRequestRepository evaluationModifyRequestRepository;

    private ResultRepository resultRepository;

    private EvaluationRepository evaluationRepository;

    @Autowired
    public MyRelatedConferenceService(ConferenceRepository conferenceRepository, PCMemberRepository pcMemberRepository, AuthorRepository authorRepository, ArticleRepository articleRepository, UserRepository userRepository,EvaluationModifyRequestRepository evaluationModifyRequestRepository,ResultRepository resultRepository,EvaluationRepository evaluationRepository) {
        this.conferenceRepository = conferenceRepository;
        this.pcMemberRepository = pcMemberRepository;
        this.authorRepository = authorRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.evaluationModifyRequestRepository=evaluationModifyRequestRepository;
        this.resultRepository=resultRepository;
        this.evaluationRepository=evaluationRepository;
    }

    public List<ConferenceForChairResponse> showAllConferenceForChair() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long id = userRepository.findByUsername(username).getId();
        List<Conference> conferences = conferenceRepository.findAllByChairId(id);
        List<ConferenceForChairResponse> conferenceForChairResponses = new ArrayList<>();
        for (Conference conference : conferences) {
            ConferenceForChairResponse conferenceForChairResponse = new ConferenceForChairResponse();
            conferenceForChairResponse.setConference(conference);
            List<Article> articles = articleRepository.findByConferenceID(conference.getId());
            int flag = 1;
            for (Article article : articles) {
                if (article.getStatus() == 0) {
                    flag = 0;
                    break;
                }
            }
            conferenceForChairResponse.setCanRelease(flag);
            conferenceForChairResponses.add(conferenceForChairResponse);
        }
        return conferenceForChairResponses;
    }


    public List<Conference> showAllConferenceForPCMember() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long id = userRepository.findByUsername(username).getId();
        List<Conference> conferences = new ArrayList<>();
        List<PCMember> myRelated = pcMemberRepository.findAllByUserId(id);
        for (PCMember pcMember : myRelated) {
            Conference conference = conferenceRepository.findById(pcMember.getConferenceId()).orElse(null);
            if (conference == null) {
                return null;
            }
            conferences.add(conference);
        }
        return conferences;
    }

    public List<Conference> showAllConferenceForAuthor() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(username);
        Long id = userRepository.findByUsername(username).getId();
        List<Conference> conferences = new ArrayList<>();
        List<Contributor> myRelated = authorRepository.findAllByUserId(id);
        Set<Long> conferenceIDList = new HashSet<>();
        for (Contributor contributor : myRelated) {
            conferenceIDList.add(contributor.getConferenceId());
        }
        for (Long conferenceId : conferenceIDList) {
            Conference conference = conferenceRepository.findById(conferenceId).orElse(null);
            if (conference == null) {
                return null;
            }
            conferences.add(conference);
        }
        return conferences;
    }

    public List<Article> getAllArticles(Long conferenceID){
        if(conferenceID==null){
            return null;
        }
        return articleRepository.findByConferenceID(conferenceID);
    }

    public List<Article> getAllArticlesAccepted(Long conferenceID){
        if(conferenceID==null){
            return null;
        }
        return articleRepository.findByConferenceIDAndIsAccepted(conferenceID,1);
    }

    public boolean isChair(Long conferenceID){
        String userName=SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userRepository.findByUsername(userName);
        Conference conference=conferenceRepository.findById(conferenceID).orElse(null);
        if(conference==null||user==null){
            return false;
        }
        return conference.getChairId().equals(user.getId());
    }

    public boolean openSubmission(String full_name) {
        Conference conference = conferenceRepository.findByFullName(full_name);
        if(conference==null||conference.getIsOpenSubmission()!=1){
            return false;
        }
        conference.setIsOpenSubmission(2);
        conferenceRepository.save(conference);
        return true;
    }

    public List<ShowSubmissionResponse> showSubmission(ShowSubmissionRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        List<Article> articles = articleRepository.findByContributorIDAndConferenceID(user.getId(), request.getConference_id());
        List<ShowSubmissionResponse> responses = new LinkedList<>();
        for (Article article : articles) {
            Conference conference = conferenceRepository.findById(article.getConferenceID()).orElse(null);
            if (conference != null) {
                String name = conference.getFullName();
                ShowSubmissionResponse showSubmissionResponse = new ShowSubmissionResponse(name, article.getFilename(), article.getTitle(), article.getArticleAbstract(), article.getStatus());
                Set<Topic> topicsRaw = article.getTopics();
                Set<String> topics = new HashSet<>();

                for (Topic topic : topicsRaw) {
                    topics.add(topic.getTopic());
                }
                showSubmissionResponse.setTopics(topics);
                showSubmissionResponse.setWriters(article.getWriters());
                showSubmissionResponse.setArticleID(article.getId());
                responses.add(showSubmissionResponse);
            }
        }
        return responses;
    }

    public List<Conference> showAllConference() {
        return conferenceRepository.findAllByReviewStatus(2);
    }

    private String getChairName(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        return user.getUsername();

    }

    public List<AllConferenceResponse> getResponses2(List<ConferenceForChairResponse> conferences) {
        List<AllConferenceResponse> responses = new LinkedList<>();
        for (ConferenceForChairResponse conferenceForChairResponse : conferences) {
            Conference conference = conferenceForChairResponse.getConference();
            List<String> topicNames = new LinkedList<>();
            for (Topic topic : conference.getTopics()) {
                topicNames.add(topic.getTopic());
            }
            String chairName = getChairName(conference.getChairId());
            AllConferenceResponse allConferenceResponse = new AllConferenceResponse(conference.getId(), conference.getFullName(), conference.getAbbreviation(), conference.getHoldingPlace(), conference.getHoldingTime(), conference.getSubmissionDeadline(), conference.getReviewReleaseDate(), conference.getReviewStatus(), chairName, conference.getIsOpenSubmission(), topicNames);
            allConferenceResponse.setCan_release(conferenceForChairResponse.getCanRelease());
            responses.add(allConferenceResponse);
        }
        return responses;
    }

    public List<AllConferenceResponse> getResponses(List<Conference> conferences) {
        List<AllConferenceResponse> responses = new LinkedList<>();
        for (Conference conference : conferences) {
            List<String> topicNames = new LinkedList<>();
            for (Topic topic : conference.getTopics()) {
                topicNames.add(topic.getTopic());
            }
            String chairName = getChairName(conference.getChairId());
            responses.add(new AllConferenceResponse(conference.getId(), conference.getFullName(), conference.getAbbreviation(), conference.getHoldingPlace(), conference.getHoldingTime(), conference.getSubmissionDeadline(), conference.getReviewReleaseDate(), conference.getReviewStatus(), chairName, conference.getIsOpenSubmission(), topicNames));
        }
        return responses;
    }

    public String releaseReviewResult(Long conference_id) {
        Conference conference = conferenceRepository.findById(conference_id).orElse(null);

        if (null != conference) {
            List<Article> articles = articleRepository.findByConferenceID(conference.getId());
            for (Article article : articles) {
                if (!article.getHowManyPeopleHaveReviewed().equals(3)) {
                    return "开启失败，有稿件未审完";
                }
            }
            for (Article article : articles) {
                article.setStatus((long)2);
                Result result = resultRepository.findByArticleIDAndConferenceID(article.getId(),conference_id);
                Set<Evaluation> evaluations = result.getEvaluations();
                article.setIsAccepted(1);
                for (Evaluation evaluation : evaluations) {
                    if (evaluation.getScore() <= -1){
                        article.setIsAccepted(-1);
                        break;
                    }
                }
                articleRepository.save(article);
            }
            updateEvaluation(conference_id);
            conference.setIsOpenSubmission(Math.max(conference.getIsOpenSubmission(), 4));
            conferenceRepository.save(conference);
            return "开启成功";
        }
        return "开启失败";

    }

    public String releaseFinalReviewResult(Long conference_id) {
        Conference conference = conferenceRepository.findById(conference_id).orElse(null);
        if (null != conference) {
            List<Article> articles = articleRepository.findByConferenceID(conference.getId());
            for (Article article : articles) {
                if (!article.canBeReleased()) {
                    return "暂时还不能发布，有人未确认或对于author的rebuttal还没有进行讨论";
                }
            }
            for (Article article : articles) {
                article.setStatus((long)3);
                articleRepository.save(article);
            }
            updateEvaluation(conference_id);
            conference.setIsOpenSubmission(Math.max(conference.getIsOpenSubmission(), 5));
            conferenceRepository.save(conference);
            return "开启成功";
        }
        return "开启失败";

    }


    /**
     * @Description: 更新一个会议的每篇文章的审阅结果
     * @Param: [conference_id]
     * @return: void
     * @Author: Shen Zhengyu
     * @Date: 2020/5/29
     */
    private void updateEvaluation(Long conference_id) {
        HashSet<EvaluationModifyRequest> evaluationModifyRequests = new HashSet<>(evaluationModifyRequestRepository.findAllByConferenceID(conference_id));
        for (EvaluationModifyRequest evaluationModifyRequest : evaluationModifyRequests) {
            Result result = resultRepository.findByArticleIDAndConferenceID(evaluationModifyRequest.getArticleID(), evaluationModifyRequest.getConferenceID());
            HashSet<Evaluation> evaluationSet = new HashSet<>(result.getEvaluations());
            Evaluation evaluationToModify = null;
            for (Evaluation evaluation : evaluationSet) {
                if (evaluation.getPCMemberID().equals(evaluationModifyRequest.getPCMemberID())) {
                    evaluationToModify = evaluation;
                    break;
                }
            }
            if (null != evaluationToModify) {
                evaluationToModify.setComment(evaluationModifyRequest.getComment());
                evaluationToModify.setConfidence(evaluationModifyRequest.getConfidence());
                evaluationToModify.setScore(evaluationModifyRequest.getScore());
                evaluationRepository.save(evaluationToModify);
                resultRepository.save(result);
                evaluationModifyRequestRepository.delete(evaluationModifyRequest);
            }
        }
        ArrayList<Article> articles = new ArrayList<>(articleRepository.findByConferenceID(conference_id));
        arti:for (Article article : articles) {
            Result result = resultRepository.findByArticleIDAndConferenceID(article.getId(),conference_id);
            ArrayList<Evaluation>  evaluations = new ArrayList<>(result.getEvaluations());
            article.setIsAccepted(1);
            for (Evaluation evaluation : evaluations) {
                if (evaluation.getScore() <= -1){
                    article.setIsAccepted(-1);
                    continue arti;
                }
            }
        }
    }

    public String openManuscriptReview(OpenManuscriptReviewRequest request) {
        Long conferenceId = request.getConference_id();
        if (conferenceId == null) {
            return "请求错误";
        }
        Conference conference = conferenceRepository.findById(conferenceId).orElse(null);
        List<PCMember> pcMembersForConference = pcMemberRepository.findAllByConferenceId(conferenceId);
        if (conference == null||pcMembersForConference == null) {
            return "服务器错误";
        }
        if (pcMembersForConference.size() < 3) {
            return "邀请的PCMember数量少于2个，您不能开启审稿";
        }
        List<Article> articles = articleRepository.findByConferenceID(conferenceId);
        HashMap<PCMember, List<Article>> results = new HashMap<>();
        String allocateResult=allocateManuscripts(request.getAllocationStrategy(),articles,pcMembersForConference,results);
        if(allocateResult.equals("稿件分配成功")){
            save(results, pcMembersForConference);
            conference.setIsOpenSubmission(3);
            conferenceRepository.save(conference);
        }
        System.out.println("结果是: "+allocateResult);
        return allocateResult;
    }

    private String allocateManuscripts(Integer strategy,List<Article> articles,List<PCMember> pcMembers,HashMap<PCMember, List<Article>> results) {
        if (strategy == null||(strategy!=1&&strategy!=2)) {
            return "请求错误";
        }
        if (strategy == 1) {
            for (Article article : articles) {
                if (!allocateBasedOnTopics(article, pcMembers, results)) {
                    return "邀请的PCMember不符合条件导致无法分配";
                }
            }
        }
        else {
            for (Article article : articles) {
                if (!allocateAll(article, pcMembers, results)) {
                    return "邀请的PCMember不符合条件导致无法分配";
                }
            }
        }
        return "稿件分配成功";
    }

    private boolean allocateBasedOnTopics(Article article, List<PCMember> pCMembers, HashMap<PCMember, List<Article>> results) {
        List<PCMember> matchingPCMembers = new LinkedList<>();
        Iterator<PCMember> pcMemberIterator = pCMembers.iterator();
        while (pcMemberIterator.hasNext()) {
            PCMember temp = pcMemberIterator.next();
            if (isNotFit(article, temp)) {
                pcMemberIterator.remove();
            }
        }
        if (pCMembers.size() < 3) {
            return false;
        }

        for (PCMember pcMember : pCMembers) {
            if (isMatch(article, pcMember)) {
                matchingPCMembers.add(pcMember);
            }
        }

        if (matchingPCMembers.size() < 3) {
            int[] random = getRandomNumbers(pCMembers.size());
            saveAllocations(random,pCMembers,article,results);
        } else if (matchingPCMembers.size() == 3) {
            saveAllocations(matchingPCMembers,article,results);
        } else {
            int[] random = getRandomNumbers(matchingPCMembers.size());
            saveAllocations(random,matchingPCMembers,article,results);
        }
        return true;
    }

    private boolean allocateAll(Article article, List<PCMember> pCMembers, HashMap<PCMember, List<Article>> results) {
        List<PCMember> temp = new LinkedList<>(pCMembers);
        int minimumNumber ;

        for (int i = 0; i < 3; i++) {
            minimumNumber=getMinimumNumber(pCMembers,results);
            List<PCMember> feasiblePCMembers = new LinkedList<>();
            for (PCMember pcMember : temp) {
                if (pcMember.getArticles().size() <= minimumNumber) {
                    feasiblePCMembers.add(pcMember);
                }
            }
            for (Iterator<PCMember> pcMemberIterator = feasiblePCMembers.iterator(); pcMemberIterator.hasNext(); ) {
                PCMember feasiblePCMember = pcMemberIterator.next();
                if (isNotFit(article, feasiblePCMember)) {
                    pcMemberIterator.remove();
                }
            }

            if (feasiblePCMembers.size() < 1) {
                return false;
            }
            int pcMemberSelectedIndex = (new Random().nextInt(Integer.MAX_VALUE)) % feasiblePCMembers.size();
            PCMember matchingPCMember = feasiblePCMembers.get(pcMemberSelectedIndex);
            saveAllocation(matchingPCMember, article, results);
            temp.remove(matchingPCMember);
        }

        return true;
    }

    /*
    判断稿件topic与PCMEMBER负责topic是否相符
     */
    private boolean isMatch(Article article, PCMember pcMember) {
        Set<Topic> topicsForArticles = article.getTopics();
        Set<Topic> topicsForPCMember = pcMember.getTopics();
        for (Topic topicForArticle : topicsForArticles) {
            for (Topic topicForPCMember : topicsForPCMember) {
                if (topicForArticle.getTopic().equals(topicForPCMember.getTopic())) {
                    return true;
                }
            }
        }
        return false;
    }
    /*
    判断稿件是否可以被该PCMember审阅
     */

    private boolean isNotFit(Article article, PCMember pcMember) {
        if (article.getContributorID().equals(pcMember.getUserId())) {
            return true;
        }
        User user = userRepository.findById(pcMember.getUserId()).orElse(null);
        if (user == null) {
            return true;
        }
        List<Writer> writers = article.getWriters();
        for (Writer writer : writers) {
            if (writer.getWriterName().equals(user.getFullName()) && writer.getEmail().equals(user.getEmail())) {
                return true;
            }
        }
        return false;
    }

    private void saveAllocation(PCMember pcMember, Article article, HashMap<PCMember, List<Article>> results) {
        List<Article> articleSaved = results.containsKey(pcMember) ? results.get(pcMember) : new LinkedList<>();
        articleSaved.add(article);
        results.put(pcMember, articleSaved);
    }

    private void saveAllocations(List<PCMember> pcMembers,Article article,HashMap<PCMember,List<Article>> results){
        for(PCMember pcMember:pcMembers){
            List<Article> articleSaved=results.containsKey(pcMember)?results.get(pcMember):new LinkedList<>();
            articleSaved.add(article);
            results.put(pcMember,articleSaved);
        }

    }

    private void saveAllocations(int[] random,List<PCMember> pcMembers,Article article,HashMap<PCMember,List<Article>> results){
        for(int index:random){
            List<Article> articleSaved=results.containsKey(pcMembers.get(index))?results.get(pcMembers.get(index)):new LinkedList<>();
            articleSaved.add(article);
            results.put(pcMembers.get(index),articleSaved);
        }
    }


    private void save(HashMap<PCMember, List<Article>> results, List<PCMember> pcMembers) {
        for (PCMember pcMember : pcMembers) {
            List<Article> articles = results.get(pcMember);
            if (articles != null) {
                Set<Article> articleForPCMembers=pcMember.getArticles();
                articleForPCMembers.addAll(articles);
                pcMember.setArticles(articleForPCMembers);
                pcMemberRepository.save(pcMember);
                for(Article article:articles){
                    Set<PCMember> pcMemberForArticles=article.getPcMembers();
                    pcMemberForArticles.add(pcMember);
                    article.setPcMembers(pcMemberForArticles);
                }
                articleRepository.saveAll(articles);
            }
        }
    }



    private static int[] getRandomNumbers(int max) {
        int[] source = new int[max];
        for (int i = 0; i < source.length; i++) {
            source[i] = i;
        }

        int[] result = new int[3];
        SecureRandom rd = new SecureRandom();
        int index;
        int len = source.length;
        for (int i = 0; i < result.length; i++) {
            //待选数组0到(len-2)随机一个下标
            index = Math.abs(rd.nextInt() % len--);
            //将随机到的数放入结果集
            result[i] = source[index];
            //将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
            source[index] = source[len];
        }
        return result;
    }

    private int getMinimumNumber(List<PCMember> pCMembers,HashMap<PCMember,List<Article>> results){
        int minimumNumber=Integer.MAX_VALUE;
        for (PCMember pcMember : pCMembers) {
            int allocatedNumber=0;
            if(results.get(pcMember)!=null){
                allocatedNumber=results.get(pcMember).size();
            }
            if(allocatedNumber<minimumNumber){
                minimumNumber=allocatedNumber;
            }
        }
        return minimumNumber;
    }
}