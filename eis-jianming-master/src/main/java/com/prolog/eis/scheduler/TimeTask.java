package com.prolog.eis.scheduler;

import com.prolog.eis.logs.LogServices;
import com.prolog.eis.service.OutBoundTaskService;
import com.prolog.eis.service.sxk.SxStoreCkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 写入库出库任务
 */
@Component
public class TimeTask {


    @Autowired
    OutBoundTaskService outBoundTaskService;
    @Autowired
    private SxStoreCkService sxStoreCkService;






    /**
     * 定时出库任务
     *
     * @throws Exception
     */
    @Scheduled(initialDelay = 3000, fixedDelay = 5000)
    public void buildUnTask()   {
        outBoundTaskService.unboundTask();
    }


    @Scheduled(initialDelay = 3000, fixedDelay = 5000)
    public void buildAndSendSxCkTask()  {

        try {
            synchronized ("kucun".intern()) {
                sxStoreCkService.buildSxCkTask();
            }
            sxStoreCkService.sendSxCkTask();
        } catch (Exception e) {
            LogServices.logSys(e);
        }

    }




 //   private WmsLoginService wmsLoginService;
/*

    //刷新token
    @Scheduled(cron = "0/1 * * * * ? ")
    public void getToken() {
        long currnetTime = System.currentTimeMillis() / 1000;
        long deviationTime = currnetTime - LoginWmsResponse.getTokenTime;
        if (StringUtils.isEmpty(LoginWmsResponse.accessToken) || StringUtils.isEmpty(LoginWmsResponse.expiresIn) || deviationTime >= Integer.parseInt(LoginWmsResponse.expiresIn)) {
            wmsLoginService.loginWms();
        }
    }
*/


}
