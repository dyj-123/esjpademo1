package com.example.esjpademo1.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;


/**
 * @author: zxy
 * @desc: 加在敏感字段字段上，实现自动加密
 * @date: 2020/4/29 9:30
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface SensitiveField {
    String value();
    boolean isKeyword() default false;
}
