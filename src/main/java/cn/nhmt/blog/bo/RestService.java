package cn.nhmt.blog.bo;

import cn.nhmt.blog.dto.Message;
import cn.nhmt.blog.po.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.IntStream;

/**
 * @Description: restful service 总接口
 * @Date: 2020-05-18 20:48
 * @Author: PinJyu
 * @Version: 1.0
 **/
public interface RestService<T> {

    Message doGet(Integer pageNumber
            , Integer perPage
            , Integer offset
            , Integer limit
            , String sortBy
            , Boolean isAsc
            , String key
            , Set<String> ignoreSet
            , Set<String> foreignSet);

    Message doGet(int id, Set<String> ignoreSet, Set<String> foreignSet);

    Message doPost(T source, User user);

    Message doPut(T source, User user);

    Message doDelete(int key, User user);

//    default StringBuilder retrieveAllParamString(
//            Integer pageNumber
//            , Integer perPage
//            , Integer offset
//            , Integer limit
//            , String sortBy
//            , Boolean isAsc
//            , String key
//            , Set<String> ignoreSet
//            , Set<String> foreignSet
//    ) {
//        StringBuilder sb = new StringBuilder().append("{");
//        if (Objects.nonNull(pageNumber)) {
//            sb.append("pageNumber=").append(pageNumber).append(", ");
//        }
//        if (Objects.nonNull(perPage)) {
//            sb.append("perPage=").append(perPage).append(", ");
//        }
//        if (Objects.nonNull(offset)) {
//            sb.append("offset=").append(offset).append(", ");
//        }
//        if (Objects.nonNull(limit)) {
//            sb.append("limit=").append(limit).append(", ");
//        }
//        if (Objects.nonNull(sortBy)) {
//            sb.append("sortBy=").append(sortBy).append(", ");
//        }
//        if (Objects.nonNull(isAsc)) {
//            sb.append("isAsc=").append(isAsc).append(", ");
//        }
//        if (Objects.nonNull(key)) {
//            sb.append("key=").append(key).append(", ");
//        }
//        if (!ObjectUtils.isEmpty(ignoreSet)) {
//            sb.append(set2String("ignoreSet", ignoreSet));
//        }
//        if (!ObjectUtils.isEmpty(foreignSet)) {
//            sb.append(set2String("foreignSet", foreignSet));
//        }
//        int len;
//        if ((len = sb.length()) > 2) {
//            sb.replace(len - 2, len, "");
//        }
//        sb.append("}; ");
//        return sb;
//    }
//
//    default StringBuilder retrieveOneParamsString(int id, Set<String> ignoreSet, Set<String> foreignSet) {
//        StringBuilder sb = new StringBuilder().append("{").append("id=").append(id).append(", ");
//        if (!ObjectUtils.isEmpty(ignoreSet)) {
//            sb.append(set2String("ignoreSet", ignoreSet));
//        }
//        if (!ObjectUtils.isEmpty(foreignSet)) {
//            sb.append(set2String("foreignSet", foreignSet));
//        }
//        int len;
//        sb.replace((len = sb.length()) - 2, len, "");
//        sb.append("}; ");
//        return sb;
//    }
//
//    private StringBuilder set2String(String setName, Set<String> set) {
//        Objects.requireNonNull(setName);
//        Objects.requireNonNull(set);
//        List<String> tempList = new ArrayList<>(set);
//        StringBuilder sb = new StringBuilder().append(setName).append("=[");
//        for (int i = 0, max = tempList.size() - 1; i <= max; ++i) {
//            sb.append(tempList.get(i));
//            if (i == max) {
//                break;
//            }
//            sb.append(", ");
//        }
//        sb.append("], ");
//        return sb;
//    }

}
