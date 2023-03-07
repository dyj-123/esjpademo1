package com.example.esjpademo1.aspect;//package com.example.esdemo.aspect;
//
//import com.example.esdemo.config.RsaProperties;
//import com.example.esdemo.utils.RsaUtils;
//import com.google.common.collect.Lists;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
///**
// * 敏感字段解密/加密切面
// *
// * @Author: zxy
// * @date: 2020/4/29 9:30
// */
//@Component
//@Aspect
//@Slf4j
//public class SensitiveFieldAspect {
//
//    @Around("execution(* com.example.esdemo.controller.EsController.*(..))")
//    public Object doProcess(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        // 捕获方法参数列表
//        List<Object> methodArgs = this.getMethodArgs(proceedingJoinPoint);
////        // 循环所有参数项
//        for (Object item : methodArgs) {
//            // 对参数项进行敏感字段加密处理
//            RsaUtils.handleItem(item, RsaProperties.publicKey,false);
//        }
//        Object result = proceedingJoinPoint.proceed();
//        return result;
//    }
//
//    /**
//     * 获取方法的请求参数
//     */
//    private List<Object> getMethodArgs(ProceedingJoinPoint proceedingJoinPoint) {
//        List<Object> methodArgs = Lists.newArrayList();
//        for (Object arg : proceedingJoinPoint.getArgs()) {
//            if (null != arg) {
//                methodArgs.add(arg);
//            }
//        }
//        return methodArgs;
//    }
//
//}


import com.example.esjpademo1.annotation.SensitiveMethod;
import com.example.esjpademo1.utils.EncryptUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 敏感字段加密切面
 * 支持 String list 类型
 * @Author: zxy
 * @date: 2020/4/29 9:30
 */
@Component
@Aspect
@Slf4j
public class SensitiveFieldAspect {

//    @Around("execution(* com.example.esdemo.controller.EsController.*(..))")
    @Around(value = "@annotation(around)")
    public Object doProcess(ProceedingJoinPoint proceedingJoinPoint, SensitiveMethod around) throws Throwable {
        // 捕获方法参数列表
        List<Object> methodArgs = this.getMethodArgs(proceedingJoinPoint);
        // 循环所有参数项
        for (Object item : methodArgs) {
            // 对参数项进行敏感字段加密处理
            EncryptUtil.handleItem(item);
        }
        //继续执行方法
        Object result = proceedingJoinPoint.proceed();
        return result;
    }

    /**
     * 获取方法的请求参数
     */
    private List<Object> getMethodArgs(ProceedingJoinPoint proceedingJoinPoint) {
        List<Object> methodArgs = Lists.newArrayList();
        for (Object arg : proceedingJoinPoint.getArgs()) {
            if (null != arg) {
                methodArgs.add(arg);
            }
        }
        return methodArgs;
    }

}

