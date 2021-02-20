package cn.nhmt.blog.dao;

import cn.nhmt.blog.dto.article.ArticleTitleAndCreateGmt;
import cn.nhmt.blog.po.Article;
import cn.nhmt.blog.dto.article.ArticleCatatoryAndCount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ArticleDao {

    /**
     * article_Id int unsigned primary key NOT NULL AUTO_INCREMENT COMMENT '文章id',
     * article_Title varchar(128) NOT NULL COMMENT '题目',
     * article_Catatory varchar(64) NOT NULL DEFAULT '杂项' COMMENT '分类',
     * article_Create_Gmt datetime NOT NULL COMMENT '创建时间',
     * article_Last_Modify_Gmt datetime NOT NULL COMMENT '上次修改时间',
     * article_Introduction varchar(255) NOT NULL COMMENT '简介',
     * article_Markdown longtext NOT NULL COMMENT 'Markdown文本',
     * article_User_Id int unsigned NOT NULL COMMENT '用户id外键',
     * constraint fk_user_id_to_article foreign key (article_User_Id) references blog_user(user_Id) on delete cascade on update cascade,
     * key k_ca_ti_cg_lm_in (article_Catatory, article_Title, article_Create_Gmt, article_Last_Modify_Gmt, article_Introduction(255))
     */

    // insert: 插入一条新文章
    void post(Article a);

    // delete: 删除文章,通过id
    void deleteById(int id);

    // update: 更新文章
    void put(Article a);

    // select: 获取一页文章的介绍
    List<Article> getPage(@Param("start") int start, @Param("limit") int limit, @Param("catatory") String catatory);

    // select: 获取文章，通过id
    Article getById(int id);

    // for cache
    // select: 获取所有的title和createGmt
    List<ArticleTitleAndCreateGmt> getTitleAndCreateGmt();

    // select: 获取所有的类型及数量
    List<ArticleCatatoryAndCount> getCatatoryAndCount();

    // for article-cover-image
    // update: 跟随article创建时创建的封面
    void postImage(@Param("id") int id, @Param("imageUrl") String imageUrl);

    // update: 跟随article更新时增加的封面
    void putImage(@Param("id") int id, @Param("imageUrl") String imageUrl);

    // udpate: 跟随article更新时置空封面
    void deleteImage(int id);
}
