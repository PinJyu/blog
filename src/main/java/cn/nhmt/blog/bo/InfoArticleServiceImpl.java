package cn.nhmt.blog.bo;

import cn.nhmt.blog.bo.exception.ErrorCode;
import cn.nhmt.blog.bo.service.ArticleService;
import cn.nhmt.blog.bo.service.CacheService;
import cn.nhmt.blog.dto.OkMessage;
import cn.nhmt.blog.dto.article.ArticleCreateYearAndTitleList;
import cn.nhmt.blog.dto.article.ArticleTitleAndCreateGmt;
import cn.nhmt.blog.dto.article.ArticleCatatoryAndCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description: 获取文章分类及分类数量，时间轴
 * @Date: 2020-04-19 15:37
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Service
public class InfoArticleServiceImpl implements ArticleService.Info {

    @Autowired
    private CacheService<String, ArticleCatatoryAndCount> catatoryAndCountCacheService;

    @Autowired
    private CacheService<Integer, List<ArticleTitleAndCreateGmt>> titleAndCreateGmtCahceService;

    public OkMessage handleGetCatatories() {
        List<ArticleCatatoryAndCount> acacl = new ArrayList<>(catatoryAndCountCacheService.getTotalMap().values());
        OkMessage om = new OkMessage(false);
        if (acacl.size() != 0) {
            om.setOk(true);
            om.put("catatoryList", acacl);
            return om;
        }
        om.setErrorCode(ErrorCode.Catatory_NULL);
        return om;
    }

    public OkMessage handleGetArchive() {
        Set<Map.Entry<Integer, List<ArticleTitleAndCreateGmt>>> entrySet = titleAndCreateGmtCahceService.getTotalMap().entrySet();
        OkMessage om = new OkMessage(false);
        if (entrySet.size() != 0) {
            om.setOk(true);
            Iterator<Map.Entry<Integer, List<ArticleTitleAndCreateGmt>>> iterator = entrySet.iterator();
            List<ArticleCreateYearAndTitleList> acyatll = new ArrayList<>();
            while (iterator.hasNext()) {
                Map.Entry<Integer, List<ArticleTitleAndCreateGmt>> next = iterator.next();
                ArticleCreateYearAndTitleList acyatl = new ArticleCreateYearAndTitleList();
                acyatl.setYear(next.getKey());
                acyatl.setTitleList(next.getValue());
                acyatll.add(acyatl);
            }
            om.put("achiveList", acyatll);
            return om;
        }
        om.setErrorCode(ErrorCode.ARTICLE_NULL);
        return om;
    }
}