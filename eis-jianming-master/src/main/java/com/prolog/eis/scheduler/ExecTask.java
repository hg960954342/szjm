package com.prolog.eis.scheduler;

import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.service.ContainerTaskService;
import com.prolog.eis.service.InBoundTaskService;
import com.prolog.eis.service.impl.EisSendRcsTaskServiceSend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public void sendTask2Rcs()  {

        List<ContainerTask> containerTasks = containerTaskService.selectByTaskStateAndSourceType("1", "2");
        if (!containerTasks.isEmpty() && containerTasks.size() > 0) {
            eisSendRcsTaskServiceSend.sendTask(containerTasks);
        }

    }
}
