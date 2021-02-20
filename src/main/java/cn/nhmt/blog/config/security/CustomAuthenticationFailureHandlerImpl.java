package cn.nhmt.blog.config.security;

import cn.nhmt.blog.contants.ErrorCode;
import cn.nhmt.blog.dto.Message;
import cn.nhmt.blog.util.WebUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @Description: oriented ajax
 * @Date: 2020-07-05 12:45
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Slf4j
@Data
@NoArgsConstructor
public class CustomAuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    private static final String DEFAULT_USERNAME_PARAMTER = "username";

    private String usernameParamter = DEFAULT_USERNAME_PARAMTER;

    private boolean hideUsernameNotFoundException = true;

    private AuthenticationException getRealExcetption(AuthenticationException ex) {
        AuthenticationException rex = CustomUserDetailsServiceImpl.AuthenticationExceptionHolder.get();
        return Objects.isNull(rex) ? ex : rex;
    }

    private Message getMessage(HttpServletResponse response, AuthenticationException ex) {
        Message message = new Message(false);
        message.setMessage(ex.getMessage());

        Class<? extends AuthenticationException> clazz = ex.getClass();
        if (UsernameNotFoundException.class.isAssignableFrom(clazz)) {
            message.setErrorCode(ErrorCode.LOGIN_USER_NAME_NOT_EXISTS);
        } else if (BadCredentialsException.class.isAssignableFrom(clazz)) {
            message.setErrorCode(ErrorCode.LOGIN_USER_PWD_NOT_CORRECT);
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            message.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }

        return message;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        AuthenticationException realExcetption = getRealExcetption(exception);
        Message message = getMessage(response, hideUsernameNotFoundException ? exception : realExcetption);
        WebUtil.print(response, message);
        log.error("username:{} (Auth fail) address:{}", request.getParameter(usernameParamter), request.getRemoteAddr(), realExcetption);
    }

}

