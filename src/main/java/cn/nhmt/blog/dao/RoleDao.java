package cn.nhmt.blog.dao;

import cn.nhmt.blog.po.Role;

import java.util.List;

/**
 * @Description: TODO
 * @Date: 2020-06-29 14:52
 * @Author: PinJyu
 * @Version: 1.0
 **/
public interface RoleDao {

    Role retrieveByUserId(int userId);

    List<Role> retrieveAll();

    void create(Role role);

    void update(Role role);

    void delete(int userId);

}
