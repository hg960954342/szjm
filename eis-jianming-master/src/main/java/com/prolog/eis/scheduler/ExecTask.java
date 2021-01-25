package com.prolog.eis.scheduler;

import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.service.ContainerTaskService;
import com.prolog.eis.service.InBoundTaskService;
import com.prolog.eis.service.OutBoundTaskService;
import com.prolog.eis.service.impl.EisSendRcsTaskServiceSend;
import com.prolog.eis.service.sxk.SxStoreCkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @description 执行入库出库的定时器
 * @date 2020/8/27 9:17
 */
@Component
public class ExecTask {


    @Autowired
    private ContainerTaskService containerTaskService;

    @Autowired
    private EisSendRcsTaskServiceSend eisSendRcsTaskServiceSend;

    @Autowired
    InBoundTaskService inBoundTaskService;

    @Autowired
    AsyncConfiguration asyncConfiguration;



    /**
     * 定时处理入库任务
     *
     * @throws Exception
     */
    @Scheduled(initialDelay = 3000, fixedDelay = 5000)
    public void buildCkTask()   {
        inBoundTaskService.inboundTask();
    }

    //定时给agv小车下分任务
    @Async
    @Scheduled(initialDelay = 3000, fixedDelay = 5000)
    public void doAsyncSendTask2Rcs(){
        Set<String> asyncSet=asyncConfiguration.getAsyncSet();
        if(!asyncSet.contains("sendTask2Rcs")){
            asyncSet.add("sendTask2Rcs");
            sendTask2Rcs();
            asyncSet.remove("sendTask2Rcs");
        }
    }



    public void sendTask2Rcs()  {

        List<ContainerTask> containerTasks = containerTaskService.selectByTaskStateAndSourceType("1", "2");
        if (!containerTasks.isEmpty() && containerTasks.size() > 0) {
            eisSendRcsTaskServiceSend.sendTask(containerTasks);
        }

    }
}
