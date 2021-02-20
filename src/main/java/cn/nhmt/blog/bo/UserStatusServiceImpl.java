package cn.nhmt.blog.bo;

import cn.nhmt.blog.bo.exception.ErrorCode;
import cn.nhmt.blog.bo.exception.UserExcepition;
import cn.nhmt.blog.bo.service.UserService;
import cn.nhmt.blog.dao.UserDao;
import cn.nhmt.blog.dto.OkMessage;
import cn.nhmt.blog.dto.user.UserIdAndName;
import cn.nhmt.blog.po.User;
import cn.nhmt.blog.util.transfer.po2dto.Po2Dto;
import org.apache.ibatis.annotations.Param;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Description: TODO
 * @Date: 2020-04-22 12:26
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private Po2Dto<User, UserIdAndName> u2uiam;

    @Override
    @Transactional
    public OkMessage register(User user) {
        OkMessage om = ((UserService) AopContext.currentProxy()).validUserNameNotRegister(user.getName());
        if (!om.isOk()) {
            return om;
        }
        om.setOk(false);
        if (!validUserPassword(user.getPassword())) {
            om.setErrorCode(ErrorCode.REGISTRE_USER_PWD_NOT_VALID);
            return om;
        }
        try {
            user.setCreateGmt(new Date());
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] digest = sha256.digest(user.getPassword().getBytes(StandardCharsets.UTF_8));
            user.setPassword(byte2Hex(digest));
        } catch (NoSuchAlgorithmException ignore) { }
        try {
            userDao.post(user);
        } catch (RuntimeException e) {
            throw UserExcepition.forRegister(ErrorCode.REGISTER_FAIL, e);
        }
        om.setOk(true);
        om.put("user", u2uiam.transfer(user));
        return om;
    }

    @Override
    @Transactional(readOnly = true)
    public OkMessage loginValid(User user) {
        OkMessage om = new OkMessage(false);
        if (validFieldEmpty(user.getName()) || validFieldEmpty(user.getPassword())) {
            om.setErrorCode(ErrorCode.LOGIN_USER_NAME_OR_PWD_NULL);
            return om;
        }
        List<User> usersWithTheName;
        try {
            usersWithTheName = userDao.getByName(user.getName());
        } catch (RuntimeException e) {
            throw UserExcepition.forLogin(ErrorCode.LOGIN_FAIL, e);
        }
        if (usersWithTheName != null && usersWithTheName.size() == 1) {
            try {
                User userWithTheName = usersWithTheName.get(0);
                MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
                byte[] digest = sha256.digest(user.getPassword().getBytes(StandardCharsets.UTF_8));
                String sha256Password = byte2Hex(digest);
                if (sha256Password.equals(userWithTheName.getPassword())) {
                    om.setOk(true);
                    om.put("user", u2uiam.transfer(user));
                    return om;
                }
                om.setErrorCode(ErrorCode.LOGIN_USER_PWD_NOT_CORRECT);
                return om;
            } catch (NoSuchAlgorithmException ignore) { }
        }
        om.setErrorCode(ErrorCode.LOGIN_USER_NAME_NOT_EXISTS);
        return om;
    }

    @Override
    @Transactional(readOnly = true)
    public OkMessage offlineValid(User user) {
        OkMessage om = new OkMessage(false);
        if (user.getId() <= 0 || !validFieldInBound(user.getName(), 4, 32)) {
            om.setErrorCode(ErrorCode.OFFLINE_USER_NOT_EXISTS);
            return om;
        }
        User userWithTheId;
        try {
            userWithTheId = userDao.getById(user.getId());
        } catch (RuntimeException e) {
            throw UserExcepition.forOffline(ErrorCode.OFFLINE_FAIL, e);
        }
        if (userWithTheId != null && userWithTheId.getId() == user.getId()
                && userWithTheId.getName().equals(user.getName())) {
            om.setOk(true);
            return om;
        }
        om.setErrorCode(ErrorCode.OFFLINE_USER_INFO_NOT_CORRECT);
        return om;
    }

    @Override
    @Transactional(readOnly = true)
    public OkMessage validUserNameNotRegister(String userName) {
        OkMessage om = new OkMessage(false);
        if (validFieldEmpty(userName)) {
            om.setErrorCode(ErrorCode.REGISTER_USER_NAME_NULL);
            return om;
        }
        if (!validFieldInBound(userName, 4, 32)) {
            om.setErrorCode(ErrorCode.REGISTER_USER_NAME_LENGTH_NOT_VALID);
            return om;
        }
        List<User> usersWithTheName = null;
        try {
            usersWithTheName = userDao.getByName(userName);
        } catch (RuntimeException ignore) { }

        if (usersWithTheName != null && usersWithTheName.size() != 0) {
            om.setErrorCode(ErrorCode.REGISTER_USER_NANE_EXISTS);
            return om;
        }
        om.setOk(true);
        return om;
    }

    private static boolean validFieldEmpty(String field) {
        return field == null || "".equals(field);
    }

    private static boolean validFieldInBound(String field, int start, int end) {
        final int len;
        return !validFieldEmpty(field) && (len = field.length()) >= start && len <= end;
    }

    private static boolean validUserPassword(String userPassword) {
        return validFieldInBound(userPassword, 10, 16)
                && Pattern.matches("\\d+", userPassword)
                && Pattern.matches("[a-zA-Z]", userPassword)
                && Pattern.matches("_", userPassword);
    }

    private static String byte2Hex(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        String temp;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1)
                sb.append("0");
            sb.append(temp);
        }
        return sb.toString();
    }

}
