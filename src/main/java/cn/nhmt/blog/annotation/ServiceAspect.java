package cn.nhmt.blog.annotation;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Description: service aspect
 * @Date: 2020-05-26 23:59
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Aspect
public @interface ServiceAspect {

    @AliasFor(annotation = Component.class)
    String value() default "";

    @AliasFor(annotation = Aspect.class, attribute = "value")
    String per() default "";

}
