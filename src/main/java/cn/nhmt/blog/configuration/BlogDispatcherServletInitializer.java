package cn.nhmt.blog.configuration;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

public class BlogDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    // Root spring容器配置类
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { RootConfiguration.class };
    }

    // MVC spring容器配置类
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { WebConfiguration.class };
    }

    // DispatcherServlet url映射
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    // Servlet Filter
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter cef = new CharacterEncodingFilter();
        cef.setEncoding("utf-8");
        cef.setForceEncoding(true);
        return new Filter[] { cef };
    }
}
