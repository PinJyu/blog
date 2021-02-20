package cn.nhmt.blog.controller;

import cn.nhmt.blog.bo.exception.ErrorCode;
import cn.nhmt.blog.bo.service.UserStatusService;
import cn.nhmt.blog.dto.OkMessage;
import cn.nhmt.blog.dto.user.UserIdAndName;
import cn.nhmt.blog.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: TODO
 * @Date: 2020-04-22 17:12
 * @Author: PinJyu
 * @Version: 1.0
 **/
@RestController
public class UserStatusController {

    @Autowired
    private UserStatusService userStatusService;

    private ConcurrentHashMap<Integer, User> loginUserIdMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, UserIdAndName> loginUserNameMap = new ConcurrentHashMap<>();

    @PostMapping("/login")
    OkMessage login(User user, HttpSession session) {
        OkMessage om;
        String userName;
        if ((userName = user.getName()) != null && loginUserNameMap.containsKey(userName) ) {
            om = new OkMessage(false);
            om.setErrorCode(ErrorCode.LOGIN_ALREADY);
            return om;
        }
        om = userStatusService.loginValid(user);
        if (!om.isOk()) {
            return om;
        }

        UserIdAndName user2login = (UserIdAndName) om.getOptional().get("user");
        userName = user2login.getName();
        loginUserNameMap.put(userName, user2login);
        session.setAttribute(userName, user2login);

    }



}
