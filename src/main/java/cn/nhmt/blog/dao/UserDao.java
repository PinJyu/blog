package cn.nhmt.blog.dao;

import cn.nhmt.blog.po.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: TODO
 * @Date: 2020-04-20 16:02
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Repository
public interface UserDao {

    User getById(int id);

    List<User> getByName(String name);

    List<User> getUsers();

    void post(User user);

    void put(User user);

    void deleteById(int id);
}
