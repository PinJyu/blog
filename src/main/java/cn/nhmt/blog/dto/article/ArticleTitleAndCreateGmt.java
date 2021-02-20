package cn.nhmt.blog.dto.article;

import cn.nhmt.blog.po.Article;

import java.util.Date;
import java.util.List;

/**
 * @Description: TODO
 * @Date: 2020-04-11 01:19
 * @Author: PinJyu
 * @Version: 1.0
 **/
public class ArticleTitleAndCreateGmt {

    private int id;
    private String title;
    private Date createGmt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateGmt() {
        return createGmt;
    }

    public void setCreateGmt(Date createGmt) {
        this.createGmt = createGmt;
    }

    @Override
    public String toString() {
        return "ArticleTitleAndCreateGmt{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", createGmt=" + createGmt +
                '}';
    }
}

