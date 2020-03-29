package cn.nhmt.blog.controller;

import cn.nhmt.blog.dao.ArticleDao;
import cn.nhmt.blog.domain.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
public class First {

    @Autowired
    private ArticleDao articleDao;

    @RequestMapping("/success")
    String success() {
        Article article = new Article();
        article.setContent("content");
        article.setEstablishtime(new Date());
        article.setLastmodifytime(new Date());
        article.setIntroduction("introduction");
        article.setMarkdown("markdown");
        article.setTitle("title");
        articleDao.insertNewArticle(article);
        Article article1 = articleDao.selectArticleByAid(1);
        System.out.println(article1);

        article1.setLastmodifytime(new Date());
        articleDao.updateOldArticle(article1);
        Article article2 = articleDao.selectArticleByAid(1);
        System.out.println(article2);

        articleDao.deleteArticleByAid(1);
        System.out.println(articleDao.selectArticleByAid(1));
        return "success";
    }


}
