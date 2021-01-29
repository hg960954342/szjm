package com.prolog.eis.scheduler.async;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * TODO 异步任务单线程执行控制
 */
@Aspect
@Component
public class AsyncAspect {

    @Pointcut("@annotation(org.springframework.scheduling.annotation.Async))")
    public void scheduledAspect() {
    }


    @Autowired
    private AsyncConfiguration asyncConfiguration;

    @Around("scheduledAspect()")
    public Object scheduled(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature sig = joinPoint.getSignature();
        MethodSignature msig = null;
        Object returnObj = null;
        if ((sig instanceof MethodSignature)) {
            msig = (MethodSignature) sig;
            Object target = joinPoint.getTarget();
            Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
            String methodName = currentMethod.getName();
            Set<String> asyncSet = asyncConfiguration.getAsyncSet();
            if (!asyncSet.contains(methodName)) {
                asyncSet.add(methodName);
                returnObj = joinPoint.proceed();
                asyncSet.remove(methodName);
            }
        } else {
            returnObj = joinPoint.proceed();
        }
        return returnObj;


    }
}


