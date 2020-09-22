package com.prolog.eis.logs;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @description 记录Service层未捕获所有异常
 * @date 2020/8/27 23:47
 */
@Aspect
@Component
@ControllerAdvice
public class ServiceLog {


    @Pointcut("@annotation(org.springframework.stereotype.Service)")
    public void serviceAspect() {
    }


    @AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
         LogServices.logSys(e);
    }

    @ExceptionHandler(Throwable.class)
    public void handleException(Throwable e){
        LogServices.logSys(e);
    }
}
