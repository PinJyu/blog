package cn.nhmt.blog.ao.util;

import cn.nhmt.blog.ao.SearchResult;

import java.util.List;

/**
 * @Description: 搜索结果转Dto
 * @Date: 2020-04-20 15:02
 * @Author: PinJyu
 * @Version: 1.0
 **/
public interface SearchResult2Dto<T, D> {

    List<D> transfer(SearchResult<T> sr);

}
