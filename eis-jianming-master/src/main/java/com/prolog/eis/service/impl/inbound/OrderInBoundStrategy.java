package com.prolog.eis.service.impl.inbound;

import com.prolog.eis.dao.AgvStorageLocationMapper;
import com.prolog.eis.dao.ContainerTaskDetailMapper;
import com.prolog.eis.dao.ContainerTaskMapper;
import com.prolog.eis.dao.InBoundTaskMapper;
import com.prolog.eis.dao.baseinfo.PortInfoMapper;
import com.prolog.eis.dto.base.Coordinate;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.model.wms.InboundTask;
import com.prolog.eis.service.enums.ContainerTaskTaskTypeEnum;
import com.prolog.eis.service.enums.InBoundType;
import com.prolog.eis.util.PrologLocationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
@Component(InBoundType.TASK_TYPE+2)
@Transactional(rollbackFor=Exception.class)
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
    @Autowired
    InBoundContainerService inBoundContainerService;
    @Override
    public void inbound(InboundTask inboundTask) {
        {
                InboundTask task = inboundTask;
                String agvLoc=task.getAgvLoc();
                Coordinate coordinateAgv= PrologLocationUtils.analysis(agvLoc);
                //暂时定入库任务状态开始为0

            AgvStorageLocation  distinPortInfo=inBoundContainerService.getInBound(coordinateAgv);
            if(distinPortInfo==null){
                LogServices.logSysBusiness("订单入库没有找到可用入口");
                return;
            }

                //写托盘任务
                ContainerTask containerTask=new ContainerTask();
                BeanUtils.copyProperties(task,containerTask);

                //copyProperties转换数量不成功,手动set,get
                containerTask.setQty(task.getQty());
                containerTask.setTarget(PrologLocationUtils.splicingXYStr(distinPortInfo.getCeng(),distinPortInfo.getX(),distinPortInfo.getY()));
                containerTask.setSource(agvLoc);

                Date date=new Date();
                containerTask.setCreateTime(date);
                containerTask.setSendTime(date);

                //暂时定入库任务类型为5


                containerTask.setTaskType(ContainerTaskTaskTypeEnum.ORDER_IN_BOUND.getTaskType());
                containerTask.setTaskState(1);
                containerTask.setSourceType(2);
                containerTask.setTargetType(2);

               // containerTask.setTaskCode(PrologStringUtils.newGUID());

                containerTaskMapper.save(containerTask);
                //更新入库状态
            inboundTask.setTaskState(1);
            inBoundTaskMapper.update(inboundTask);







        }
    }
}
