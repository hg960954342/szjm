package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.*;
import com.prolog.eis.dao.baseinfo.PortInfoMapper;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.eis.PortInfo;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.eis.service.enums.ContainerTaskTaskTypeEnum;
import com.prolog.eis.service.enums.OutBoundEnum;
import com.prolog.eis.service.enums.OutBoundType;
import com.prolog.eis.service.enums.PortInfoTaskTypeEnum;
import com.prolog.eis.service.impl.unbound.entity.CheckOutResult;
import com.prolog.eis.service.impl.unbound.entity.CheckOutTask;
import com.prolog.eis.service.sxk.SxStoreCkService;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.framework.utils.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Component(OutBoundType.TASK_TYPE+3)
@Slf4j
public class OrderCheckOutBoundStrategy extends DefaultOutBoundPickCodeStrategy {

    @Autowired
    AgvStorageLocationMapper agvStorageLocationMapper;
    @Autowired
    PickStationMapper pickStationMapper;

    @Autowired
    ContainerTaskMapper containerTaskMapper;


    @Autowired
    OutBoundTaskMapper outBoundTaskMapper;

    @Autowired
    OutBoundTaskDetailMapper outBoundTaskDetailMapper;

    @Autowired
    ContainerTaskDetailMapper containerTaskDetailMapperMapper;



    @Autowired
    QcSxStoreMapper qcSxStoreMapper;

    @Autowired
    PortInfoMapper portInfoMapper;

    @Autowired
    CheckOutTaskMapper checkOutTaskMapper;

    @Autowired
    SxStoreCkService sxStoreCkService;




    @Override
    @Transactional(rollbackFor=Exception.class)
    public void unbound(OutboundTask outboundTask){
        if(checkOutTaskMapper.getCount()>0)  {LogServices.logSysBusiness("上次盘点任务还未结束"); return;}
       List<CheckOutResult> list= qcSxStoreMapper.getCheckOutByOutBoundTaskDetail(outboundTask.getBillNo());

       for(CheckOutResult checkOutResult:list){
           ContainerTask task=new ContainerTask();
           BeanUtils.copyProperties(checkOutResult,task);
           task.setTaskType(ContainerTaskTaskTypeEnum.ORDER_CHECK_OUT_BOUND.getTaskType());
           task.setTaskState(1);
           task.setSource(PrologCoordinateUtils.splicingStr(checkOutResult.getX(),checkOutResult.getY(),checkOutResult.getLayer()));
           task.setSourceType(1);
           //健民只有一个任务出库口
           List<PortInfo> portList = portInfoMapper.findByMap(MapUtils.put("portType", 2).put("taskType", PortInfoTaskTypeEnum.TASK.getTaskType()).put("portlock", 2).put("taskLock", 2).getMap(), PortInfo.class);
           if(portList.isEmpty()) {
               return;
           }
           PortInfo ckPortInfo = portList.get(0);
           AgvStorageLocation agvStorageLocation=agvStorageLocationMapper.findByPickCodeAndLock(ckPortInfo.getJunctionPort(),0,0);
           task.setTarget(agvStorageLocation.getRcsPositionCode());
           task.setTargetType(OutBoundEnum.TargetType.AGV.getNumber());
           List<String> listItemNames=outBoundTaskDetailMapper.getItemNamesByBillNo(outboundTask.getBillNo());
           if(listItemNames.size()>0){
               task.setItemName(listItemNames.get(0));
           }
           task.setCreateTime(new Date());
            sxStoreCkService.buildSxCkTaskByContainerTask(task);
           CheckOutTask checkOutTask=new CheckOutTask();
           checkOutTask.setBillNo(outboundTask.getBillNo());
           checkOutTask.setContainerCode(checkOutResult.getContainerCode());
           checkOutTask.setState("1");
           checkOutTaskMapper.save(checkOutTask);
           outboundTask.setTaskState(1);
           outBoundTaskMapper.update(outboundTask);

       }


    }

}