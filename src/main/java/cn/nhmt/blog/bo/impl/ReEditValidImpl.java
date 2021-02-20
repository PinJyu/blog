package cn.nhmt.blog.bo.impl;

import cn.nhmt.blog.bo.ReEditValid;
import cn.nhmt.blog.dao.ArticleDao;
import cn.nhmt.blog.po.Article;
import cn.nhmt.blog.po.User;
import cn.nhmt.blog.po.group.ArticleGroup;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description: TODO
 * @Date: 2020-07-07 15:49
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Service
public class ReEditValidImpl implements ReEditValid {

    @Autowired
    private ArticleDao articleDao;

    private Set<String> ignoreSet;

    @Override
    public boolean isbelongUser(int articleId, User user) {
        if (user.getId() > 0) {
            Article article = articleDao.retrieveById(articleId, ignoreSet, null);
            return article != null && article.getUserId() == user.getId();
        }
        return false;
    }

    @PostConstruct
    public void init() {
        ignoreSet = new HashSet<>();
        ignoreSet.add("title");
        ignoreSet.add("catatory");
        ignoreSet.add("markdown");
        ignoreSet.add("createGmt");
        ignoreSet.add("lastModifyGmt");
        ignoreSet.add("introduction");
        ignoreSet.add("imageUrl");
    }

}
