package com.prolog.eis.service.impl.inbound;

import com.prolog.eis.dao.ContainerTaskDetailMapper;
import com.prolog.eis.dao.ContainerTaskMapper;
import com.prolog.eis.dao.InBoundTaskMapper;
import com.prolog.eis.dao.baseinfo.PortInfoMapper;
import com.prolog.eis.dto.base.Coordinate;
import com.prolog.eis.model.eis.PortInfo;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.model.wms.InboundTask;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.eis.util.PrologLocationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Component(InBoundType.TASK_TYPE+0)
public class OrderInBoundStrategy implements InBoundStragtegy {

    @Autowired
    private InBoundTaskMapper inBoundTaskMapper;

    @Autowired
    ContainerTaskMapper containerTaskMapper;

    @Autowired
    ContainerTaskDetailMapper containerTaskDetailMapper;

    @Autowired
    PortInfoMapper portInfoMapper;
    @Override
    public void inbound(InboundTask inboundTask) {
        {
                InboundTask task = inboundTask;
                String agvLoc=task.getAgvLoc();
                Coordinate CoordinateAgv= PrologCoordinateUtils.analysis(agvLoc);
                //暂时定入库任务状态开始为0

                int taskType=1; //定义任务类型 （1任务托 2包材 3 空拖 4质检 ）
                //获取所有的入库口
                List<PortInfo> listPortInfo=portInfoMapper.getPortInfoByTaskType(taskType);
                //查找同一楼层没有任务占用的入库口集合
                listPortInfo=listPortInfo.stream().filter(listPortInfotemp->{
                    int x=listPortInfotemp.getX();
                    int y=listPortInfotemp.getY();
                    int layer=listPortInfotemp.getLayer();
                    //查找当前点位是否有任务
                    String source=PrologCoordinateUtils.splicingStr(x,y,layer);
                    List<ContainerTask> listContainerTask= containerTaskMapper.selectBySource(source);
                    return (listContainerTask.size()==0)
                            &&layer==CoordinateAgv.getLayer();
                }).collect(Collectors.toList());

                if(listPortInfo.size()==0) return;  //没有则直接结束
                //查找最近的入库口
                PortInfo distinPortInfo=listPortInfo.stream().sorted((s1,s2)->{
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
                String target= PrologLocationUtils.splicingXYStr(PrologCoordinateUtils.splicingStr(distinPortInfo.getX(),distinPortInfo.getY(),distinPortInfo.getLayer()));

                containerTask.setTarget(target);
                containerTask.setSource(PrologLocationUtils.splicingXYStr(agvLoc));

                Date date=new Date();
                containerTask.setCreateTime(date);
                containerTask.setSendTime(date);

                //暂时定入库任务类型为5


                containerTask.setTaskType(5);
                containerTask.setTaskState(1);
                containerTask.setSourceType(2);
                String uuid = UUID.randomUUID().toString().replaceAll("-","");
                containerTask.setTaskCode(uuid);

                containerTaskMapper.save(containerTask);









        }
    }
}
