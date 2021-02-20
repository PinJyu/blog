package cn.nhmt.blog.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Configurable
@EnableWebMvc
@ComponentScan(value = "cn.nhmt.blog.controller")
public class WebConfiguration implements WebMvcConfigurer {

    // 文件上传处理
    @Bean("multipartResolver")
    public CommonsMultipartResolver multipartResolverConfig() {
        return new CommonsMultipartResolver();
    }

    // 配置freeMarker
    @Bean
    public FreeMarkerConfig freeMarkerConfig() {
        FreeMarkerConfigurer fmc = new FreeMarkerConfigurer();
        fmc.setTemplateLoaderPath("/WEB-INF/pages/html");
        fmc.setDefaultEncoding("utf-8");
        return fmc;
    }

    // 注册视图解析器
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        FreeMarkerViewResolver fmvr = new FreeMarkerViewResolver();
        fmvr.setSuffix(".html");
        fmvr.setContentType("text/html; charset=utf-8");
        fmvr.setOrder(0);
        registry.viewResolver(fmvr);
        InternalResourceViewResolver irvr = new InternalResourceViewResolver();
        irvr.setPrefix("/WEB-INF/pages/jsp/");
        irvr.setSuffix(".jsp");
        irvr.setOrder(1);
        registry.viewResolver(irvr);
    }

    // 静态资源映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("css/**").addResourceLocations("WEB-INF/static/css/");
        registry.addResourceHandler("js/**").addResourceLocations("WEB-INF/static/js/");
        registry.addResourceHandler("plugins/**").addResourceLocations("WEB-INF/static/plugins/");
        registry.addResourceHandler("images/**").addResourceLocations("WEB-INF/static/images/");
        registry.addResourceHandler("files/**").addResourceLocations("WEB-INF/static/files");
        registry.addResourceHandler("editormd/**").addResourceLocations("WEB-INF/static/plugins/editormd/");
    }

}
