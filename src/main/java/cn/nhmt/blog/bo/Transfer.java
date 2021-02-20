package cn.nhmt.blog.bo;

import cn.nhmt.blog.dto.article.ArticleNoIntroduction;
import cn.nhmt.blog.dto.article.ArticleNoMarkdown;
import cn.nhmt.blog.dto.article.ArticleTitleAndCreateGmt;
import cn.nhmt.blog.po.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @Description: po2dto
 * @Date: 2020-05-18 22:34
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Mapper(componentModel = "spring")
public interface Transfer {

    @Mapping(target = "userName", source = "article.user.name")
    ArticleNoIntroduction articleToArticle1(Article article);

    @Mapping(target = "userName", source = "article.user.name")
    ArticleNoMarkdown articleToArticle2(Article article);

    ArticleTitleAndCreateGmt articleToArticleTitleAndCreateGmt(Article article);

    List<ArticleNoMarkdown> articlesToArticle2s(List<Article> articles);
}
