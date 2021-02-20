package cn.nhmt.blog.filter;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * @Description: In PUT method, request with content type "multipart/**" that probably params in request body
 *               cause HiddenHttpMethodFilter unable to get params. This Filter will advance resolver multipart
 *               request. So this should be front of HiddenHttpMethodFilter.
 * @Date: 2020-04-18 15:07
 * @Author: PinJyu
 * @Version: 1.0
 **/
public class MultipartWithPutAdvanceResolveFilter extends OncePerRequestFilter {

    private final static CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest requestToUse = request;
        if (multipartResolver.isMultipart(request) && !(request instanceof MultipartHttpServletRequest)) {
            requestToUse = multipartResolver.resolveMultipart(request);
        }
        filterChain.doFilter(requestToUse, response);
    }

}
