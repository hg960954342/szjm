package com.prolog.eis.scheduler;

import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.mcs.MCSTask;
import com.prolog.eis.model.wms.RepeatReport;
import com.prolog.eis.service.EisCallbackService;
import com.prolog.eis.service.RepeatReportService;
import com.prolog.eis.service.mcs.McsInterfaceService;
import com.prolog.eis.util.FileLogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 回告定时任务 一般都是异步任务因为回告超时时间比较长 防止阻塞
 */
@Component
public class RecallBackTask {


    @Autowired
    private RepeatReportService repeatReportService;

    @Autowired
    private EisCallbackService eisCallbackService;

    @Autowired
    private McsInterfaceService mcsInterfaceService;

    /**
     * 回告wms未成功 重复回告
     *
     * @throws Exception
     */
    @Scheduled(initialDelay = 3000, fixedDelay = 5000)
    public void resendReport()   {
        long start = System.currentTimeMillis() /1000;

        List<RepeatReport> repeatReports = repeatReportService.findByState(0);
        if (repeatReports != null && repeatReports.size() > 0) {
            for (RepeatReport repeatReport : repeatReports) {
                eisCallbackService.recall(repeatReport); //异步任务
            }
        }

        long end = System.currentTimeMillis()/1000;
        FileLogHelper.WriteLog("recallBackTask","time:"+(end-start));
    }




    @Scheduled(initialDelay = 3000, fixedDelay = 5000)
    public void resendMcsTask() {
        long start = System.currentTimeMillis() /1000;

        try {  List<MCSTask> mcsTasks = mcsInterfaceService.findFailMCSTask();
            for (MCSTask mcsTask : mcsTasks) {
                mcsInterfaceService.recall(mcsTask);}   //异步任务
        } catch (Exception e) {
            LogServices.logSys("resendMcsTaskMCS重发异常:" + e.toString());
        }

        long end = System.currentTimeMillis()/1000;
        FileLogHelper.WriteLog("recallBackTask","time:"+(end-start));

    }
}
