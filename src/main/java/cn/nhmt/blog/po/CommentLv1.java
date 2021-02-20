package cn.nhmt.blog.po;

import java.io.Serializable;
import java.util.Date;

public class CommentLv1 implements Serializable {

    private int id;
    private String content;
    private Date createGmt;
    private int articleId;
    private int userId;

    public CommentLv1() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateGmt() {
        return createGmt;
    }

    public void setCreateGmt(Date createGmt) {
        this.createGmt = createGmt;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "CommentLv1{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createGmt=" + createGmt +
                ", articleId=" + articleId +
                ", userId=" + userId +
                '}';
    }
}
