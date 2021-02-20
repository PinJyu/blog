package cn.nhmt.blog.ao.util;

import cn.nhmt.blog.ao.SearchResult;
import cn.nhmt.blog.dto.Po2Dto;

import java.util.List;

/**
 * @Description: 搜索结果转Dto
 * @Date: 2020-04-20 15:02
 * @Author: PinJyu
 * @Version: 1.0
 **/
public interface SearchResult2Dto<T, D> extends Po2Dto<SearchResult<T>, List<D>> {

    List<D> transfer(SearchResult<T> sr);

}
