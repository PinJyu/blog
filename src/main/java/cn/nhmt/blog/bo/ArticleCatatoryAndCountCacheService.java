package cn.nhmt.blog.bo;

import cn.nhmt.blog.bo.service.CacheService;
import cn.nhmt.blog.dao.ArticleDao;
import cn.nhmt.blog.dto.article.ArticleCatatoryAndCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: 缓存所有文章分类及分类的数量
 * @Date: 2020-04-19 16:27
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Service
public class ArticleCatatoryAndCountCacheService implements CacheService<String, ArticleCatatoryAndCount> {

    @Autowired
    private ArticleDao articleDao;

    private Map<String, ArticleCatatoryAndCount> articleCatatoryAndCountMap = new HashMap<>(0);

    private AtomicInteger count = new AtomicInteger(-1);

    @Override
    public int getTotalCount() {
        final AtomicInteger countToUse = count;
        if (countToUse.get() == -1) {
            articleCatatoryAndCountMap.values().stream()
                    .map(ArticleCatatoryAndCount::getCount).reduce(Integer::sum)
                    .ifPresent(sum -> countToUse.compareAndSet(-1, sum));
        }
        return countToUse.get();
    }

    @Override
    public Map<String, ArticleCatatoryAndCount> getTotalMap() {
        return new HashMap<>(articleCatatoryAndCountMap);
    }

    @Override
    public ArticleCatatoryAndCount getCache(String u) {
        return articleCatatoryAndCountMap.get(u);
    }

    @Transactional(readOnly = true)
    public synchronized void doInit() {
        List<ArticleCatatoryAndCount> cacl = null;
        try {
            cacl= articleDao.getCatatoryAndCount();
        } catch (Exception ignore) { }

        if (cacl != null && cacl.size() != 0) {
            Map<String, ArticleCatatoryAndCount> tempMap = new HashMap<>();
            cacl.forEach(ac -> tempMap.put(ac.getCatatory(), ac));
            articleCatatoryAndCountMap = tempMap;
            count = new AtomicInteger(-1);
        }
    }
}
