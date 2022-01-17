package fudan.se.lab2.domain;

import javax.persistence.*;
import java.util.*;

/**
 * @program: lab2
 * @description: ConferenceArticle
 * @author: Shen Zhengyu
 * @create: 2020-04-08 08:57
 **/
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long conferenceID;

    private Long contributorID;

    private String filename;

    private String title;

    private String articleAbstract;

    //-1代表未被录用，0代表还未判定，1代表被录用了
    //在发布结果的时候，进行判定
    private Integer isAccepted;
    @ManyToMany(cascade = {CascadeType.MERGE},fetch = FetchType.EAGER)
    private List<Writer> writers = new ArrayList<>();

    @ManyToMany(cascade ={CascadeType.MERGE})
    private Set<Topic> topics=new HashSet<>();

    @ManyToMany(cascade ={CascadeType.MERGE},fetch = FetchType.EAGER)
    private Set<PCMember> pcMembers=new HashSet<>();
    //0暂时无法发布 1已经可以发布 2主席已经第一次发布 3主席已经第二次发布
    private Long status;

    //-1代表rebuttal还未被讨论，初始值为0代表不需要被讨论，1代表rebuttal已经被讨论了，可以被发布
    // 当这篇文章被新建讨论或帖子被回复时，为其+1，>=0才可发布，有人提交rebuttal时置为-1
    private Integer isDiscussed;

    private Integer timesLeftForRebuttal;

    //初始为0，设置为3，当每个PCMember确认/修改了评审结果，为其-1，每位PCMember共有一次修改审阅的机会
    private Integer numberToBeConfirmed;

    private Integer HowManyPeopleHaveReviewed;

    private Integer canPost;
    public Article() {
    }

    /**
    * @Description:
    * @Param: [conferenceID, AuthorID, filename, title, articleAbstract]
    * @return:
    * @Author: Shen Zhengyu
    * @Date: 2020/4/8
    */
    public Article(Long conferenceID,Long contributorID,String filename,String title,String articleAbstract,List<Writer> writers)
    {
        this.conferenceID = conferenceID;
        this.contributorID = contributorID;
        this.filename = filename;
        this.title = title;
        this.articleAbstract = articleAbstract;
        this.status = (long)0;
        this.writers = writers;
        this.isDiscussed = 0;
        this.numberToBeConfirmed = 0;
        this.isAccepted = 0;
        this.HowManyPeopleHaveReviewed=0;
        this.canPost = 1;
        this.timesLeftForRebuttal = 1;
    }

    public Integer getTimesLeftForRebuttal() {
        return timesLeftForRebuttal;
    }

    public void setTimesLeftForRebuttal(Integer timesLeftForRebuttal) {
        this.timesLeftForRebuttal = timesLeftForRebuttal;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConferenceID() {
        return conferenceID;
    }

    public void setConferenceID(Long conferenceID) {
        this.conferenceID = conferenceID;
    }

    public Long getContributorID() {
        return contributorID;
    }

    public void setContributorID(Long authorID) {
        this.contributorID = authorID;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticleAbstract() {
        return articleAbstract;
    }

    public void setArticleAbstract(String articleAbstract) {
        this.articleAbstract = articleAbstract;
    }

    public List<Writer> getWriters() {
        return writers;
    }

    public void setWriters(List<Writer> writers) {
        this.writers = writers;
    }

    public Set<Topic> getTopics() {
        return topics;
    }

    public void setTopics(Set<Topic> topics) {
        this.topics = topics;
    }

    public Set<PCMember> getPcMembers() {
        return pcMembers;
    }

    public void setPcMembers(Set<PCMember> pcMembers) {
        this.pcMembers = pcMembers;
    }


    public Integer getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(Integer isAccepted) {
        this.isAccepted = isAccepted;
    }

    public Integer getIsDiscussed() {
        return isDiscussed;
    }

    public void setIsDiscussed(Integer isDiscussed) {
        this.isDiscussed = isDiscussed;
    }

    public Integer getNumberToBeConfirmed() {
        return numberToBeConfirmed;
    }

    public void setNumberToBeConfirmed(Integer numberToBeConfirmed) {
        this.numberToBeConfirmed = numberToBeConfirmed;
    }

    public boolean canBeReleased(){
        return ((isDiscussed >= 0) && (numberToBeConfirmed <= 0));
    }

    public Integer getHowManyPeopleHaveReviewed() {
        return HowManyPeopleHaveReviewed;
    }

    public void setHowManyPeopleHaveReviewed(Integer howManyPeopleHaveReviewed) {
        HowManyPeopleHaveReviewed = howManyPeopleHaveReviewed;
    }

    public Integer getCanPost() {
        return canPost;
    }

    public void setCanPost(Integer canPost) {
        this.canPost = canPost;
    }
}
