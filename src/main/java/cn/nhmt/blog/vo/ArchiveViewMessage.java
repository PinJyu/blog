package cn.nhmt.blog.vo;

import cn.nhmt.blog.dto.OkMessage;
import cn.nhmt.blog.dto.article.ArticleTitleAndCreateGmt;

import java.util.List;

/**
 * @Description: TODO
 * @Date: 2020-04-11 02:58
 * @Author: PinJyu
 * @Version: 1.0
 **/
public class ArchiveViewMessage extends OkMessage {

    private List<ArticleTitleAndCreateGmt> archiveList;

    public List<ArticleTitleAndCreateGmt> getArchiveList() {
        return archiveList;
    }

    public void setArchiveList(List<ArticleTitleAndCreateGmt> archiveList) {
        this.archiveList = archiveList;
    }
}
