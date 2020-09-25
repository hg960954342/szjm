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
    public AgvStorageLocation getInBound(Coordinate CoordinateAgv){
         return getInBound(CoordinateAgv,PortInfoTaskTypeEnum.TASK); //默认任务托
    }
    /**
     * 获取相应类型的入库口Agv点位
     * @param CoordinateAgv
     * @return
     */
    public AgvStorageLocation getInBound(Coordinate CoordinateAgv,PortInfoTaskTypeEnum taskType){
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
