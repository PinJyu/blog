package cn.nhmt.blog.bo.impl;

import cn.nhmt.blog.bo.ArticleTableInfoService;
import cn.nhmt.blog.bo.Transfer;
import cn.nhmt.blog.dao.ArticleDao;
import cn.nhmt.blog.dto.Message;
import cn.nhmt.blog.dto.article.ArticleCatatoryAndCount;
import cn.nhmt.blog.po.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: article table info cache
 * @Date: 2020-05-19 22:27
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Service
@CacheConfig(cacheNames = "article")
public class ArticleTableInfoServiceImpl implements ArticleTableInfoService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private Transfer transfer;

    private Set<String> ignoreSet;

    @PostConstruct
    public void init() {
        ignoreSet = new HashSet<>();
        ignoreSet.add("catatory");
        ignoreSet.add("lastModifyGmt");
        ignoreSet.add("introduction");
        ignoreSet.add("markdown");
        ignoreSet.add("image_url");
        ignoreSet.add("userId");
    }

    @Cacheable(keyGenerator = "keyGenerator")
    @Transactional(readOnly = true)
    @Override
    public int getCount() {
        return articleDao.retrieveCount();
    }

    @Cacheable(keyGenerator = "keyGenerator")
    @Transactional(readOnly = true)
    @Override
    public int getCatatoryCount(String catatory) {
        Integer count = null;
        List<ArticleCatatoryAndCount> acacs = articleDao.retrieveCatatoryCount();
        if (!ObjectUtils.isEmpty(acacs)) {
            for (ArticleCatatoryAndCount acac : acacs) {
                if (acac.getCatatory() != null && acac.getCatatory().equals(catatory)) {
                    count = acac.getCount();
                }
            }
        }

        return count == null ? 0 : count;
    }

    @Cacheable(keyGenerator = "keyGenerator")
    @Transactional(readOnly = true)
    @Override
    public Message doGetCatatory() {
        List<ArticleCatatoryAndCount> acacs = articleDao.retrieveCatatoryCount();
        Message message = new Message(false);
        if (!ObjectUtils.isEmpty(acacs)) {
            message.setOk(true);
            message.put("catatories", acacs.stream()
                    .collect(Collectors.toMap(ArticleCatatoryAndCount::getCatatory, ArticleCatatoryAndCount::getCount)));
        }

        return message;
    }

    @Cacheable(keyGenerator = "keyGenerator")
    @Transactional(readOnly = true)
    @Override
    public Message doGetArchive() {
        List<Article> articles = articleDao.retrieveAll(null, null, null, null, "article_id", false, null, ignoreSet, null);
        Message message = new Message( false);
        if (!ObjectUtils.isEmpty(articles)) {
            message.setOk(true);
            message.put("archive", articles.stream()
                    .map(transfer::articleToArticleTitleAndCreateGmt).collect(Collectors.toList()));
        }

        return message;
    }

}
