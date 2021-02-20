package cn.nhmt.blog.ao.util;

import cn.nhmt.blog.ao.SearchResult;
import cn.nhmt.blog.dto.article.ArticleIntro;
import cn.nhmt.blog.po.Article;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @Date: 2020-04-20 16:52
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Component
public class Sr2ArticleIntro implements SearchResult2Dto<Article, ArticleIntro>  {

    @Override
    public List<ArticleIntro> transfer(SearchResult<Article> sr) {
        List<ArticleIntro> transferResult = new ArrayList<>();
        for (Article article : sr.getResult()) {
            ArticleIntro dto = new ArticleIntro();
            transferResult.add(dto);
            dto.setId(article.getId());
            dto.setTitle(article.getTitle());
            dto.setCatatory(article.getCatatory());
            dto.setIntroduction(article.getIntroduction());
            dto.setCreateGmt(article.getCreateGmt());
            dto.setLastModifyGmt(article.getLastModifyGmt());
            dto.setImageUrl(article.getImageUrl());
            dto.setUserName(article.getUser().getName());
        }
        return transferResult;
    }
}
