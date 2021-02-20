package cn.nhmt.blog.bo.impl;

import cn.nhmt.blog.bo.RestService;
import cn.nhmt.blog.contants.ErrorCode;
import cn.nhmt.blog.dao.UserDao;
import cn.nhmt.blog.dto.Message;
import cn.nhmt.blog.po.User;
import cn.nhmt.blog.util.DigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @Description: User crud
 * @Date: 2020-05-22 21:56
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Service
@CacheConfig(cacheNames = "user")
public class UserRestServiceImpl implements RestService<User> {

    @Autowired
    private UserDao userDao;

    @Cacheable(keyGenerator = "keyGenerator")
    @Transactional(readOnly = true)
    @Override
    public Message doGet(Integer pageNumber, Integer perPage, Integer offset, Integer limit, String sortBy, Boolean isAsc, String key, Set<String> ignoreSet, Set<String> foreignSet) {
        Message message = new Message(false);
        message.setErrorCode(ErrorCode.RETRIEVE_FAIL);
        List<User> users = userDao.retrieveAll(pageNumber, perPage, offset, limit, sortBy, isAsc, ignoreSet);
        if (Objects.nonNull(users)) {
            message.setOk(true);
            message.setErrorCode(0);
            message.put("users", users);
        }

        return message;
    }

    @Cacheable(keyGenerator = "keyGenerator")
    @Transactional(readOnly = true)
    @Override
    public Message doGet(int id, Set<String> ignoreSet, Set<String> foreignSet) {
        Message message = new Message(false);
        message.setErrorCode(ErrorCode.RETRIEVE_FAIL);
        User user = userDao.retrieveById(id, ignoreSet);
        if (Objects.nonNull(user)) {
            message.setOk(true);
            message.setErrorCode(0);
            message.put("user", user);
        }
        return message;
    }

    @CacheEvict(allEntries = true)
    @Transactional
    @Override
    public Message doPost(User source, User user){
        Message message = new Message(false);
        source.setCreateGmt(new Date());
        source.setLastModifyGmt(source.getCreateGmt());
        try {
            source.setPassword(DigestUtil.sha256(source.getPassword()));
            userDao.create(user);
        } catch (NoSuchAlgorithmException | RuntimeException e) {
            message.setErrorCode(ErrorCode.CREATE_FAIL);
            message.setMessage("用户创建异常：参数" + source);
        }
        message.setOk(true);
        message.put("currentUser", user);
        message.put("user", source);
        return message;
    }

    @CacheEvict(allEntries = true)
    @Transactional
    @Override
    public Message doPut(User source, User user) {
        Message message = new Message(false);
        source.setId(user.getId());
        source.setLastModifyGmt(new Date());
        try {
            String password;
            if (Objects.nonNull(password = source.getPassword())) {
                source.setPassword(DigestUtil.sha256(password));
            }
            userDao.update(user);
        } catch (NoSuchAlgorithmException | RuntimeException e) {
            message.setErrorCode(ErrorCode.UPDATE_FAIL);
            message.setMessage("用户更新异常：参数" + source);
        }
        message.setOk(true);
        message.put("user", source);

        return message;
    }

    @CacheEvict(allEntries = true)
    @Transactional
    @Override
    public Message doDelete(int key, User user) {
        Message message = new Message(false);
        try {
            if (key != user.getId()) {
                throw new RuntimeException();
            }
            userDao.delete(key);
        } catch (RuntimeException e) {
            message.setErrorCode(ErrorCode.DELETE_FAIL);
            message.setMessage(String.format("用户删除异常：参数{user.id=%d, current.user.id=%d}", key, user.getId()));
        }
        message.setOk(true);
        message.put("user", user);

        return message;
    }

}
