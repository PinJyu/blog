package cn.nhmt.blog.bo.service;

import cn.nhmt.blog.ao.SearchResult;

import java.util.List;

/**
 * @Description: 搜索服务接口
 * @Date: 2020-04-03 02:24
 * @Author: PinJyu
 * @Version: 1.0
 **/
public interface SearchService<T> {

    SearchResult<T> search(String query, int number);

}
