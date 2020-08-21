package com.prolog.eis.logs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LogTask {

    /**
     * 每天12点半清除日志的定时任务
     */
    @Scheduled(cron = "0 30 12 * * ?")
    public void clearLog(){
     LogServices.deteleteLog();
    }
}
