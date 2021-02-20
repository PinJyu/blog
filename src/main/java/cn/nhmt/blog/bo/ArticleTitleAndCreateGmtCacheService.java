package cn.nhmt.blog.bo;

import cn.nhmt.blog.bo.service.CacheService;
import cn.nhmt.blog.dao.ArticleDao;
import cn.nhmt.blog.dto.article.ArticleTitleAndCreateGmt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @Date: 2020-04-19 19:01
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Service
public class ArticleTitleAndCreateGmtCacheService implements CacheService<Integer, List<ArticleTitleAndCreateGmt>> {

    @Autowired
    private ArticleDao articleDao;

    private Map<Integer, List<ArticleTitleAndCreateGmt>> articleCreateYearAndDetailListMap = new LinkedHashMap<>(0);

    @Override
    public int getTotalCount() {
        return articleCreateYearAndDetailListMap.size();
    }

    @Override
    public Map<Integer, List<ArticleTitleAndCreateGmt>> getTotalMap() {
        return new HashMap<>(articleCreateYearAndDetailListMap);
    }

    @Override
    public List<ArticleTitleAndCreateGmt> getCache(Integer u) {
        return articleCreateYearAndDetailListMap.get(u);
    }

    @Override
    public synchronized void doInit() {
        List<ArticleTitleAndCreateGmt> tacg = null;
        try {
            tacg= articleDao.getTitleAndCreateGmt();
        } catch (Exception ignore) { }

        if (tacg != null && tacg.size() != 0) {
            Calendar cal = Calendar.getInstance();
            Map<Integer, List<ArticleTitleAndCreateGmt>> tempMap = new HashMap<>();

            tacg.stream().collect(Collectors.groupingBy(tc -> {
                cal.setTime(tc.getCreateGmt());
                return cal.get(Calendar.YEAR);
            })).forEach((k, v) -> {
                tempMap.put(k, v);
                v.sort(Comparator.comparing(ArticleTitleAndCreateGmt::getCreateGmt));
                Collections.reverse(v);
            });

            ArrayList<Integer> years = new ArrayList<>(tempMap.keySet());
            years.sort(Collections.reverseOrder());
            LinkedHashMap<Integer, List<ArticleTitleAndCreateGmt>> tempLinkedMap = new LinkedHashMap<>();
            for (Integer year : years) {
                tempLinkedMap.put(year, tempMap.get(year));
            }
            articleCreateYearAndDetailListMap = tempLinkedMap;
        }
    }
}
