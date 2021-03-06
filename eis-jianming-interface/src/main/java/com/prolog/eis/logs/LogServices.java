package com.prolog.eis.logs;

import com.prolog.eis.dao.EisInterfaceLogMapper;
import com.prolog.eis.dao.LogSysMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.PrintWriter;
import java.io.StringWriter;

@Component
public class LogServices {


    @Autowired
    EisInterfaceLogMapper eisInterfaceLogMapper;

    @Autowired
    LogSysMapper logSysMapper;


    private static LogServices logServices;


    @PostConstruct
    public void init() {
        logServices = this;
        logServices.eisInterfaceLogMapper = this.eisInterfaceLogMapper;
        logServices.logSysMapper=this.logSysMapper;


    }

    /**
     * 系统内部日志
     *
     */
    public static void logSys(Throwable e){
        SysLog sysLog=new SysLog();
        String className = Thread.currentThread().getStackTrace()[2].getClassName();//调用的类名
        sysLog.setClassName(className);
        String classSimpleName = StringUtils.substring(className,StringUtils.lastIndexOf(className,".")+1);
        sysLog.setClassSimpleName(classSimpleName);
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();//调用的方法名
        sysLog.setClassMethod(methodName);
        int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();//调用的行数
        sysLog.setLineNumber(lineNumber+"");
        sysLog.setError(toString_(e));
        sysLog.setCreateTime(new java.util.Date());
        new Thread( new Runnable(){
            @Override
            public void run() {
                logServices.logSysMapper.save(sysLog);
            }
        }).start();


    }

    /**
     * 记录其他系统-->EIS接口的日志
     *
     * @param url
     * @param params
     * @param error
     * @param result
     */
    public static void logEis(String url, String params, String error, String result) {
        EisInterfaceLog eisInterfaceLog = new EisInterfaceLog();
        eisInterfaceLog.setUrl(url);
        eisInterfaceLog.setError(spliitString(error));
        eisInterfaceLog.setParams(spliitString(params));
        eisInterfaceLog.setResult(spliitString(result));
        eisInterfaceLog.setCreateTime(new java.util.Date());
        logServices.eisInterfaceLogMapper.save(eisInterfaceLog);
    }


    /**
     * 截取错误日志防止日志超长 插入不进
     *
     * @param str
     * @return
     */
    public static String spliitString(String str) {
        if (!str.isEmpty() && str.length() > 4500) {
            return str.substring(0, 4500);
        }
        return str;
    }

    private static String toString_(Throwable e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return spliitString(sw.toString());
    }


}
