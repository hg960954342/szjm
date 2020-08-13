package com.prolog.eis.service.impl;

import com.prolog.eis.dao.*;
import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.eis.service.OutBoundTaskService;
import com.prolog.eis.service.impl.unbound.OutBoundType;
import com.prolog.eis.service.impl.unbound.UnBoundStragtegy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
@Slf4j
public class OutBoundTaskServiceImpl implements OutBoundTaskService {


    @Autowired
    OutBoundTaskMapper outBoundTaskMapper;

    @Autowired
    OutBoundTaskDetailMapper  outBoundTaskDetailMapper;



    @Autowired
    ContainerTaskMapper containerTaskMapper;

    @Autowired
    ContainerTaskDetailMapper containerTaskDetailMapperMapper;





    @Autowired
    private final Map<String, UnBoundStragtegy> strategyMap = new ConcurrentHashMap<>();

    public OutBoundTaskServiceImpl(Map<String, UnBoundStragtegy> strategyMap) {
        this.strategyMap.clear();
        strategyMap.forEach((k, v)-> this.strategyMap.put(k, v));
    }



    public UnBoundStragtegy getUnBoundStragtegy(OutboundTask OutboundTask){
        return strategyMap.get(OutBoundType.TASK_TYPE+OutboundTask.getTaskType());
    }

    @Override
    public void unboundTask() {

        List<OutboundTask> outboundTaskList=outBoundTaskMapper.getListOutboundTask();
        for (int i = 0; i <outboundTaskList.size() ; i++) {
            OutboundTask OutboundTask=outboundTaskList.get(i);
            UnBoundStragtegy unBoundStragtegy=this.getUnBoundStragtegy(OutboundTask);
            if(null!=unBoundStragtegy){
                log.info(unBoundStragtegy.getClass().getName());
            unBoundStragtegy.unbound(OutboundTask);
            }
        }


    }



}
