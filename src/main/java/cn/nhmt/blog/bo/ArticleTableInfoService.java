package cn.nhmt.blog.bo;

import cn.nhmt.blog.dto.Message;

import java.util.Map;

/**
 * @Description: 获取article表信息
 * @Date: 2020-05-19 22:21
 * @Author: PinJyu
 * @Version: 1.0
 **/
public interface ArticleTableInfoService {

    int getCount();

    int getCatatoryCount(String catatory);

    Message doGetCatatory();

    Message doGetArchive();

}
