package cn.nhmt.blog.annotation;


import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Description: controller aspect
 * @Date: 2020-05-26 01:53
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Aspect
public @interface ControllerAspect {

    @AliasFor(annotation = Component.class)
    String value() default "";

    @AliasFor(annotation = Aspect.class, attribute = "value")
    String per() default "";

}
