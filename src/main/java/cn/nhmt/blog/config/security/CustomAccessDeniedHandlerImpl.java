package cn.nhmt.blog.config.security;

import cn.nhmt.blog.ao.UserAo;
import cn.nhmt.blog.dto.Message;
import cn.nhmt.blog.util.WebUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: oriented ajax and document
 * @Date: 2020-07-05 13:25
 * @Author: PinJyu
 * @Version: 1.0
 **/
public class CustomAccessDeniedHandlerImpl extends AccessDeniedHandlerImpl {

    public CustomAccessDeniedHandlerImpl(String error) {
        setErrorPage(error);
    }

    private void log(HttpServletRequest request, AccessDeniedException accessDeniedException) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        StringBuilder sb = new StringBuilder().append(request.getRequestURI()).append(" (Access deny) ");

        if (Objects.nonNull(authentication)) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserAo) {
                UserAo userAo = (UserAo) principal;
                sb.append("username:").append(userAo.getUsername()).append(" id:").append(userAo.getId()).append(" ");
            } else if (principal instanceof String) {
                sb.append("authentication:").append("anonymous ");
            } else {
                sb.append("authentication:").append(principal).append(" ");
            }
        } else {
            sb.append("authentication:without any authentication ");
        }

        sb.append("Address:").append(request.getRemoteAddr());

        logger.error(sb.toString(), accessDeniedException);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log(request, accessDeniedException);

        if (WebUtil.isAjaxRequest(request) || HttpMethods.contains(request.getMethod().toUpperCase())) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            Message message = new Message(false);
            message.setMessage(request.getRequestURI() + " (Access deny)");
            WebUtil.print(response, message);
        } else {
            super.handle(request, response, accessDeniedException);
        }
    }

    private Set<String> HttpMethods;

    {
        Set<String> set = Collections.newSetFromMap(new ConcurrentHashMap<>());
        set.add(HttpMethod.POST.name());
        set.add(HttpMethod.PUT.name());
        set.add(HttpMethod.DELETE.name());
        HttpMethods = Collections.unmodifiableSet(set);

    }

}
