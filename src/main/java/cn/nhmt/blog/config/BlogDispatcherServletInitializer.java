package cn.nhmt.blog.config;

import cn.nhmt.blog.filter.MultipartWithPutAdvanceResolveFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.web.util.WebUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

public class BlogDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    // Root spring容器配置类
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {RootConfiguration.class,};
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
        cef.setEncoding("utf-8");
        cef.setForceEncoding(true);
        MultipartWithPutAdvanceResolveFilter mwparf = new MultipartWithPutAdvanceResolveFilter();
        HiddenHttpMethodFilter hhmf = new HiddenHttpMethodFilter();
        HttpPutFormContentFilter hpfcf = new HttpPutFormContentFilter();
        return new Filter[] {cef, mwparf, hhmf, hpfcf};
    }

}
