package com.prolog.eis.logs;

import com.prolog.eis.dao.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.PrintWriter;
import java.io.StringWriter;

@Component
public class LogServices {

    @Autowired
    LogMapper logMapper;

    @Autowired
    EisInterfaceLogMapper eisInterfaceLogMapper;

    @Autowired
    LogSysMapper logSysMapper;

    @Autowired
    LogSysBusinessMapper logSysBusinessMapper;

    @Autowired
    RcsLogMapper rcsLogMapper;
    @Autowired
    private  WmsLogMapper wmsLogMapper;
    @Autowired
    ScheduledLogMapper scheduledLogMapper;

    @Autowired
    ThreadPoolTaskScheduler taskScheduler;

    private static LogServices logServices;


    @Bean
    public ThreadPoolTaskScheduler getTaskScheduler(){
        return new ThreadPoolTaskScheduler();
    }



    @PostConstruct
    public void init() {
        logServices = this;
        logServices.logMapper = this.logMapper;
        logServices.eisInterfaceLogMapper = this.eisInterfaceLogMapper;
        logServices.logSysMapper=this.logSysMapper;
        logServices.rcsLogMapper=this.rcsLogMapper;
        logServices.logSysBusinessMapper=this.logSysBusinessMapper;
        logServices.wmsLogMapper=this.wmsLogMapper;
        logServices.scheduledLogMapper=this.scheduledLogMapper;
        logServices.taskScheduler= this.taskScheduler;


     }

    /**
     * 记录EIS->MCS接口日志
     * @param postUrl
     * @param params
     * @param error
     * @param result
     */

    public static void log(String postUrl,String params,String error,String result ){
           McsLog mcsLog=new McsLog();
           mcsLog.setError(spliitString(error));
           mcsLog.setInterfaceAddress(postUrl);
           mcsLog.setResult(spliitString(result));
           mcsLog.setParams(spliitString(params));
           mcsLog.setCreateTime(new java.util.Date());
           logServices.logMapper.save(mcsLog);


    }

    /**
     * 记录EIS->MCS接口日志
     * @param postUrl
     * @param error
     */
    public static void log(String postUrl,String error ){
         McsLog mcsLog=new McsLog();
        mcsLog.setError(spliitString(error));
        mcsLog.setInterfaceAddress(postUrl);
        mcsLog.setResult("");
        mcsLog.setParams("");
        mcsLog.setCreateTime(new java.util.Date());
        logServices.logMapper.save(mcsLog);


    }

    /**
     * 记录EIS->Wms接口日志
     * @param postUrl
     * @param error
     */
    public static void logWms(String postUrl,String params,String error,String result){
         WmsLog wmsLog = new WmsLog();
        wmsLog.setError(spliitString(error));
        wmsLog.setInterfaceAddress(postUrl);
        wmsLog.setResult(spliitString(result));
        wmsLog.setParams(spliitString(params));
        wmsLog.setCreateTime(new java.util.Date());
        logServices.wmsLogMapper.save(wmsLog);

    }



    /**
     * 记录其他系统-->EIS接口的日志
     * @param url
     * @param params
     * @param error
     * @param result
     */
    public static void logEis(String url,String params,String error,String result ){
        EisInterfaceLog eisInterfaceLog=new EisInterfaceLog();
        eisInterfaceLog.setUrl(url);
        eisInterfaceLog.setError(spliitString(error));
        eisInterfaceLog.setParams(spliitString(params));
        eisInterfaceLog.setResult(spliitString(result));
        eisInterfaceLog.setCreateTime(new java.util.Date());
        logServices.eisInterfaceLogMapper.save(eisInterfaceLog);
    }

    /**
     * EIS->RCS接口请求日志
     * @param postUrl
     * @param params
     * @param error
     * @param result
     */
    public static void logRcs(String postUrl,String params,String error,String result ){
        RcsLog rcsLog=new RcsLog();
        rcsLog.setError(spliitString(error));
        rcsLog.setInterfaceAddress(postUrl);
        rcsLog.setResult(spliitString(result));
        rcsLog.setParams(spliitString(params));
        rcsLog.setCreateTime(new java.util.Date());
        logServices.rcsLogMapper.save(rcsLog);

    }


    /**
     * 系统内部日志
     *
     */
    public static void logSys(Throwable e){
        SysLog sysLog=new SysLog();
        String className = Thread.currentThread().getStackTrace()[2].getClassName();
        sysLog.setClassName(className);
        String classSimpleName = StringUtils.substring(className,StringUtils.lastIndexOf(className,".")+1);
        sysLog.setClassSimpleName(classSimpleName);
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        sysLog.setClassMethod(methodName);
        int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
        sysLog.setLineNumber(lineNumber+"");
        sysLog.setError(toString(e));
        sysLog.setCreateTime(new java.util.Date());
        logServices.taskScheduler.execute(()->{
            logServices.logSysMapper.save(sysLog);
        });



    }

    /**
     * 系统内部业务输出日志
     * @param errorMsg
     */
    public static void logSysBusiness(String errorMsg){
        SysBusinessLog sysBusinessLog=new SysBusinessLog();
        String className = Thread.currentThread().getStackTrace()[2].getClassName();
        sysBusinessLog.setClassName(className);
        String classSimpleName = StringUtils.substring(className,StringUtils.lastIndexOf(className,".")+1);
        sysBusinessLog.setClassSimpleName(classSimpleName);
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        sysBusinessLog.setClassMethod(methodName);
        int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
        sysBusinessLog.setLineNumber(lineNumber+"");
        sysBusinessLog.setError(spliitString(errorMsg));
        sysBusinessLog.setCreateTime(new java.util.Date());
        logServices.logSysBusinessMapper.save(sysBusinessLog);
    }
    private static String toString(Throwable e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return spliitString(sw.toString());
    }


    /**
     * 清空日志
     */
    public static void deteleteLog(){

        logServices.logMapper.deleteAll();
        logServices.logSysMapper.deleteAll();
        logServices.eisInterfaceLogMapper.deleteAll();
        logServices.rcsLogMapper.deleteAll();
        logServices.wmsLogMapper.deleteAll();
        logServices.scheduledLogMapper.deleteAll();
    }

    /**
     * 截取错误日志防止日志超长 插入不进
     * @param str
     * @return
     */
    public static String spliitString(String str) {
        if(!str.isEmpty()&&str.length()>4500){
             return str.substring(0,4500);
        }
        return str;
    }

}
