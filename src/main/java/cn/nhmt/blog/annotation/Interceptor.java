package cn.nhmt.blog.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Description: TODO
 * @Date: 2020-04-24 16:25
 * @Author: PinJyu
 * @Version: 1.0
 **/

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface interceptor {

    @AliasFor(annotation = Component.class)
    String value() default "";
}
