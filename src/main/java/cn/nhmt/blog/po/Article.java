package cn.nhmt.blog.po;

import java.io.Serializable;
import java.util.Date;

public class Article implements Serializable {

    private int id;
    private String title;
    private String catatory;
    private Date createGmt;
    private Date lastModifyGmt;
    private String introduction;
    private String markdown;
    private String imageUrl;
    private User user;

    public Article() {
    }

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

    public String getCatatory() {
        return catatory;
    }

    public void setCatatory(String catatory) {
        this.catatory = catatory;
    }

    public Date getCreateGmt() {
        return createGmt;
    }

    public void setCreateGmt(Date createGmt) {
        this.createGmt = createGmt;
    }

    public Date getLastModifyGmt() {
        return lastModifyGmt;
    }

    public void setLastModifyGmt(Date lastModifyGmt) {
        this.lastModifyGmt = lastModifyGmt;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getMarkdown() {
        return markdown;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", catatory='" + catatory + '\'' +
                ", createGmt=" + createGmt +
                ", lastModifyGmt=" + lastModifyGmt +
                ", introduction='" + introduction + '\'' +
                ", markdown='" + markdown + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", user=" + user +
                '}';
    }
}
