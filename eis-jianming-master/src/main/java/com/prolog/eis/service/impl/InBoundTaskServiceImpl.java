package com.prolog.eis.service.impl;

import com.prolog.eis.dao.InBoundTaskMapper;
import com.prolog.eis.model.wms.InboundTask;
import com.prolog.eis.service.InBoundTaskService;
import com.prolog.eis.service.impl.inbound.InBoundStragtegy;
import com.prolog.eis.service.impl.inbound.InBoundType;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    public List<InboundTask> selectByContainerCode(String containerCode) {
        Map<String, Object> map = MapUtils.put("containerCode", containerCode).getMap();
        return inBoundTaskMapper.findByMap(map,InboundTask.class);
        /*return inBoundTaskMapper.getReportData(containerCode);*/
    }

    @Override
    public void update(InboundTask inboundTask) {
    inBoundTaskMapper.update(inboundTask);
    }

    @Override
    public void delete(InboundTask inboundTask) {
        inBoundTaskMapper.deleteById(inboundTask.getId(),InboundTask.class);
    }


}