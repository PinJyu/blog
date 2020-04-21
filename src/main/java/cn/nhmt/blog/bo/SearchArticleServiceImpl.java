package cn.nhmt.blog.bo;

import cn.nhmt.blog.ao.SearchResult;
import cn.nhmt.blog.bo.service.CacheService;
import cn.nhmt.blog.bo.service.SearchService;
import cn.nhmt.blog.dao.ArticleDao;
import cn.nhmt.blog.dto.article.ArticleCatatoryAndCount;
import cn.nhmt.blog.po.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Description: TODO
 * @Date: 2020-04-03 02:55
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Service
public class SearchArticleServiceImpl implements SearchService<Article> {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private CacheService<String, ArticleCatatoryAndCount> sacs;

    private static final int PAGESIZE = 10;
    private static final String ALL = "all";
    private static final String DETAIL = "detail";

    @Override
    @Transactional(readOnly = true)
    public SearchResult<Article> search(String query, int number) {
        SearchResult<Article> asr;
        ArticleCatatoryAndCount acac;
        if (query.equals(ALL)) {
            int totalCount = sacs.getTotalCount();
            asr = doSearch2PageArticleIntroduction(query, number, totalCount);
        } else if (query.equals(DETAIL)) {
            asr = doSearch2ArticleDetail(number);
        } else if ((acac = sacs.getTotalMap().get(query)) != null) {
            asr = doSearch2PageArticleIntroduction(query, number, acac.getCount());
        } else {
            asr = new SearchResult<>();
        }
        List<Article> result;
        if (asr.isSuccess() && (result = asr.getResult()).size() != 1)
            Collections.reverse(result);
        return asr;
    }

    private SearchResult<Article> doSearch2ArticleDetail(int id) {
        SearchResult<Article> asr = new SearchResult<>();
        asr.setResult(Collections.singletonList(articleDao.getById(id)));
        asr.setKey(DETAIL);
        asr.setHasPage(false);
        return asr;
    }

    private SearchResult<Article> doSearch2PageArticleIntroduction(String catatory, int page, int count) {
        int tpc;
        SearchResult<Article> asr = new SearchResult<>();
        if ((tpc = vaildPage(page, count)) == 0)
            return asr;
        asr.setResult(getPage(catatory.equals(ALL) ? null : catatory, page, count));
        asr.setKey(catatory);
        asr.setHasPage(true);
        asr.setCurrentPage(page);
        asr.setTotalPageCount(tpc);
        return asr;
    }

    private List<Article> getPage(String catatory, int page, int count) {
        int start = count - page * PAGESIZE;
        int end = count - (page - 1) * PAGESIZE;
        if (end <= 0)
            return null;
        if (start < 0)
            start = 0;
        return articleDao.getPage(start, end - start, catatory);
    }

    private int vaildPage(int page, int count) {
        int tpc;
        return (0 < page && page <= (tpc = count % PAGESIZE == 0 ? count / PAGESIZE : count / PAGESIZE + 1))
                ? tpc : 0;
    }

}

