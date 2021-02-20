package cn.nhmt.blog.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

/**
 * @Description: Only register spring security face filter DelegatingFilterProxy. Don't give configClasses.
 * @Date: 2020-06-22 16:22
 * @Author: PinJyu
 * @Version: 1.0
 **/
public class BlogSecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

    /**
     * Suffix is dispatcher servlet name; "org.springframework.web.servlet.FrameworkServlet.CONTEXT."
     * + dispatcherServletName as key is registered with WebApplicationContext as value in ServletContext
     * during FrameworkServlet init;
     *
     * In first time, DelegatingFilterProxy do with doFilter method that will find WebApplicationContext
     * in ServletContext use pre key. And get Real Inner Filter.
     * @return dispatcher servlet name;
     */
    @Override
    protected String getDispatcherWebApplicationContextSuffix() {
        return AbstractDispatcherServletInitializer.DEFAULT_SERVLET_NAME;
    }

}
