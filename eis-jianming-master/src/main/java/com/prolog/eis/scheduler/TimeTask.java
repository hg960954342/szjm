package com.prolog.eis.scheduler;

import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.service.ContainerTaskService;
import com.prolog.eis.service.EisSendRcsTaskService;
import com.prolog.eis.service.InBoundTaskService;
import com.prolog.eis.service.OutBoundTaskService;
import com.prolog.eis.service.sxk.SxStoreCkService;
import com.prolog.eis.util.FileLogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class TimeTask {

    @Autowired
    InBoundTaskService inBoundTaskService;
    @Autowired
    OutBoundTaskService outBoundTaskService;


    @Autowired
    private EisSendRcsTaskService eisSendRcsTaskService;

    @Autowired
    private ContainerTaskService containerTaskService;

    @Autowired
    private SxStoreCkService sxStoreCkService;

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(50);
        return taskScheduler;
    }

    /**
     * 定时处理入库任务
     *
     * @throws Exception
     */
    @Scheduled(initialDelay = 3000, fixedDelay = 5000)
    public void buildCkTask()   {
        long start=System.currentTimeMillis();
        FileLogHelper.WriteLog("timeTask","TimeTask类 buildCkTask方法 开始时间:"+(start / 1000));
        inBoundTaskService.inboundTask();
        long end=System.currentTimeMillis();
        FileLogHelper.WriteLog("timeTask","TimeTask类 buildCkTask方法 结束时间:"+(end/1000));
    }


    /**
     * 定时出库任务
     *
     * @throws Exception
     */
    @Scheduled(initialDelay = 3000, fixedDelay = 5000)
    public void buildUnTask()   {
        long start=System.currentTimeMillis();
        FileLogHelper.WriteLog("timeTask","TimeTask类 buildUnTask方法 结束时间:"+(start/1000));

        outBoundTaskService.unboundTask();
        long end=System.currentTimeMillis();
        FileLogHelper.WriteLog("timeTask","TimeTask类 buildUnTask方法 结束时间:"+(end/1000));

    }

    @Scheduled(initialDelay = 3000, fixedDelay = 5000)
    public void buildAndSendSxCkTask()  {
        long start=System.currentTimeMillis();
        FileLogHelper.WriteLog("timeTask","TimeTask类 buildAndSendSxCkTask方法 结束时间:"+(start/1000));

        try {
            synchronized ("kucun".intern()) {
                sxStoreCkService.buildSxCkTask();
            }
            sxStoreCkService.sendSxCkTask();
        } catch (Exception e) {
             LogServices.logSys("生成四向库出库任务错误"+e.getMessage());
        }
        long end=System.currentTimeMillis();
        FileLogHelper.WriteLog("timeTask","TimeTask类 buildAndSendSxCkTask方法 结束时间:"+(end/1000));

    }






    //定时给agv小车下分任务
	@Scheduled(initialDelay = 3000,fixedDelay = 5000)
    public void sendTask2Rcs()  {
        long start=System.currentTimeMillis();
        FileLogHelper.WriteLog("timeTask","TimeTask类 sendTask2Rcs方法 结束时间:"+(start/1000));

        List<ContainerTask> containerTasks = containerTaskService.selectByTaskStateAndSourceType("1", "2");
        if (!containerTasks.isEmpty() && containerTasks.size() > 0) {
            eisSendRcsTaskService.sendTask(containerTasks);  //异步任务
        }
        long end=System.currentTimeMillis();
        FileLogHelper.WriteLog("timeTask","TimeTask类 sendTask2Rcs方法 结束时间:"+(end/1000));

    }






//    @Scheduled(initialDelay = 3000, fixedDelay = 5000)
   /* public void testReport()   {
        ContainerTask containerTask = new ContainerTask();
//		containerTask.setContainerCode("800011");
//        containerTask.setContainerCode("800012");//出库
//        containerTask.setSource("057200AB054000");
//		containerTask.setContainerCode("700010");//移库
//		containerTask.setTaskType(2);
//		containerTask.setItemId("SPH00001363");
//		containerTask.setOwnerId("008");
//		eisCallbackService.inBoundReport("800027");//入库
//		eisCallbackService.inBoundReport("800045");//移入回告
//        eisCallbackService.outBoundReport(containerTask);
        AgvStorageLocation byRcs = agvStorageLocationMapper.findByRcs("060080AB054000");

//		eisCallbackService.moveBoundReport(containerTask);
//		eisCallbackService.checkBoundReport("PDC00000101");


    }*/

  /*  @Autowired
    private WmsLoginService wmsLoginService;*/
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
