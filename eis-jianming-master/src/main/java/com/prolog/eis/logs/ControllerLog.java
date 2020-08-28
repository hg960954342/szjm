package com.prolog.eis.logs;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @description 记录controller层的未捕获异常
 * @date 2020/8/27 23:47
 */
@Aspect
@Component
public class ControllerLog {


    @Pointcut("@annotation(org.springframework.stereotype.Controller)")
    public void controllerAspect() {
    }


    @AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        LogServices.logSys(e);
    }


}
