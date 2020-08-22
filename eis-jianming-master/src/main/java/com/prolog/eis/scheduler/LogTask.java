package com.prolog.eis.scheduler;

import com.prolog.eis.logs.LogServices;
import com.prolog.eis.util.FileLogHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class LogTask {

    /**
     * 每天12点半清除日志的定时任务
     */
    @Scheduled(cron = "0 30 12 * * ?")
    public void clearLog(){
        long start = System.currentTimeMillis() /1000;
        LogServices.deteleteLog();
        long end = System.currentTimeMillis()/1000;
        FileLogHelper.WriteLog("timeTask","logTask:"+(end-start));

    }
}
