package com.prolog.eis.service.impl;

import com.prolog.eis.dao.ContainerTaskDetailMapper;
import com.prolog.eis.dao.ContainerTaskMapper;
import com.prolog.eis.dao.OutBoundTaskDetailMapper;
import com.prolog.eis.dao.OutBoundTaskMapper;
import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.eis.service.OutBoundTaskService;
import com.prolog.eis.service.enums.OutBoundType;
import com.prolog.eis.service.impl.unbound.UnBoundStragtegy;
import com.prolog.eis.service.sxk.SxStoreCkService;
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
    private SxStoreCkService sxStoreCkService;






    @Autowired
    private final Map<String, UnBoundStragtegy> strategyMap = new ConcurrentHashMap<>();

    public OutBoundTaskServiceImpl(Map<String, UnBoundStragtegy> strategyMap) {
        this.strategyMap.clear();
        strategyMap.forEach((k, v)-> this.strategyMap.put(k, v));
    }



    public UnBoundStragtegy getUnBoundStragtegy(OutboundTask outboundTask){
        return strategyMap.get(OutBoundType.TASK_TYPE+outboundTask.getTaskType());
    }

    @Override
    public void unboundTask() {

        List<OutboundTask> outboundTaskList=outBoundTaskMapper.getListOutboundTask();
        for (int i = 0; i <outboundTaskList.size() ; i++) {
            OutboundTask outboundTask=outboundTaskList.get(i);
            UnBoundStragtegy unBoundStragtegy=this.getUnBoundStragtegy(outboundTask);
            if(null!=unBoundStragtegy){
            unBoundStragtegy.unbound(outboundTask);
            }
        }


    }



}
