package cn.nhmt.blog.ao.util;

import cn.nhmt.blog.ao.SearchResult;
import cn.nhmt.blog.dto.article.ArticleDetail;
import cn.nhmt.blog.po.Article;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @Date: 2020-04-20 15:30
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Component
public class Sr2ArticleDetail implements SearchResult2Dto<Article, ArticleDetail> {

    @Override
    public List<ArticleDetail> transfer(SearchResult<Article> sr) {
        List<ArticleDetail> transferResult = new ArrayList<>();
        for (Article article : sr.getResult()) {
            ArticleDetail dto = new ArticleDetail();
            transferResult.add(dto);
            dto.setId(article.getId());
            dto.setTitle(article.getTitle());
            dto.setCatatory(article.getCatatory());
            dto.setMarkdown(article.getMarkdown());
            dto.setCreateGmt(article.getCreateGmt());
            dto.setLastModifyGmt(article.getLastModifyGmt());
            dto.setUserName(article.getUser().getName());
        }
        return transferResult;
    }

}
