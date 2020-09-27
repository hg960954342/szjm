package com.prolog.eis.service.impl.inbound;

import com.prolog.eis.dao.AgvStorageLocationMapper;
import com.prolog.eis.dto.base.Coordinate;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.service.enums.PortInfoTaskTypeEnum;
import com.prolog.eis.util.PrologLocationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InBoundContainerService {

    @Autowired
    AgvStorageLocationMapper agvStorageLocationMapper;

    /**
     * 任务托入库口Agv点位
     * @param CoordinateAgv
     * @return
     */
    public AgvStorageLocation getInBound(Coordinate coordinateAgv){
         return getInBound(coordinateAgv,PortInfoTaskTypeEnum.TASK); //默认任务托
    }
    /**
     * 获取相应类型的入库口Agv点位
     * @param CoordinateAgv
     * @return
     */
    public AgvStorageLocation getInBound(Coordinate coordinateAgv,PortInfoTaskTypeEnum taskType){
        //获取所有的入库口
        List<AgvStorageLocation> listPortInfo=agvStorageLocationMapper.getPortInfoByTaskType(taskType.getTaskType());
        //查找同一楼层没有任务占用的入库口集合
        listPortInfo=listPortInfo.stream().filter(listPortInfotemp->{
            int x=listPortInfotemp.getX();
            int y=listPortInfotemp.getY();
            int layer=listPortInfotemp.getCeng();

            //查找当前点位是否有任务
            String source= PrologLocationUtils.splicingXYStr(layer,x,y);
            List<ContainerTask> listContainerTask= new ArrayList<>();//containerTaskMapper.selectBySource(source);
            return (listContainerTask.size()==0)
                    &&((layer)==(coordinateAgv.getLayer()));
        }).collect(Collectors.toList());

        if(listPortInfo.size()==0) {
            return null;  }
        //查找最近的入库口
        AgvStorageLocation distinPortInfo=listPortInfo.stream().sorted((s1,s2)->{
            double x1 = Math.abs(coordinateAgv.getX()- s1.getX());
            double y1 = Math.abs(coordinateAgv.getY()- s1.getY());
            Double p1 =Math.sqrt(x1*x1+y1*y1);
            double x2 = Math.abs(coordinateAgv.getX()- s2.getX());
            double y2 = Math.abs(coordinateAgv.getY()- s2.getY());
            Double p2 = Math.sqrt(x2*x2+y2*y2);
            return  p1.compareTo(p2);
        }).collect(Collectors.toList()).get(0);

        return distinPortInfo;
    }

}
