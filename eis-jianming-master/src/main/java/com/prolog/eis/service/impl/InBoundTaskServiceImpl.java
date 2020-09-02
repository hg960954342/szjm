package com.prolog.eis.service.impl;

import com.prolog.eis.dao.AgvStorageLocationMapper;
import com.prolog.eis.dao.InBoundTaskMapper;
import com.prolog.eis.dto.base.Coordinate;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.model.wms.InboundTask;
import com.prolog.eis.service.InBoundTaskService;
import com.prolog.eis.service.impl.inbound.InBoundStragtegy;
import com.prolog.eis.service.impl.inbound.InBoundType;
import com.prolog.eis.util.PrologLocationUtils;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class InBoundTaskServiceImpl implements InBoundTaskService {

    @Autowired
    private InBoundTaskMapper inBoundTaskMapper;
    @Autowired
    AgvStorageLocationMapper agvStorageLocationMapper;




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




    public AgvStorageLocation getInBound(Coordinate CoordinateAgv){
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
                    &&((layer)==(CoordinateAgv.getLayer()));
        }).collect(Collectors.toList());

        if(listPortInfo.size()==0) {
            return null;  }
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

        return distinPortInfo;
    }


}