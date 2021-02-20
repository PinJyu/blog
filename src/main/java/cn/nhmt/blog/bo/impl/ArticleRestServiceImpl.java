package cn.nhmt.blog.bo.impl;

import cn.nhmt.blog.bo.ArticleTableInfoService;
import cn.nhmt.blog.bo.RestService;
import cn.nhmt.blog.dao.ArticleDao;
import cn.nhmt.blog.po.Article;
import cn.nhmt.blog.dto.Message;
import cn.nhmt.blog.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @Description: 处理Article资源的增删改查
 * @Date: 2020-04-03 17:59
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Service
@CacheConfig(cacheNames = "article")
public class ArticleRestServiceImpl implements RestService<Article> {

    private static final int MAX_INTRODUCTION_BYTE_LENGTH = 500;

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private ArticleTableInfoService tableInfoService;

    @Cacheable(keyGenerator = "keyGenerator")
    @Transactional(readOnly = true)
    @Override
    public Message doGet(
            Integer pageNumber, Integer perPage, Integer offset, Integer limit, String sortBy
            , Boolean isAsc, String key, Set<String> ignoreSet, Set<String> foreignSet
    ) {
        Message message = new Message(false);
        List<Article> articles = articleDao.retrieveAll(
                pageNumber, perPage, offset, limit, sortBy, isAsc , key, ignoreSet, foreignSet
        );

        if (!ObjectUtils.isEmpty(articles)) {
            message.setOk(true);
            int total = key == null ? tableInfoService.getCount() : tableInfoService.getCatatoryCount(key);
            message.put("articleList", articles).put("catatory", key).put("total" , total);
        }

        return message;
    }

    @Cacheable(keyGenerator = "keyGenerator")
    @Transactional(readOnly = true)
    @Override
    public Message doGet(int id, Set<String> ignoreSet, Set<String> foreignSet) {
        Message message = new Message(false);
        Article article = articleDao.retrieveById(id, ignoreSet, foreignSet);
        if (Objects.nonNull(article)) {
            message.setOk(true);
            message.put("article", article);
        }

        return message;
    }

    @CacheEvict(allEntries = true)
    @Transactional
    @Override
    public Message doPost(Article article, User user) {
        Message message = new Message(true);
        article.setUserId(user.getId());
        article.setCreateGmt(new Date());
        article.setLastModifyGmt(article.getCreateGmt());
        article.setIntroduction(generateIntroduciton(article.getMarkdown()));
        articleDao.create(article);

        return message.put("id", article.getId());
    }

    @CacheEvict(allEntries = true)
    @Transactional
    @Override
    public Message doPut(Article article, User user) {
        Message message = new Message(true);
        article.setUserId(user.getId());
        article.setLastModifyGmt(new Date());
        article.setIntroduction(generateIntroduciton(article.getMarkdown()));
        articleDao.update(article);

        return message.put("id", article.getId());
    }

    @CacheEvict(allEntries = true)
    @Transactional
    @Override
    public Message doDelete(int key, User user) {
        Message message = new Message(true);
        Article article = articleDao.retrieveById(key, null, null);
        if (article == null) {
            return message;
        }
        articleDao.delete(key, user.getId());
        message.setOk(true);
        message.put("article", article);
        message.put("user", user);

        return message;
    }

    private String generateIntroduciton(String markdown) {
        markdown = markdown.replaceAll("[\n\t\r*#+->_]", "").trim();
        int len;
        char[] chars = markdown.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0, j = 0; i < MAX_INTRODUCTION_BYTE_LENGTH; ++j) {
            if (j >= chars.length) break;
            len = String.valueOf(chars[j]).getBytes().length;
            sb.append(chars[j]);
            len = Math.min(len, 2);
            i += len;
        }
        return sb.toString();
    }

}
