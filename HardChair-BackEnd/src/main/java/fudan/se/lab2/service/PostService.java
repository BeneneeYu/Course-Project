package fudan.se.lab2.service;

import fudan.se.lab2.domain.Article;
import fudan.se.lab2.domain.Post;
import fudan.se.lab2.domain.Reply;
import fudan.se.lab2.domain.User;
import fudan.se.lab2.repository.ArticleRepository;
import fudan.se.lab2.repository.PostRepository;
import fudan.se.lab2.repository.ReplyRepository;
import fudan.se.lab2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-28 16:15
 **/
@Service
public class PostService {
    private PostRepository postRepository;
    private ReplyRepository replyRepository;
    private ArticleRepository articleRepository;
    private UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository,ReplyRepository replyRepository,ArticleRepository articleRepository,UserRepository userRepository){
        this.postRepository=postRepository;
        this.replyRepository=replyRepository;
        this.articleRepository=articleRepository;
        this.userRepository=userRepository;
    }

    public Post browsePostsOnArticle(Long articleID){
        return postRepository.findByArticleID(articleID);
    }

    public Long postOnArticle(Long articleID,Long ownerID,String words) {
        if (null != postRepository.findByArticleID(articleID)){
            return (long)(-255);
        }
        Post post = new Post(ownerID, articleID, words);
        Article article = articleRepository.findById(articleID).orElse(null);
        if(article == null){
            return (long)(-1);
        }
        post.setArticleTitle(article.getTitle());
        User user=userRepository.findById(ownerID).orElse(null);
        if(user==null){
            return (long)(-1);
        }
        String ownerFullName = userRepository.findById(ownerID).orElse(null) == null ? "系统":user.getFullName();
        post.setOwnerFullName(ownerFullName);

        postRepository.save(post);
        if (null == postRepository.findByArticleID(articleID)) {
            return (long) (-1);
        } else {
            article.setIsDiscussed(article.getIsDiscussed() + 1);
            article.setCanPost(-1);
            articleRepository.save(article);
            return postRepository.findByArticleID(articleID).getId();
        }
    }

    public Reply replyPost(Long postID,Long ownerID,String words,Long floorNumber){
        Post post = postRepository.findById(postID).orElse(null);
        if(post==null){
            return null;
        }
        Reply reply = saveReply(words, ownerID, post, floorNumber);
        Article article = articleRepository.findById(post.getArticleID()).orElse(null);
        if (article != null) {
            article.setIsDiscussed(article.getIsDiscussed() + 1);
            articleRepository.save(article);
        }
        return reply;
    }

    //先检查能否rebuttal
    //再将文章设置为必须被讨论
    public Reply submitRebuttal(Long articleID,String words,Long authorID){
        Article article = articleRepository.findById(articleID).orElse(null);
        if(article==null){
            return null;
        }
        if (article.getIsAccepted() != -1 || article.getTimesLeftForRebuttal() <= 0){
            return null;
        }
        Post post = postRepository.findByArticleID(articleID);


        if(null == post){
            Long id = postOnArticle(articleID,(long)1001,"由于作者提交了Rebuttal，系统自动发起讨论");
            article.setIsDiscussed(-1);
            post = postRepository.findById(id).orElse(null);
        }
        Reply reply = saveReply(words, authorID, post, (long) (-1));
        article.setTimesLeftForRebuttal(0);
        article.setNumberToBeConfirmed(article.getNumberToBeConfirmed() + article.getHowManyPeopleHaveReviewed());
        article.setIsDiscussed(-1);
        articleRepository.save(article);
        return reply;
    }

    private Reply saveReply(String words, Long authorID, Post post, long l) {
        Reply reply = new Reply(authorID, words, (post.getReplyNumber() + 2));
        reply.setReplyToFloorNumber(l);
        User user=userRepository.findById(authorID).orElse(null);
        if(user==null){
            return null;
        }
        reply.setOwnerFullName(user.getFullName());
        replyRepository.save(reply);
        post.getReplyList().add(reply);
        post.setReplyNumber(post.getReplyNumber() + 1);
        postRepository.save(post);
        return reply;
    }

}
