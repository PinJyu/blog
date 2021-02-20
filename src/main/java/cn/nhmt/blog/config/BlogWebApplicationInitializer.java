package cn.nhmt.blog.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.*;
import java.nio.charset.StandardCharsets;

public class BlogWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    // Root spring容器配置类
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {RootConfiguration.class, SecurityConfiguration.class};
    }

    // MVC spring容器配置类
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {WebConfiguration.class,};
    }

    // DispatcherServlet url映射
    @Override
    protected String[] getServletMappings() {
        return new String[] {"/",};
    }

    // Servlet Filter
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter cef = new CharacterEncodingFilter();
        cef.setEncoding(StandardCharsets.UTF_8.name());
        cef.setForceEncoding(true);
        HiddenHttpMethodFilter hhmf = new HiddenHttpMethodFilter();
        FormContentFilter fcf = new FormContentFilter();
        return new Filter[] {cef, hhmf, fcf};
    }

}
