package com.prolog.eis.scheduler;

import com.prolog.eis.logs.LogServices;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LogTask {

    /**
     * 每个周日 凌晨1点执行 清空日志
     */
    @Scheduled(cron = "0 0 1 ? * SUN")
    public void clearLog(){
          LogServices.deteleteLog();

    }
}
