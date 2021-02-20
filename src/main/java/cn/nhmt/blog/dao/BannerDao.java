package cn.nhmt.blog.dao;

import cn.nhmt.blog.po.Banner;

import java.util.List;

/**
 * @Description: TODO
 * @Date: 2020-06-29 14:53
 * @Author: PinJyu
 * @Version: 1.0
 **/
public interface BannerDao {

    Banner retrieveByAddress(String addresss);

    Banner retrieveById(int id);

    List<Banner> retrieveAll();

    void update(Banner banner);

    void create(Banner banner);

    void deleteById(int id);

    void deleteByAddress(String address);

}

