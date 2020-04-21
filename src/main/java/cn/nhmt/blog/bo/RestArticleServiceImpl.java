package cn.nhmt.blog.bo;

import cn.nhmt.blog.ao.SearchResult;
import cn.nhmt.blog.ao.util.SearchResult2Dto;
import cn.nhmt.blog.bo.exception.ErrorCode;
import cn.nhmt.blog.bo.exception.RestArticleException;
import cn.nhmt.blog.bo.service.ArticleService;
import cn.nhmt.blog.bo.service.CacheService;
import cn.nhmt.blog.bo.service.SearchService;
import cn.nhmt.blog.dao.ArticleDao;
import cn.nhmt.blog.dto.article.ArticleCatatoryAndCount;
import cn.nhmt.blog.dto.article.ArticleDetail;
import cn.nhmt.blog.dto.article.ArticleIntro;
import cn.nhmt.blog.dto.article.ArticleTitleAndCreateGmt;
import cn.nhmt.blog.po.Article;
import cn.nhmt.blog.dto.OkMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Description: 处理Article资源的增删改查
 * @Date: 2020-04-03 17:59
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Service
public class RestArticleServiceImpl implements ArticleService.Rest {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private SearchService<Article> articleSearchService;

    @Autowired
    private SearchResult2Dto<Article, ArticleIntro> asr2ai;

    @Autowired
    private SearchResult2Dto<Article, ArticleDetail> asr2ad;

    @Autowired
    private CacheService<String, ArticleCatatoryAndCount> catatoryAndCountCacheService;

    @Autowired
    private CacheService<Integer, List<ArticleTitleAndCreateGmt>> titleAndCreateGmtCacheService;

    private static final int MIN_INTRODUCTION_BYTE_LENGTH = 500;

    @Override
    public OkMessage handleGetMethod(String catatory, int number) {
        SearchResult<Article> asr;
        try {
            asr = articleSearchService.search(catatory, number);
        } catch (RuntimeException e) {
            throw RestArticleException.forGet(ErrorCode.SEARCH_FAIL, e);
        }
        OkMessage om = new OkMessage(false);
        if (!asr.isSuccess()) {
            om.setErrorCode(ErrorCode.SEARCH_RESULT_NOT_EXISTS);
            return om;
        }
        om.setOk(true);
        if (!asr.isHasPage()) {
            return om.put("article", asr2ad.transfer(asr).get(0));
        }
        return om.put("introList", asr2ai.transfer(asr)).put("currentPage", asr.getCurrentPage())
                .put("currentCatatory", asr.getKey()).put("totalPageCount", asr.getTotalPageCount());
    }

    @Override
    @Transactional
    public OkMessage handlePostMethod(Article a) {
        OkMessage om = new OkMessage(false);
        if (validFieldNotEmpty(a)) {
            om.setErrorCode(ErrorCode.CREATE_FAIL_FIELD_NULL);
            return om;
        }
        try {
            a.setCreateGmt(new Date());
            a.setLastModifyGmt(a.getCreateGmt());
            a.setIntroduction(generateIntroduciton(a.getMarkdown()));
            articleDao.post(a);
        } catch (RuntimeException e) {
           throw RestArticleException.forPost(ErrorCode.CREATE_FAIL, e);
        }
        catatoryAndCountCacheService.pulishBuildCacheEvent();
        titleAndCreateGmtCacheService.pulishBuildCacheEvent();

        om.setOk(true);
        return om;
    }

    @Override
    @Transactional
    public OkMessage handlePutMethod(Article a) {
        OkMessage om = new OkMessage(false);
        if (validFieldNotEmpty(a)) {
            om.setErrorCode(ErrorCode.UPDATE_FAIL_FIELD_NULL);
            return om;
        }
        try {
            a.setLastModifyGmt(new Date());
            a.setIntroduction(generateIntroduciton(a.getMarkdown()));
            articleDao.put(a);
        } catch (RuntimeException e) {
            throw RestArticleException.forPut(ErrorCode.UPDATE_FAIL, e);
        }
        catatoryAndCountCacheService.pulishBuildCacheEvent();
        titleAndCreateGmtCacheService.pulishBuildCacheEvent();

        om.setOk(true);
        return om;
    }

    @Override
    @Transactional
    public OkMessage handleDeleteMethod(int id) {
        OkMessage om = new OkMessage(false);
        try {
            articleDao.deleteById(id);
        } catch (RuntimeException e) {
            throw RestArticleException.forDelete(ErrorCode.DELETE_FAIL, e);
        }
        catatoryAndCountCacheService.pulishBuildCacheEvent();
        titleAndCreateGmtCacheService.pulishBuildCacheEvent();

        om.setOk(true);
        return om;
    }

    private String generateIntroduciton(String markdown) {
        markdown = markdown.replaceAll("[\n\t\r*#+->_]", "").trim();
        int len;
        char[] chars = markdown.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0, j = 0; i < MIN_INTRODUCTION_BYTE_LENGTH; ++j) {
            if (j >= chars.length) break;
            len = String.valueOf(chars[j]).getBytes().length;
            sb.append(chars[j]);
            len = Math.min(len, 2);
            i += len;
        }
        return sb.toString();
    }

    private boolean validFieldNotEmpty(Article a) {
        return !(a.getTitle().equals("") || a.getMarkdown().equals("")
                || a.getCatatory().equals("") || (a.getImageUrl() != null && a.getImageUrl().equals("")));
    }

}
