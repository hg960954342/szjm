package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.*;
import com.prolog.eis.dao.baseinfo.PortInfoMapper;
import com.prolog.eis.model.eis.PortInfo;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.service.enums.OutBoundEnum;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

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
    SimilarityDataEntityListLoad similarityDataEntityListLoad;

    @Autowired
    QcSxStoreMapper qcSxStoreMapper;

    @Autowired
    PortInfoMapper portInfoMapper;






    @Autowired
    private  Map<String, DefaultOutBoundPickCodeStrategy> strategyMap  ;

    public DefaultOutBoundPickCodeStrategy getDefaultOutBoundPickCodeStrategy(OutboundTask OutboundTask){
        int isPickCode=OutboundTask.getSfReq();
        return strategyMap.get(OutBoundType.TASK_TYPE+OutboundTask.getTaskType()+OutBoundType.IF_SfReq+isPickCode);
    }

    @Override
    public void unbound(OutboundTask outboundTask) {
        DefaultOutBoundPickCodeStrategy defaultOutBoundPickCodeStrategy=this.getDefaultOutBoundPickCodeStrategy(outboundTask);
        if(null!=defaultOutBoundPickCodeStrategy){
            log.info(defaultOutBoundPickCodeStrategy.getClass().getName());
            similarityDataEntityListLoad.addOutboundTask(outboundTask);
            if(similarityDataEntityListLoad.currentBillNoList.size()==similarityDataEntityListLoad.maxSize)
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
        //过滤掉不能用的拣选站
        listPickStation=listPickStation.stream().filter(x->{
            String stationNo=x.getStationNo();
            AgvStorageLocation agvStorageLocation=agvStorageLocationMapper.findByPickCodeAndLock(stationNo,0,0);
            if(null!=agvStorageLocation){

                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return listPickStation;
    }

    /**
     * 判断目标点位是否有任务
     * @param
     * @return boolean
     */
    public boolean isExistTask(String source){
        List<ContainerTask> list=containerTaskMapper.selectBySource(source);
        if(list!=null&&list.size()>0) return true;
        return false;
    }

    protected void unbound(DetailDataBean detailDataBeand,String pickCode){

        AgvStorageLocation agvStorageLocation = agvStorageLocationMapper.findByPickCodeAndLock(pickCode, 0, 0);



        ContainerTask ordercontainerTask = new ContainerTask();
        ordercontainerTask.setLotId(detailDataBeand.getLotId());
        ordercontainerTask.setCreateTime(new Date(System.currentTimeMillis()));
        ordercontainerTask.setOwnerId(detailDataBeand.getOwnerId());
        ordercontainerTask.setItemId(detailDataBeand.getItemId());
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        ordercontainerTask.setTaskCode(uuid);
        ordercontainerTask.setTaskType(1);
        ordercontainerTask.setSourceType(1);

        String target=agvStorageLocation.getRcsPositionCode();
        ordercontainerTask.setTarget(target);
        ordercontainerTask.setTargetType(OutBoundEnum.TargetType.AGV.getNumber()); //Agv目标区域
        float last = detailDataBeand.getLast();           //获取需要出库的总量
        ordercontainerTask.setQty(last);
        List<Map<String, Object>> listSxStore = qcSxStoreMapper.getSxStoreByOrder(detailDataBeand.getItemId(), detailDataBeand.getLotId(), detailDataBeand.getOwnerId());
        if (listSxStore.size() < 1) {log.info("未找到库存！"); return; }    //没有库存结束
        listSxStore=  listSxStore.stream().filter(x -> {
            BigDecimal qty = (BigDecimal) x.get("qty");
            if (qty!=null&&qty.floatValue() >= last) return true;
            return false;
        }).collect(Collectors.toList());
        if(listSxStore.size()==0)  {log.info("库存不够！"); return;}
        Map<String, Object> sxStore1 = listSxStore.stream().min(Comparator.comparingInt(entry -> {
            Object objectDeptNum= entry.get("deptNum");
            if(objectDeptNum!=null){
                return  (Integer)entry.get("deptNum");
            }
            return 0;
        })).get();
        String sourceLocation= PrologCoordinateUtils.splicingStr((Integer) sxStore1.get("x"),(Integer) sxStore1.get("y"),(Integer) sxStore1.get("layer"));
        ordercontainerTask.setSource(sourceLocation);
        ordercontainerTask.setContainerCode((String)sxStore1.get("containerNo"));
        ordercontainerTask.setTaskState(1);
        int LocationType = agvStorageLocation.getLocationType();
        if(((BigDecimal) sxStore1.get("qty")).floatValue()==last&&(LocationType==3 ||LocationType==5 )&&!this.isExistTask(target)){ //出整托
            containerTaskMapper.save(ordercontainerTask);
            List<ContainerTaskDetail> listContainerTaskDetail=outBoundTaskDetailMapper.
                    getOutBoundContainerTaskDetail("'"+detailDataBeand.getBillNo()+"'",ordercontainerTask.getContainerCode());
            containerTaskDetailMapperMapper.saveBatch(listContainerTaskDetail);
            outBoundTaskMapper.updateOutBoundTaskBySQL("'"+detailDataBeand.getBillNo()+"'");


        }
        if(((BigDecimal) sxStore1.get("qty")).floatValue()>=last&&(LocationType==4 ||LocationType==5 )&&!this.isExistTask(target)){ //非整托
            containerTaskMapper.save(ordercontainerTask);
            List<ContainerTaskDetail> listContainerTaskDetail=outBoundTaskDetailMapper.getOutBoundContainerTaskDetail
                    ("'"+detailDataBeand.getBillNo()+"'",ordercontainerTask.getContainerCode());
            containerTaskDetailMapperMapper.saveBatch(listContainerTaskDetail);

            outBoundTaskMapper.updateOutBoundTaskBySQL("'"+detailDataBeand.getBillNo()+"'");
        }


    }
}
