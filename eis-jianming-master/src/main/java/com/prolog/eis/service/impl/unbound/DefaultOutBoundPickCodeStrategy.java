package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.*;
import com.prolog.eis.dao.baseinfo.PortInfoMapper;
import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.eis.model.wms.PickStation;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 订单出库必须继承此类 名称必须符合 OutBoundType.TASK_TYPE+数据库中配置的类型数字
 *  * IF_PICKCODE_EXISTS 是否指定拣选站 1指定 0不指定
 */
@Component(OutBoundType.TASK_TYPE+1)
@Slf4j
public class DefaultOutBoundPickCodeStrategy implements UnBoundStragtegy {

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
   private  Map<String, SimilarityDataEntityLoadInterface> similarityDataEntityListLoadMap  ;



    @Autowired
    private  Map<String, DefaultOutBoundPickCodeStrategy> strategyMap  ;

    public SimilarityDataEntityLoadInterface getsimilarityDataEntityListLoad(OutboundTask OutboundTask){
        int isPickCode=OutboundTask.getSfReq();
        return similarityDataEntityListLoadMap.get(OutBoundType.IF_SfReq+isPickCode);
    }

    public DefaultOutBoundPickCodeStrategy getDefaultOutBoundPickCodeStrategy(OutboundTask OutboundTask){
        int isPickCode=OutboundTask.getSfReq();
        return strategyMap.get(OutBoundType.TASK_TYPE+OutboundTask.getTaskType()+OutBoundType.IF_SfReq+isPickCode);
    }

    @Override
    public void unbound(OutboundTask outboundTask) {
        DefaultOutBoundPickCodeStrategy defaultOutBoundPickCodeStrategy=this.getDefaultOutBoundPickCodeStrategy(outboundTask);
        SimilarityDataEntityLoadInterface similarityDataEntityLoadStrategy=getsimilarityDataEntityListLoad(outboundTask);
        if(null!=defaultOutBoundPickCodeStrategy&&similarityDataEntityLoadStrategy!=null){
            similarityDataEntityLoadStrategy.addOutboundTask(outboundTask);
             if(similarityDataEntityLoadStrategy.getCrrentBillNoList().size()!=0
                    &&similarityDataEntityLoadStrategy.getCrrentBillNoList().size()==similarityDataEntityLoadStrategy.getMaxSize())
            defaultOutBoundPickCodeStrategy.unbound(outboundTask);
        }

    }

    /**
     *获取所有能够作业的拣选站
     * @return
     */
    public List<PickStation> getAvailablePickStation(){
        //获取所有能用的拣选站
        Criteria pickStationCriteria=Criteria.forClass(PickStation.class);
        Long[] ios=new Long[]{1L,3L};
        Long[] taskTypes=new Long[]{1L,3L,5L,7L};
        pickStationCriteria.setRestriction(Restrictions.in("io",ios));
        pickStationCriteria.setRestriction(Restrictions.in("taskType",taskTypes));
        pickStationCriteria.setRestriction(Restrictions.eq("isLock",0 ));
        List<PickStation> listPickStation= pickStationMapper.findByCriteria(pickStationCriteria);
      /*  //过滤掉不能用的拣选站
        listPickStation=listPickStation.stream().filter(x->{
            String stationNo=x.getDeviceNo();
            AgvStorageLocation agvStorageLocation=agvStorageLocationMapper.findByPickCodeAndLock(stationNo,0,0);
            if(null!=agvStorageLocation){

                return true;
            }
            return false;
        }).collect(Collectors.toList());*/
        return listPickStation;
    }

    /**
     * 判断目标点位是否有任务
     * @param
     * @return boolean
     */
    public boolean isExistTask(String source){
        //List<ContainerTask> list=containerTaskMapper.selectBySource(source);
        //if(list!=null&&list.size()>0) return true;
        return false;
    }


}
