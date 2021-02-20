package cn.nhmt.blog.controller;

import cn.nhmt.blog.bo.UserRegisterService;
import cn.nhmt.blog.dto.Message;
import cn.nhmt.blog.po.User;
import cn.nhmt.blog.po.group.UserRegisterGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.security.NoSuchAlgorithmException;

/**
 * @Description: user login register
 * @Date: 2020-04-22 17:12
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Validated
@RestController
@RequestMapping("/register")
public class UserRegisterController {

    @Autowired
    private UserRegisterService userRegisterService;

    @PreAuthorize("hasRole('VISITOR')")
    @PostMapping("/valid-name")
    Message validName(
            @Pattern(regexp = "^(?=[a-zA-Z\\u4e00-\\u9fa5])[a-zA-Z\\u4e00-\\u9fa5\\d]{4,10}$")
            @RequestParam("name") String name
            , @CookieValue("JSESSIONID") String jSessionId
    ) {
        return userRegisterService.isLegalUserName(name, jSessionId);
    }

    @PreAuthorize("hasRole('VISITOR')")
    @PostMapping("/valid-email")
    Message validEmail(
            @Email(regexp = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$")
            @RequestParam("email") String email
            , @CookieValue("JSESSIONID") String jSessionId
    ) {
        return userRegisterService.isLegalUserEmail(email, jSessionId);
    }

    @PreAuthorize("hasRole('VISITOR')")
    @PostMapping("/send-email-CAPTCHA")
    Message sendEmailCAPTCHA(
            @Email(regexp = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$")
            @RequestParam("email") String email
            , @CookieValue("JSESSIONID") String jSessionId
    ) {
        return userRegisterService.sendEmailCAPTCHA(email, jSessionId);
    }

    @PreAuthorize("hasRole('VISITOR')")
    @PostMapping("/go")
    Message register(
            @Validated({UserRegisterGroup.class}) User user
            , @Pattern(regexp = "^\\d{6}$") String CAPTCHA
            , @CookieValue("JSESSIONID") String jSessionId
    ) throws NoSuchAlgorithmException {
        return userRegisterService.register(user, CAPTCHA, jSessionId);
    }

}
