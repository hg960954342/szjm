package com.prolog.eis.service.impl.inbound;

import com.prolog.eis.dao.AgvStorageLocationMapper;
import com.prolog.eis.dao.ContainerTaskDetailMapper;
import com.prolog.eis.dao.ContainerTaskMapper;
import com.prolog.eis.dao.InBoundTaskMapper;
import com.prolog.eis.dao.baseinfo.PortInfoMapper;
import com.prolog.eis.dto.base.Coordinate;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.model.wms.InboundTask;
import com.prolog.eis.util.PrologLocationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Component(InBoundType.TASK_TYPE+2)
@Transactional(rollbackFor=Exception.class)
@Slf4j
public class OrderInBoundStrategy implements InBoundStragtegy {

    @Autowired
    private InBoundTaskMapper inBoundTaskMapper;

    @Autowired
    ContainerTaskMapper containerTaskMapper;

    @Autowired
    ContainerTaskDetailMapper containerTaskDetailMapper;
    @Autowired
    AgvStorageLocationMapper agvStorageLocationMapper;

    @Autowired
    PortInfoMapper portInfoMapper;
    @Override
    public void inbound(InboundTask inboundTask) {
        {
                InboundTask task = inboundTask;
                String agvLoc=task.getAgvLoc();
                Coordinate CoordinateAgv= PrologLocationUtils.analysis(agvLoc);
                //暂时定入库任务状态开始为0

                int taskType=1; //定义任务类型 （1任务托 2包材 3 空拖 4质检 ）
                //获取所有的入库口
                List<AgvStorageLocation> listPortInfo=agvStorageLocationMapper.getPortInfoByTaskType(taskType);
                //查找同一楼层没有任务占用的入库口集合
                listPortInfo=listPortInfo.stream().filter(listPortInfotemp->{
                    int x=listPortInfotemp.getX();
                    int y=listPortInfotemp.getY();
                    int layer=listPortInfotemp.getCeng();

                    //查找当前点位是否有任务
                    String source= PrologLocationUtils.splicingXYStr(layer,x,y);
                    List<ContainerTask> listContainerTask= new ArrayList<>();//containerTaskMapper.selectBySource(source);
                    return (listContainerTask.size()==0)
                            &&((layer+"").equals(task.getCeng()));
                }).collect(Collectors.toList());

            if(listPortInfo.size()==0) {
                log.error("没有找到taskType："+taskType+"入口");
                return;  }
                //查找最近的入库口
                 AgvStorageLocation distinPortInfo=listPortInfo.stream().sorted((s1,s2)->{
                    double _x1 = Math.abs(CoordinateAgv.getX()- s1.getX());
                    double _y1 = Math.abs(CoordinateAgv.getY()- s1.getY());
                    Double _p1 =Math.sqrt(_x1*_x1+_y1*_y1);
                    double _x2 = Math.abs(CoordinateAgv.getX()- s2.getX());
                    double _y2 = Math.abs(CoordinateAgv.getY()- s2.getY());
                    Double _p2 = Math.sqrt(_x2*_x2+_y2*_y2);
                    return  _p1.compareTo(_p2);
                }).collect(Collectors.toList()).get(0);

                //写托盘任务
                ContainerTask containerTask=new ContainerTask();
                BeanUtils.copyProperties(task,containerTask);

                containerTask.setTarget(PrologLocationUtils.splicingXYStr(distinPortInfo.getCeng(),distinPortInfo.getX(),distinPortInfo.getY()));
                containerTask.setSource(agvLoc);

                Date date=new Date();
                containerTask.setCreateTime(date);
                containerTask.setSendTime(date);

                //暂时定入库任务类型为5


                containerTask.setTaskType(5);
                containerTask.setTaskState(1);
                containerTask.setSourceType(2);
                containerTask.setTargetType(2);
                String uuid = UUID.randomUUID().toString().replaceAll("-","");
                containerTask.setTaskCode(uuid);

                containerTaskMapper.save(containerTask);
                //更新入库状态
            inboundTask.setTaskState(1);
            inBoundTaskMapper.update(inboundTask);







        }
    }
}
