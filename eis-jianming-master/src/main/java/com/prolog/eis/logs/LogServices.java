package com.prolog.eis.logs;

import com.prolog.eis.dao.EisInterfaceLogMapper;
import com.prolog.eis.dao.LogMapper;
import com.prolog.eis.dao.LogSysMapper;
import com.prolog.framework.core.restriction.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class LogServices {

    @Autowired
    LogMapper logMapper;

    @Autowired
    EisInterfaceLogMapper eisInterfaceLogMapper;

    @Autowired
    LogSysMapper logSysMapper;

    private static LogServices logServices;

    @PostConstruct
    public void init() {
        logServices = this;
        logServices.logMapper = this.logMapper;
        logServices.eisInterfaceLogMapper = this.eisInterfaceLogMapper;
        logServices.logSysMapper=this.logSysMapper;
    }

    /**
     * 记录MCS接口日志
     * @param postUrl
     * @param params
     * @param error
     * @param result
     */
    public static void log(String postUrl,String params,String error,String result ){
          McsLog mcsLog=new McsLog();
           mcsLog.setError(error);
           mcsLog.setInterfaceAddress(postUrl);
           mcsLog.setResult(result);
           mcsLog.setParams(params);
           mcsLog.setCreateTime(new java.util.Date());
           logServices.logMapper.save(mcsLog);
    }

    /**
     * Eis调用MCS接口
     * @param postUrl
     * @param error
     */
    public static void log(String postUrl,String error ){
        McsLog mcsLog=new McsLog();
        mcsLog.setError(error);
        mcsLog.setInterfaceAddress(postUrl);
        mcsLog.setResult("");
        mcsLog.setParams("");
        mcsLog.setCreateTime(new java.util.Date());
        logServices.logMapper.save(mcsLog);
    }

    public static void logEis(String url,String params,String error,String result ){
        EisInterfaceLog eisInterfaceLog=new EisInterfaceLog();
        eisInterfaceLog.setUrl(url);
        eisInterfaceLog.setError(error);
        eisInterfaceLog.setParams(params);
        eisInterfaceLog.setResult(result);
        eisInterfaceLog.setCreateTime(new java.util.Date());
        logServices.eisInterfaceLogMapper.save(eisInterfaceLog);
    }

    /**
     * 系统内部日志
     * @param error
     */
    public static void logSys(String error){
        SysLog sysLog=new SysLog();
        String className = Thread.currentThread().getStackTrace()[2].getClassName();//调用的类名
        sysLog.setClassName(className);
        String classSimpleName = Thread.currentThread().getStackTrace()[2].getClass().getSimpleName();
        sysLog.setClassSimpleName(classSimpleName);
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();//调用的方法名
        sysLog.setClassMethod(methodName);
        int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();//调用的行数
        sysLog.setLineNumber(lineNumber+"");
        sysLog.setError(error);
        sysLog.setCreateTime(new java.util.Date());
        logServices.logSysMapper.save(sysLog);
    }

    /**
     * 清空日志
     */
    public static void deteleteLog(){

        logServices.logMapper.deleteAll();
        logServices.logSysMapper.deleteAll();
        logServices.eisInterfaceLogMapper.deleteAll();
    }

}
