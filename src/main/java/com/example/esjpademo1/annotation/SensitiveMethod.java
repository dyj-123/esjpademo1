package com.example.esjpademo1.annotation;

import java.lang.annotation.*;

/**
*@描述 在方法上加上该注解，改变执行目标方法的参数值
*@创建人 dyj
*@创建时间 2023/2/13
**/
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveMethod {
}
