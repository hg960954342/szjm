package com.prolog.eis.logs.handler;

import com.alibaba.fastjson.JSONObject;
import com.prolog.eis.logs.LogServices;
import com.prolog.framework.common.message.RestMessage;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dubux
 * @description 记录controller层的日志postMapping注解方法
 * @date 2020/8/27 23:47
 */
@Aspect
@Component
public class ControllerLogHandler {



    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping))")
    public void controllerAspect() {
    }


    @Around("controllerAspect()")
    public Object logEis(ProceedingJoinPoint joinPoint) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        String uri = request.getRequestURI();
        Object[] objs = joinPoint.getArgs();
        Object result = "";
        String error = "";
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            LogServices.logSys(e);
            error = e.getMessage();
            result = RestMessage.newInstance(false, "eis内部错误，请检查！"+e.getMessage(),null);
        }
        LogServices.logEis(uri, JSONObject.toJSONString(objs != null && objs.length >= 1 ? objs[0] : null), error, JSONObject.toJSONString(result));
        return result;
    }

    /**
     * controller异常进行记录
     * @param e
     */
    @AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
    public void handleThrowing(Exception e) {
           LogServices.logSys(e);
    }
}
