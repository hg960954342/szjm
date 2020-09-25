package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.*;
import com.prolog.eis.dao.baseinfo.PortInfoMapper;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.eis.model.wms.PickStation;
import com.prolog.eis.service.enums.*;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单出库必须继承此类 名称必须符合 OutBoundType.TASK_TYPE+数据库中配置的类型数字
 *  * IF_PICKCODE_EXISTS 是否指定拣选站 1指定 0不指定
 */
@Component(OutBoundType.TASK_TYPE+1)
@Primary
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
     public synchronized void unbound(OutboundTask outboundTask) {
        DefaultOutBoundPickCodeStrategy defaultOutBoundPickCodeStrategy=this.getDefaultOutBoundPickCodeStrategy(outboundTask);
        SimilarityDataEntityLoadInterface similarityDataEntityLoadStrategy=getsimilarityDataEntityListLoad(outboundTask);
        if(null!=defaultOutBoundPickCodeStrategy&&similarityDataEntityLoadStrategy!=null){
            similarityDataEntityLoadStrategy.addOutboundTask(outboundTask);
             if(similarityDataEntityLoadStrategy.getCrrentBillNoList().size()!=0
                    &&similarityDataEntityLoadStrategy.getCrrentBillNoList().size()==similarityDataEntityLoadStrategy.getMaxSize())
             {defaultOutBoundPickCodeStrategy.unbound(outboundTask);
             }
             else if(similarityDataEntityLoadStrategy.getCrrentBillNoList().size()!=0
              &&similarityDataEntityLoadStrategy.getCrrentBillNoList().size()>similarityDataEntityLoadStrategy.getMaxSize()){
                 similarityDataEntityLoadStrategy.getCrrentBillNoList().clear();
             }else{}
        }

    }

    /**
     *获取所有能够作业的拣选站
     * @return
     */
    public List<PickStation> getAvailablePickStation(){
        //获取所有能用的拣选站
        Criteria pickStationCriteria=Criteria.forClass(PickStation.class);
        Integer[] ios=new Integer[]{PickStationIOTypeEnum.IN.getIoType(),PickStationIOTypeEnum.IN_OUT.getIoType()};
        Integer[] taskTypes=new Integer[]{
                PickStationTaskTypeEnum.ORDER_TASK_TYPE.getTaskType(),
                PickStationTaskTypeEnum.ORDER_AND_MOVE_TASK_TYPE.getTaskType(),
                PickStationTaskTypeEnum.ORDER_AND_EMPTY_TASK_TYPE.getTaskType(),
                PickStationTaskTypeEnum.ALL_TASK_TYPE.getTaskType()
                };
        pickStationCriteria.setRestriction(Restrictions.in("io",ios));
        pickStationCriteria.setRestriction(Restrictions.in("taskType",taskTypes));
        pickStationCriteria.setRestriction(Restrictions.eq("isLock", PickStationLockEnum.NO_LOCK.getLockTypeNumber()));
        List<PickStation> listPickStation= pickStationMapper.findByCriteria(pickStationCriteria);
        //过滤掉不能用的拣选站
        listPickStation=listPickStation.stream().filter(x->{
            String stationNo=x.getDeviceNo();
            AgvStorageLocation agvStorageLocation=agvStorageLocationMapper.findByPickCodeAndLock(stationNo, AgvLocationLocationLockEnum.NO_LOCK.getLockTypeNumber(), AgvLocationTaskLockEnum.NO_LOCK.getLockTypeNumber());
            if(null!=agvStorageLocation){

                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return listPickStation;
    }

    public AgvStorageLocation getPickStationAndLock(){
        List<PickStation> list=getAvailablePickStation();
        if(list.size()>0){
            String stationNo=list.get(0).getDeviceNo();
            AgvStorageLocation agvStorageLocation=agvStorageLocationMapper.findByPickCodeAndLock(stationNo,AgvLocationLocationLockEnum.NO_LOCK.getLockTypeNumber(),AgvLocationTaskLockEnum.NO_LOCK.getLockTypeNumber());
            agvStorageLocation.setLocationLock(1);
            agvStorageLocationMapper.update(agvStorageLocation);
            return agvStorageLocation;
        }
        return null;
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




    public void removeCompleteOrderAndUpdate(List<String> listBillNos,SimilarityDataEntityLoadInterface similarityDataEntityLoadInterface){
       HashSet<String> setList=new HashSet<String>();
       setList.addAll(listBillNos);
        for(String billNoString:setList) {
            boolean isComplete= outBoundTaskDetailMapper.isOrderComplete(billNoString);
            if(isComplete){
                billNoString=  String.format("'%s'", billNoString);
                outBoundTaskMapper.updateOutBoundTaskBySQL(billNoString);
                similarityDataEntityLoadInterface.getCrrentBillNoList().remove(billNoString);
            }
        }

    }



















}
