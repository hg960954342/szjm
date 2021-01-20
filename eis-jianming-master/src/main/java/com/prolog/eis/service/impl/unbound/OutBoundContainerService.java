package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.*;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.service.enums.*;
import com.prolog.eis.service.impl.unbound.entity.DetailDataBean;
import com.prolog.eis.service.sxk.SxStoreCkService;
import com.prolog.eis.util.ListHelper;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class OutBoundContainerService {

    @Autowired
    OutBoundTaskHistoryMapper outBoundTaskHistoryMapper;
    @Autowired
    OutBoundTaskMapper outBoundTaskMapper;
    @Autowired
    OutBoundTaskDetailMapper outBoundTaskDetailMapper;
    @Autowired
    OutBoundTaskDetailHistoryMapper outBoundTaskDetailHistoryMapper;
    @Autowired
    QcSxStoreMapper qcSxStoreMapper;

    @Autowired
    SxStoreCkService sxStoreCkService;
    @Autowired
    ContainerTaskDetailMapper containerTaskDetailMapperMapper;
    @Autowired
    AgvStorageLocationMapper agvStorageLocationMapper;
    @Autowired
    PickStationMapper pickStationMapper;

    /**
     * 将以完成的出库订单转入历史表中
     * @param outboundTask
     */
    public void deleteOutBoundAndInsertHistory(OutboundTask outboundTask) {
        OutboundTaskHistory outboundTaskHistory = new OutboundTaskHistory();
        outboundTask.setTaskState(1);
        BeanUtils.copyProperties(outboundTask,outboundTaskHistory);
        outBoundTaskHistoryMapper.save(outboundTaskHistory);
        outBoundTaskMapper.deleteById(outboundTask.getId(),OutboundTask.class);
    }


    /**
     * 将生成托盘任务的出库明细数据转入历史表中
     * @param outboundTaskDetail
     * @param containerTaskDetail
     */
    public void deleteDetailAndInsertHistory(OutboundTaskDetail outboundTaskDetail, ContainerTaskDetail containerTaskDetail) {
        outboundTaskDetail.setFinishQty((float) containerTaskDetail.getQty());
        outboundTaskDetail.setEndTime(new Date(System.currentTimeMillis()));
        OutboundTaskDetailHistory outboundTaskDetailHistory = new OutboundTaskDetailHistory();
        BeanUtils.copyProperties(outboundTaskDetail,outboundTaskDetailHistory);
        outBoundTaskDetailHistoryMapper.save(outboundTaskDetailHistory);
        outBoundTaskDetailMapper.deleteById(outboundTaskDetail.getId(),OutboundTaskDetail.class);
    }

    /**
     * 将库存中不为已上架状态的出库明细数据转入历史表中
     * @param outboundTaskDetail
     */
    public void deleteDetailAndInsertHistory(OutboundTaskDetail outboundTaskDetail) {
        outboundTaskDetail.setFinishQty(0);
        outboundTaskDetail.setEndTime(new Date(System.currentTimeMillis()));
        OutboundTaskDetailHistory outboundTaskDetailHistory = new OutboundTaskDetailHistory();
        BeanUtils.copyProperties(outboundTaskDetail,outboundTaskDetailHistory);
        outBoundTaskDetailHistoryMapper.save(outboundTaskDetailHistory);
        outBoundTaskDetailMapper.deleteById(outboundTaskDetail.getId(),OutboundTaskDetail.class);
    }


    /**OutboundTaskDetail删除转明细history
     * @param outboundTaskDetails
     */
    public void deleteBatchDetailAndInsertHistory(List<OutboundTaskDetail> outboundTaskDetails) {
       for(OutboundTaskDetail outboundTaskDetail:outboundTaskDetails){
           this.deleteDetailAndInsertHistory(outboundTaskDetail);
       }
    }




    /**
     * TODO 出库容器任务和明细生成
     * @param detailDataBeand 出库汇总实体
     * @param miniPackage 规格
     * @param isPickStation 是否指定拣选站出库 true指定 false不指定
     */
    public void buildContainerTaskAndDetails(DetailDataBean detailDataBeand,float miniPackage,boolean isPickStation){
        //TODO 需要出库的量
        float last = detailDataBeand.getLast();

        if(last==0){
            LogServices.logSysBusiness(String.format("订单:%s错误，出库数量为0!",detailDataBeand.getBillNo()));
            return;
        }
        if(last<0){
            LogServices.logSysBusiness(String.format("订单:%s待出库库存:%s计算出来错误!",detailDataBeand.getBillNo(),last));
            return;
        }
        //TODO 判断库存是否满足
        float countQty = qcSxStoreMapper.getSxStoreCount(detailDataBeand.getItemId(), detailDataBeand.getLotId(), detailDataBeand.getOwnerId());
        if (countQty < last) {
            LogServices.logSysBusiness(String.format("库存:%s，不够订单:%s 出的量:%s!",countQty,detailDataBeand.getBillNo(),last));
            return;
        }
        List<Map<String, Object>> bzClistSxStore =new ArrayList<>();
        //TODO 待出库的量大于最小包装数
        if(last-miniPackage>=0) {
            //TODO 取除数结果
            float z=(float)Math.rint(last/miniPackage);
            //TODO 取余数结果
            float y=last%miniPackage;
            //TODO 取需要正出的量
            float zc=z*miniPackage;
            List<Map<String, Object>> zClist = qcSxStoreMapper.getSxStoreByOrderByZC(detailDataBeand.getItemId(), detailDataBeand.getLotId(), detailDataBeand.getOwnerId(),miniPackage,zc);
            //TODO 添加过滤方法 过滤掉整的所有记录中之和超过出库量的记录
            float qtySum=0;
            List<Map<String,Object>> zClistSxStore=new ArrayList<>();
            for(Map<String,Object> x:zClist){
                float qty=((BigDecimal) x.get("qty")).floatValue();
                qtySum+=qty;
                if(qtySum<=last){
                    zClistSxStore.add(x);
                }
            }

            Set<String> containerNoSet=new HashSet<>();
            if(!zClistSxStore.isEmpty()){
                zClistSxStore.stream().parallel().forEach(x->{containerNoSet.add((String)x.get("containerNo"));});
                last=buildList(zClistSxStore,last,detailDataBeand,isPickStation);
            }
            if(y!=last){
                last=last+y;
            }
            bzClistSxStore = qcSxStoreMapper.getSxStoreByOrder(detailDataBeand.getItemId(), detailDataBeand.getLotId(), detailDataBeand.getOwnerId());
            bzClistSxStore=ListHelper.where(bzClistSxStore,x->{return !containerNoSet.contains(x.get("containerNo"));});
            //TODO 出整
            buildList(bzClistSxStore,last,detailDataBeand,isPickStation);
        }else{
            //TODO 小于包装数 只能出散
            bzClistSxStore=qcSxStoreMapper.getSxStoreByOrderByLC(detailDataBeand.getItemId(), detailDataBeand.getLotId(), detailDataBeand.getOwnerId(),miniPackage);
            last=buildList(bzClistSxStore,last,detailDataBeand,isPickStation);
            //TODO 零散未出完则拆整
            if(last>0){
                bzClistSxStore = qcSxStoreMapper.getSxStoreByOrder(detailDataBeand.getItemId(), detailDataBeand.getLotId(), detailDataBeand.getOwnerId());
                buildList(bzClistSxStore,last,detailDataBeand,isPickStation);
            }

        }


    }







    /**
     * TODO 批量生成容器任务
     * @param listStore
     * @param last
     * @param detailDataBeand
     * @param isPickStation
     */
    private float buildList(List<Map<String,Object>> listStore,float last,DetailDataBean detailDataBeand,boolean isPickStation){
        for(Map<String, Object> w:listStore){
            if (((BigDecimal) w.get("qty")).floatValue() <= last) {
                last = last - ((BigDecimal) w.get("qty")).floatValue();
                this.build(detailDataBeand,w,isPickStation);
                if (last <= 0){break;} else {continue;}
            }
            if (((BigDecimal) w.get("qty")).floatValue() > last) {
                last = ((BigDecimal) w.get("qty")).floatValue() - last;
                this.build(detailDataBeand,w,isPickStation);
                break;
            }
        }
        return last;
    }

    /**
     * TODO 生成容器任务
     * @param detailDataBeand 出库实体
     * @param sxStore1  库存数据
     * @param isPickStation 是否指定拣选站
     */
    private void build(DetailDataBean detailDataBeand,Map sxStore1,boolean isPickStation){
        String pickCode=null;
        if(isPickStation){
            //TODO 业务处理 指定拣选站出库 如果没传则 默认pickStation4
             pickCode = StringUtils.isEmpty(detailDataBeand.getPickCode())?"pickStation4":detailDataBeand.getPickCode();
        }
        AgvStorageLocation agvStorageLocation = this.getPickStationAndLock(pickCode);
        if (agvStorageLocation == null) {
            LogServices.logSysBusiness(String.format("拣选站%s被锁定！",pickCode));
            return;
        }
        ContainerTask ordercontainerTask = new ContainerTask();
        ordercontainerTask.setLotId(detailDataBeand.getLotId());
        ordercontainerTask.setCreateTime(new java.util.Date());
        ordercontainerTask.setOwnerId(detailDataBeand.getOwnerId());
        ordercontainerTask.setItemId(detailDataBeand.getItemId());
        ordercontainerTask.setItemName(detailDataBeand.getItemName());
        ordercontainerTask.setLot(detailDataBeand.getLot());
        ordercontainerTask.setTaskType(ContainerTaskTaskTypeEnum.ORDER_OUT_BOUND.getTaskType());
        ordercontainerTask.setSourceType(1);
        ordercontainerTask.setTargetType(OutBoundEnum.TargetType.AGV.getNumber()); //Agv目标区域
        String target = agvStorageLocation.getRcsPositionCode();
        ordercontainerTask.setTarget(target);
        ordercontainerTask.setQty(((BigDecimal) sxStore1.get("qty")).floatValue());
        String sourceLocation = PrologCoordinateUtils.splicingStr((Integer) sxStore1.get("x"), (Integer) sxStore1.get("y"), (Integer) sxStore1.get("layer"));
        ordercontainerTask.setSource(sourceLocation);
        ordercontainerTask.setTaskState(1);
        ordercontainerTask.setContainerCode((String) sxStore1.get("containerNo"));
        sxStoreCkService.buildSxCkTaskByContainerTask(ordercontainerTask);
        //TODO 写出库明细
        String bill_no_String = detailDataBeand.getBillNo();
        HashSet<String> setList=new HashSet<String>();
        setList.addAll(Arrays.asList(bill_no_String.split(",")));
        for (String billNo : setList) {
            List<OutboundTaskDetail> listOutBoundTaskDetailList = outBoundTaskDetailMapper.findByMap(MapUtils.
                    put("billNo", billNo)
                    .put("itemId", detailDataBeand.getItemId()).put("ownerId", detailDataBeand.getOwnerId()
                    ).put("lotId", detailDataBeand.getLotId()).getMap(), OutboundTaskDetail.class);
            for (OutboundTaskDetail outboundTaskDetail : listOutBoundTaskDetailList) {
                //TODO buildSxCkTaskByContainerTask方法未生成containerTask的taskCode 说明contanerTask没有生成 则跳过不保存此任务的明细
                if(StringUtils.isEmpty(ordercontainerTask.getTaskCode())){
                    break;
                }
                ContainerTaskDetail containerTaskDetail = new ContainerTaskDetail();
                BeanUtils.copyProperties(detailDataBeand, containerTaskDetail);
                containerTaskDetail.setBillNo(billNo);
                containerTaskDetail.setSeqNo(outboundTaskDetail.getSeqNo());
                containerTaskDetail.setContainerCode((String) sxStore1.get("containerNo"));
                containerTaskDetail.setCreateTime(new java.util.Date());
                List< ContainerTaskDetail> listContainerTaskDetails=containerTaskDetailMapperMapper.findByMap(MapUtils.
                        put("billNo",billNo)
                        .put("itemId",detailDataBeand.getItemId()).put("ownerId",detailDataBeand.getOwnerId()
                        ).put("lotId",detailDataBeand.getLotId()).getMap(),ContainerTaskDetail.class);
                double doubleCurrent= listContainerTaskDetails.stream().mapToDouble(ContainerTaskDetail::getQty).sum();
                if(((BigDecimal) sxStore1.get("qty")).floatValue()<(outboundTaskDetail.getQty()-doubleCurrent)){
                    containerTaskDetail.setQty(((BigDecimal) sxStore1.get("qty")).floatValue());
                }else{
                    containerTaskDetail.setQty((outboundTaskDetail.getQty()-doubleCurrent));
                }

                if((outboundTaskDetail.getQty() - doubleCurrent)>0){
                    containerTaskDetailMapperMapper.save(containerTaskDetail);}


                outboundTaskDetail.setFinishQty(outboundTaskDetail.getFinishQty()+(float) containerTaskDetail.getQty());
                outBoundTaskDetailMapper.update(outboundTaskDetail);
            }
        }
    }

    /**
     * 获取拣选站不判断是否可用
     * @return
     */
    public AgvStorageLocation getPickStationAndLock(){
        return this.getPickStationAndLock(null);
    }

    /**
     * TODO 获取拣选站 指定拣选站code
     * @param pickCode
     * @return
     */
    public AgvStorageLocation getPickStationAndLock(String pickCode){
        if(!StringUtils.isEmpty(pickCode)){
            AgvStorageLocation agvStorageLocation = agvStorageLocationMapper.findByPickCodeAndLock(pickCode, AgvLocationLocationLockEnum.NO_LOCK.getLockTypeNumber(), AgvLocationTaskLockEnum.NO_LOCK.getLockTypeNumber());
            if (agvStorageLocation == null) {
                return null;
            }else{
                agvStorageLocation.setLocationLock(1);
                agvStorageLocationMapper.update(agvStorageLocation);
                return agvStorageLocation;
            }
        }
        List<PickStation> list=getAvailablePickStation();
        if(list.size()>0){
            String stationNo=list.get(0).getDeviceNo();
            AgvStorageLocation agvStorageLocation=agvStorageLocationMapper.findByPickCodeAndLock(stationNo, AgvLocationLocationLockEnum.NO_LOCK.getLockTypeNumber(), AgvLocationTaskLockEnum.NO_LOCK.getLockTypeNumber());
            agvStorageLocation.setLocationLock(1);
            agvStorageLocationMapper.update(agvStorageLocation);
            return agvStorageLocation;
        }
        return null;
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
}
