package cn.nhmt.blog.bo.impl;

import cn.nhmt.blog.bo.UserLoginService;
import cn.nhmt.blog.contants.Contant;
import cn.nhmt.blog.dao.UserDao;
import cn.nhmt.blog.dto.Message;
import cn.nhmt.blog.po.User;
import cn.nhmt.blog.util.DigestUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @Description: 用户登陆
 * @Date: 2020-04-22 22:07
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Service
public class UserLoginServiceImpl implements UserLoginService {

    private Set<String> ignoreSet;

    @Autowired
    private UserDao userDao;

    @PostConstruct
    public void init() {
        ignoreSet = new HashSet<>();
        ignoreSet.add("createGmt");
        ignoreSet.add("lastModifyGmt");
    }

    @Transactional(readOnly = true)
    @Override
    public Message loginWithUserName(User user, HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException {
        User userWithName = userDao.retrieveByName(user.getName(), ignoreSet);
        Message message = new Message(false);
        if (userWithName != null && userWithName.getPassword().equals(DigestUtil.sha256(user.getPassword()))) {
            message.setOk(true);
            setJWTCookie(userWithName, request, response);
        }
        return message;
    }

    @Transactional(readOnly = true)
    @Override
    public Message loginWithUserEmail(User user, HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException {
        User userWithEmail = userDao.retrieveByEmail(user.getEmail(), ignoreSet);
        Message message = new Message(false);
        if (userWithEmail != null && userWithEmail.getPassword().equals(DigestUtil.sha256(user.getPassword()))) {
            message.setOk(true);
            setJWTCookie(userWithEmail, request, response);
        }
        return message;
    }

    private void setJWTCookie(User user, HttpServletRequest request, HttpServletResponse response) {
        Calendar calendar = Calendar.getInstance();
        Date iatDate = calendar.getTime();
        calendar.add(Calendar.HOUR, 24);
        Date expDate = calendar.getTime();
        String token = JWT.create()
                .withClaim("user_id", user.getId())
                .withClaim("user_name", user.getName())
                .withClaim("user_email", user.getEmail())
                .withClaim("user_address", request.getRemoteAddr())
                .withIssuedAt(iatDate)
                .withExpiresAt(expDate)
                .sign(Algorithm.HMAC256(Contant.SALT));
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/blog/");
        cookie.setHttpOnly(false);
        cookie.setMaxAge(Contant.COOKIE_MAX_AGE);
        response.addCookie(cookie);
    }

}
