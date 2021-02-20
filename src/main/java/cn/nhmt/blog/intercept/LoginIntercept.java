package cn.nhmt.blog.intercept;

import cn.nhmt.blog.contants.Contant;
import cn.nhmt.blog.contants.ErrorCode;
import cn.nhmt.blog.dto.Message;
import cn.nhmt.blog.po.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 拦截需要登陆的请求
 * @Date: 2020-04-22 17:08
 * @Author: PinJyu
 * @Version: 1.0
 **/
public class LoginIntercept implements HandlerInterceptor {

    private ConcurrentHashMap<Integer, Date> userIdIssueAtDateMap = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        boolean isLogin = false;
        User user = null;
        if (StringUtils.hasText(token.trim())) {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(Contant.SALT)).acceptLeeway(60).build();
            DecodedJWT decodedJWT = null;
            try {
                decodedJWT = jwtVerifier.verify(token);
            } catch (Exception ignored) {

            }
            if (Objects.nonNull(decodedJWT)) {
                Claim user_address;
                if (!(user_address = decodedJWT.getClaim("user_address")).isNull()
                        && user_address.asString().equals(request.getRemoteAddr())) {
                    Integer user_id;
                    if (isNewestToken(user_id = decodedJWT.getClaim("user_id").asInt(), decodedJWT.getIssuedAt())) {
                        user = new User();
                        user.setId(user_id);
                        user.setName(decodedJWT.getClaim("user_name").asString());
                        user.setEmail(decodedJWT.getClaim("user_email").asString());
                        isLogin = true;
                    }
                }
            }
        }
        after(isLogin, user, request, response);

        return isLogin;
    }

    private boolean isNewestToken(Integer userId, Date issueAt) {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(issueAt);
        Date newVal = userIdIssueAtDateMap.compute(userId, (k, v) -> v == null || v.before(issueAt) ? issueAt : v);
        return newVal.equals(issueAt);
    }

    private void after(boolean isLogin, User user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isLogin) {
            Objects.requireNonNull(user);
            request.setAttribute("user",  user);
        } else {
            String redirectUri = "/blog/login?redirect=" + request.getRequestURI();
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                Message message = new Message(false);
//                message.setErrorCode(ErrorCode.UNLOGIN);
                message.put("redirect", redirectUri);
                String jsonObj = new ObjectMapper()
                        .writeValueAsString(message);
                response.setStatus(302);
                response.setContentType("application/json; charSet=utf-8");
                try (PrintWriter writer = response.getWriter()) {
                    writer.print(jsonObj);
                    writer.flush();
                }
            } else {
                response.sendRedirect(redirectUri);
            }
        }
    }

}
