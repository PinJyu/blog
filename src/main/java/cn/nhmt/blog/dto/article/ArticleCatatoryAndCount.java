package cn.nhmt.blog.dto.article;

import java.io.Serializable;

public class ArticleCatatoryAndCount implements Serializable {

    private String catatory;
    private int count;

    public ArticleCatatoryAndCount() {
    }

    public String getCatatory() {
        return catatory;
    }

    public void setCatatory(String catatory) {
        this.catatory = catatory;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ArticleCatatory{" +
                "catatory='" + catatory + '\'' +
                ", count=" + count +
                '}';
    }
}
