package cn.nhmt.blog.vo;


import cn.nhmt.blog.dto.OkMessage;
import cn.nhmt.blog.dto.article.ArticleCatatoryAndCount;

import java.util.List;

/**
 * @Description: TODO
 * @Date: 2020-04-07 21:25
 * @Author: PinJyu
 * @Version: 1.0
 **/
public class CatatoryViewMessage extends OkMessage {

    private List<ArticleCatatoryAndCount> catatorys;

    public CatatoryViewMessage(boolean ok) {
        super(ok);
    }

    public CatatoryViewMessage() {
    }

    public List<ArticleCatatoryAndCount> getCatatorys() {
        return catatorys;
    }

    public void setCatatorys(List<ArticleCatatoryAndCount> catatorys) {
        this.catatorys = catatorys;
    }

    @Override
    public CatatoryViewMessage setErrorCode(int error) {
        return (CatatoryViewMessage) super.setErrorCode(error);
    }
}
