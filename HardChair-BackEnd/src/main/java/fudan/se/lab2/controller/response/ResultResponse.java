package fudan.se.lab2.controller.response;

import fudan.se.lab2.domain.Article;
import fudan.se.lab2.domain.Result;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-08 21:15
 **/
public class ResultResponse {
    Article article;
    Result result;

    public ResultResponse(){

    }
    public ResultResponse(Article article, Result result) {
        this.article = article;
        this.result = result;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
