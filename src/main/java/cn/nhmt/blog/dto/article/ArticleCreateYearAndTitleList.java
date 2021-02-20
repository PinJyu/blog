package cn.nhmt.blog.dto.article;

import java.util.List;

/**
 * @Description: TODO
 * @Date: 2020-04-20 22:26
 * @Author: PinJyu
 * @Version: 1.0
 **/
public class ArticleCreateYearAndTitleList {

    private int year;
    private List<ArticleTitleAndCreateGmt> titleList;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<ArticleTitleAndCreateGmt> getTitleList() {
        return titleList;
    }

    public void setTitleList(List<ArticleTitleAndCreateGmt> titleList) {
        this.titleList = titleList;
    }
}
