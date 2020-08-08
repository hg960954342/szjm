package com.prolog.eis.service.impl;

import com.prolog.eis.dao.ContainerTaskDetailMapper;
import com.prolog.eis.dao.ContainerTaskMapper;
import com.prolog.eis.dao.InBoundTaskMapper;
import com.prolog.eis.dao.baseinfo.PortInfoMapper;
import com.prolog.eis.dto.base.Coordinate;
import com.prolog.eis.model.eis.PortInfo;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.model.wms.InboundTask;
import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.eis.service.InBoundTaskService;
import com.prolog.eis.service.impl.inbound.InBoundStragtegy;
import com.prolog.eis.service.impl.inbound.InBoundType;
import com.prolog.eis.service.impl.unbound.OutBoundType;
import com.prolog.eis.service.impl.unbound.UnBoundStragtegy;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class InBoundTaskServiceImpl implements InBoundTaskService {

    @Autowired
    private InBoundTaskMapper inBoundTaskMapper;




    @Autowired
    private final Map<String, InBoundStragtegy> strategyMap = new ConcurrentHashMap<>();

    public InBoundTaskServiceImpl(Map<String, InBoundStragtegy> strategyMap) {
        this.strategyMap.clear();
        strategyMap.forEach((k, v)-> this.strategyMap.put(k, v));
    }



    public InBoundStragtegy getInBoundTaskServiceImpl(InboundTask inboundTask){
        return strategyMap.get(InBoundType.TASK_TYPE+inboundTask.getTaskType());
    }


   @Override
    public void inboundTask() {
       List<InboundTask> list=inBoundTaskMapper.getListInboundTask();
          for(InboundTask inboundTask:list){
              InBoundStragtegy inBoundStragtegy=getInBoundTaskServiceImpl(inboundTask);
              if(null!=inBoundStragtegy){
                  inBoundStragtegy.inbound(inboundTask);
              }
          }


        }

    @Override
    public InboundTask selectByContainerCode(String containerCode) {
        return inBoundTaskMapper.getReportData(containerCode);
    }

    @Override
    public void update(InboundTask inboundTask) {
    inBoundTaskMapper.update(inboundTask);
    }


}