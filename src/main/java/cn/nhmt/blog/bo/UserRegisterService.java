package cn.nhmt.blog.bo;

import cn.nhmt.blog.dto.Message;
import cn.nhmt.blog.po.User;

import java.security.NoSuchAlgorithmException;

/**
 * @Description: TODO
 * @Date: 2020-05-24 20:51
 * @Author: PinJyu
 * @Version: 1.0
 **/
public interface UserRegisterService {

    Message register(User user, String CAPTCHA, String jSessionId) throws NoSuchAlgorithmException;

    Message isLegalUserName(String userName, String jSessionId);

    Message isLegalUserEmail(String userEmail, String jSessionId);

    Message sendEmailCAPTCHA(String userEmail, String jSessionId);

}
