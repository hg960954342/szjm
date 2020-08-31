package com.prolog.eis.logs;

/**
 * @description TODO
 * @date 2020/8/26 14:23
 */

import com.prolog.eis.dao.ScheduledLogMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
@Aspect
@Component
public class ScheduledLogHandler {

    @Autowired
    private ScheduledLogMapper scheduledLogMapper;

    @Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void proxyAspect() {

    }

    @Around("proxyAspect()")
    public void doInvoke(ProceedingJoinPoint joinPoint) throws Throwable{
        Date startTime = new Date();
        Object result = joinPoint.proceed();
        Date endTime = new Date();


        Signature sig = joinPoint.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Object target = joinPoint.getTarget();
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        if((endTime.getTime()/1000-startTime.getTime()/1000)>0){
            ScheduledLog scheduledLog=new ScheduledLog();
            scheduledLog.setClassName(target.getClass().getSimpleName());
            scheduledLog.setMethodName(currentMethod.getName());
            scheduledLog.setStartTime(startTime);
            scheduledLog.setEndTime(endTime);
            scheduledLog.setRunTime((endTime.getTime()/1000-startTime.getTime()/1000)+" s");
            scheduledLogMapper.save(scheduledLog);
        }


    }
}
