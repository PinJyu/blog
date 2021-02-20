package cn.nhmt.blog.dto.article;

import cn.nhmt.blog.po.User;

import java.util.Date;

/**
 * @Description: TODO
 * @Date: 2020-04-20 16:48
 * @Author: PinJyu
 * @Version: 1.0
 **/
public class ArticleIntro {

    private int id;
    private String title;
    private String catatory;
    private Date createGmt;
    private Date lastModifyGmt;
    private String introduction;
    private String imageUrl;
    private String userName;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "ArticleIntro{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", catatory='" + catatory + '\'' +
                ", createGmt=" + createGmt +
                ", lastModifyGmt=" + lastModifyGmt +
                ", introduction='" + introduction + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
