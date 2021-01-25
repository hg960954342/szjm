package com.prolog.eis.scheduler;

import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.mcs.MCSTask;
import com.prolog.eis.model.wms.RepeatReport;
import com.prolog.eis.service.RepeatReportService;
import com.prolog.eis.service.impl.EisCallbackServiceSend;
import com.prolog.eis.service.mcs.McsInterfaceService;
import com.prolog.eis.service.mcs.impl.McsInterfaceServiceSend;
import com.prolog.eis.util.FileLogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 回告定时任务 一般都是异步任务因为回告超时时间比较长 防止阻塞
 */
@Component
public class RecallBackTask {


    @Autowired
    private RepeatReportService repeatReportService;

    @Autowired
    private EisCallbackServiceSend eisCallbackServiceSend;

    @Autowired
    private McsInterfaceService mcsInterfaceService;

    @Autowired
    private McsInterfaceServiceSend mcsInterfaceServiceSend;

    @Autowired
    AsyncConfiguration asyncConfiguration;


    /**
     * 回告wms未成功 重复回告
     *
     * @throws Exception
     */
    @Async
    @Scheduled(initialDelay = 3000, fixedDelay = 5000)
    public void doAsyncResendReport(){
        Set<String> asyncSet=asyncConfiguration.getAsyncSet();
        if(!asyncSet.contains("resendReport")){
            asyncSet.add("resendReport");
            resendReport();
            asyncSet.remove("resendReport");
        }
    }

    @Async
    @Scheduled(initialDelay = 3000, fixedDelay = 5000)
    public void doResendMcsTask(){
        Set<String> asyncSet=asyncConfiguration.getAsyncSet();
        if(!asyncSet.contains("resendMcsTask")){
            asyncSet.add("resendMcsTask");
            resendMcsTask();
            asyncSet.remove("resendMcsTask");
        }
    }


    public void resendReport()   {

        List<RepeatReport> repeatReports = repeatReportService.findByState(0);
        if (repeatReports != null && repeatReports.size() > 0) {
            for (RepeatReport repeatReport : repeatReports) {
                eisCallbackServiceSend.recall(repeatReport);
            }
        }

     }


    public void resendMcsTask() {
        try {  List<MCSTask> mcsTasks = mcsInterfaceService.findFailMCSTask();
            for (MCSTask mcsTask : mcsTasks) {
                mcsInterfaceServiceSend.recall(mcsTask);}
        } catch (Exception e) {
            LogServices.logSys(e);
        }



    }
}
