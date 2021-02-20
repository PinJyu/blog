package cn.nhmt.blog.config.security;

import cn.nhmt.blog.dto.Message;
import cn.nhmt.blog.util.WebUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @Description:
 * @Date: 2020-07-05 14:54
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Slf4j
public class CustomSavedRequestAwareAuthenticationSuccessHandlerImpl extends SavedRequestAwareAuthenticationSuccessHandler {

    private HttpSecurity httpSecurity;

    private RequestCache requestCache;

    private final Object initLock = new Object();

    public CustomSavedRequestAwareAuthenticationSuccessHandlerImpl(HttpSecurity httpSecurity) {
        Assert.notNull(httpSecurity, "CustomSavedRequestAwareAuthenticationSuccessHandlerImpl "
                + "need RequestCache in calling onAuthenticationSuccess lazily. HttpSecurity must be not null.");
        this.httpSecurity = httpSecurity;
    }

    private void initRequestCache() {
        if (Objects.isNull(requestCache)) {
            synchronized (initLock) {
                if (Objects.isNull(requestCache)) {
                    requestCache = httpSecurity.getSharedObject(RequestCache.class);
                    if (Objects.isNull(requestCache)) {
                        requestCache = new NullRequestCache();
                    }
                }
            }
        }
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        initRequestCache();

        if (WebUtil.isAjaxRequest(request)) {
            clearAuthenticationAttributes(request);

            SavedRequest savedRequest = requestCache.getRequest(request, response);
            String redirectUri = Objects.isNull(savedRequest) ? determineTargetUrl(request, response)
                    : savedRequest.getRedirectUrl();
            if (!UrlUtils.isAbsoluteUrl(redirectUri)) {
                redirectUri = request.getContextPath() + redirectUri;
            }
            redirectUri = response.encodeRedirectURL(redirectUri);
            log.debug("Redirecting to DefaultSavedRequest Url: {}", redirectUri);

            Message message = new Message(true);
            message.put("redirectUri", redirectUri);
            WebUtil.print(response, message);
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }

    }

}
