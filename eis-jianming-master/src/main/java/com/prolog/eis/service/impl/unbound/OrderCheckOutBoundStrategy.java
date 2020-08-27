package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.*;
import com.prolog.eis.dao.baseinfo.PortInfoMapper;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.eis.PortInfo;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.eis.service.enums.OutBoundEnum;
import com.prolog.eis.service.impl.unbound.entity.CheckOutResult;
import com.prolog.eis.service.impl.unbound.entity.CheckOutTask;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.eis.util.PrologLocationUtils;
import com.prolog.eis.util.PrologStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Component(OutBoundType.TASK_TYPE+3)
@Transactional(rollbackFor=Exception.class)
@Slf4j
@SuppressWarnings("all")
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




    @Override
    public void unbound(OutboundTask outboundTask){
        if(checkOutTaskMapper.getCount()!=null)  {LogServices.logSysBusiness("上次盘点任务还未结束"); return;}
       List<CheckOutResult> list= qcSxStoreMapper.getCheckOutByOutBoundTaskDetail(outboundTask.getBillNo());
       for(CheckOutResult checkOutResult:list){
           ContainerTask task=new ContainerTask();
           BeanUtils.copyProperties(checkOutResult,task);
           task.setTaskType(3);
           task.setTaskState(1);
           task.setSource(PrologCoordinateUtils.splicingStr(checkOutResult.getX(),checkOutResult.getY(),checkOutResult.getLayer()));
           task.setSourceType(1);
            //获取所在4楼层的入库口
           List<PortInfo> listPort=portInfoMapper.getPortInfoInBy(1,2);
           if(list.size()==0) {LogServices.logSysBusiness("没有可用的入库口用于盘点！");break;}
           PortInfo portInfo=listPort.get(0);
           task.setTarget(PrologLocationUtils.splicingXYStr(portInfo.getLayer(),portInfo.getX(),portInfo.getY()));
           task.setTargetType(OutBoundEnum.TargetType.SSX.getNumber());

           task.setTaskCode(PrologStringUtils.newGUID());
           task.setCreateTime(new Date());
           containerTaskMapper.save(task);
           CheckOutTask checkOutTask=new CheckOutTask();
           checkOutTask.setBillNo(outboundTask.getBillNo());
           checkOutTask.setContainerCode(checkOutResult.getContainerCode());
           checkOutTask.setState("1");
           checkOutTaskMapper.save(checkOutTask);

       }


    }

}