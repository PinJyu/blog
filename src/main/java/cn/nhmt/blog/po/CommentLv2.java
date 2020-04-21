package cn.nhmt.blog.po;

import java.io.Serializable;
import java.util.Date;

public class CommentLv2 implements Serializable {

    private int id;
    private String Content;
    private Date createGmt;
    private int toLv2UserId;
    private int commentLv1Id;

    public CommentLv2() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Date getCreateGmt() {
        return createGmt;
    }

    public void setCreateGmt(Date createGmt) {
        this.createGmt = createGmt;
    }

    public int getToLv2UserId() {
        return toLv2UserId;
    }

    public void setToLv2UserId(int toLv2UserId) {
        this.toLv2UserId = toLv2UserId;
    }

    public int getCommentLv1Id() {
        return commentLv1Id;
    }

    public void setCommentLv1Id(int commentLv1Id) {
        this.commentLv1Id = commentLv1Id;
    }

    @Override
    public String toString() {
        return "CommentLv2{" +
                "id=" + id +
                ", Content='" + Content + '\'' +
                ", createGmt=" + createGmt +
                ", toLv2UserId=" + toLv2UserId +
                ", commentLv1Id=" + commentLv1Id +
                '}';
    }
}
