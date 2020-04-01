package cn.nhmt.blog.dao;

import cn.nhmt.blog.domain.Article;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

public interface ArticleDao {
    /**
     * aid int primary key AUTO_INCREMENT COMMENT '文章主键',
     * title varchar(64) NOT NULL COMMENT '题目',
     * establishtime DATETIME NOT NULL COMMENT '创建时间',
     * lastmodifytime DATETIME NOT NULL COMMENT '上次修改时间',
     * introduction varchar(256) NOT NULL COMMENT '简介',
     * content TEXT NOT NULL COMMENT 'HTML文本',
     * markdown TEXT NOT NULL COMMENT 'Markdown文本'
     */

    void insertNewArticle(Article a);

    void deleteArticleByAid(int aid);

    void updateOldArticle(Article a);

    Article selectArticleByAid(int aid);

    List<Article> selectArticleIntroductionByPage(int start, int end);

    int count();

    List<String> selectAllCatalog();

    int selectCatalogCount(String catalog);
}
