package cn.nhmt.blog.controller;

import cn.nhmt.blog.dao.ArticleDao;
import cn.nhmt.blog.domain.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/")
public class RootController {

    @GetMapping({"/home", "/"})
    String index() {
        return "index";
    }

    @GetMapping("/about")
    String about() {
        return "about";
    }

    @Autowired
    private ArticleDao articleDao;

    @PostMapping("/about/send")
    String send(Article a) {
        a.setContent(a.getMarkdown());
        a.setEstablishtime(new Date());
        a.setLastmodifytime(a.getEstablishtime());
        String m;
        if ((m = a.getMarkdown()).length() > 266)
            m = m.substring(0, 255);
        a.setIntroduction(m.replaceAll("\n", "").trim());
        articleDao.insertNewArticle(a);
        System.out.println(a.getCatalog());
        System.out.println(m);
        return "redirect:/about";
    }

    @GetMapping("/article/page/{page}")
    @ResponseBody
    Map get(@PathVariable(value = "page", required = false) Integer page) {
        if (page == null || page <= 0)
            page = 1;
        int count, pageSize = 10, start, end, pageCount;
        count = articleDao.count();
        start = count - page * pageSize;
        end = count - (page - 1) * pageSize;
        if (end < 0)
            return null;
        if (start < 0)
            start = 0;
        List<Article> articles = articleDao.selectArticleIntroductionByPage(start, end - start);
        Collections.reverse(articles);
        System.out.println("success");
        List<String> strings = articleDao.selectAllCatalog();
        HashMap hashMap1 = new HashMap();
        for (String x : strings) {
            hashMap1.put(x,articleDao.selectCatalogCount(x));
        }
        HashMap hashMap = new HashMap();
        hashMap.put("introductions", articles);
        hashMap.put("catalogs", hashMap1);

        pageCount = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;

        hashMap.put("pageCount", pageCount);
        hashMap.put("currentPage", page);
        return hashMap;
    }
}
