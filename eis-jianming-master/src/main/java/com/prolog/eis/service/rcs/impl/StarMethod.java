package com.prolog.eis.service.rcs.impl;

import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.service.ContainerTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class StarMethod implements AgvMethod{


    @Autowired
    ContainerTaskService containerTaskService;

    @Override
    public void doAction(String taskCode, String method) {
        //根据任务号 查询 托盘任务
        List<ContainerTask> containerTasks = containerTaskService.selectByTaskCode(taskCode);
        if (StringUtils.isEmpty(containerTasks)) return;
        if (containerTasks!=null&&containerTasks.size()==0) return;
        ContainerTask containerTask = containerTasks.get(0);



        //小车任务开始
        containerTask.setTaskState(3);//设置下游设备回告开始
        containerTask.setStartTime(new Date());
        containerTaskService.update(containerTask);

    }
}
