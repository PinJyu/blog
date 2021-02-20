package cn.nhmt.blog.bo.service;

import org.springframework.beans.factory.SmartInitializingSingleton;

import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @Date: 2020-04-19 15:40
 * @Author: PinJyu
 * @Version: 1.0
 **/
public interface CacheService<K, V> extends SmartInitializingSingleton {

    int getTotalCount();

    Map<K, V> getTotalMap();

     V getCache(K u);

    void doInit();

    default void pulishBuildCacheEvent() {
        doInit();
    }

    @Override
    default void afterSingletonsInstantiated() {
        doInit();
    }
}
