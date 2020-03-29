package cn.nhmt.blog.domain;

import java.util.Date;

public class Article {

    /**
     * aid int primary key AUTO_INCREMENT COMMENT '文章主键',
     * title varchar(64) NOT NULL COMMENT '题目',
     * establishtime DATETIME NOT NULL COMMENT '创建时间',
     * lastmodifytime DATETIME NOT NULL COMMENT '上次修改时间',
     * introduction varchar(256) NOT NULL COMMENT '简介',
     * content TEXT NOT NULL COMMENT 'HTML文本',
     * markdown TEXT NOT NULL COMMENT 'Markdown文本'
     */

    private int aid;
    private String title;
    private Date establishtime;
    private Date lastmodifytime;
    private String introduction;
    private String content;
    private String markdown;

    public Article() { }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getEstablishtime() {
        return establishtime;
    }

    public void setEstablishtime(Date establishtime) {
        this.establishtime = establishtime;
    }

    public Date getLastmodifytime() {
        return lastmodifytime;
    }

    public void setLastmodifytime(Date lastmodifytime) {
        this.lastmodifytime = lastmodifytime;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMarkdown() {
        return markdown;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }

    @Override
    public String toString() {
        return "Article{" +
                "aid=" + aid +
                ", title='" + title + '\'' +
                ", establishtime=" + establishtime +
                ", lastmodifytime=" + lastmodifytime +
                ", introduction='" + introduction + '\'' +
                ", content='" + content + '\'' +
                ", markdown='" + markdown + '\'' +
                '}';
    }
}
