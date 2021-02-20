package cn.nhmt.blog.bo;

import cn.nhmt.blog.dto.Message;
import cn.nhmt.blog.po.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;

/**
 * @Description: user登陆
 * @Date: 2020-05-23 00:08
 * @Author: PinJyu
 * @Version: 1.0
 **/
public interface UserLoginService {

    Message loginWithUserName(User user, HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException;

    Message loginWithUserEmail(User user, HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException;

}
