package com.prolog.eis.service.rcs.impl;

import com.prolog.eis.dao.AgvStorageLocationMapper;
import com.prolog.eis.model.wms.AgvStorageLocation;
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
public class OutbinMethod implements AgvMethod{
    @Autowired
    ContainerTaskService containerTaskService;
    @Autowired
    AgvStorageLocationMapper agvStorageLocationMapper;
    @Override
    public void doAction(String taskCode, String method) {

        //根据任务号 查询 托盘任务
        List<ContainerTask> containerTasks = containerTaskService.selectByTaskCode(taskCode);
        if (StringUtils.isEmpty(containerTasks)) {return;}
        if (containerTasks!=null&&containerTasks.size()==0) {return;}
        ContainerTask containerTask = containerTasks.get(0);

        AgvStorageLocation currentPosition = agvStorageLocationMapper.findByRcs(containerTask.getSource());


            //小车离开原存储位
            containerTask.setTaskState(4);//设置下游设备离开原存储位
            containerTask.setMoveTime(new Date());
            containerTaskService.update(containerTask);
            //更新点位状态
            currentPosition.setTaskLock(0);
            currentPosition.setLocationLock(0);
            agvStorageLocationMapper.update(currentPosition);




    }
}
