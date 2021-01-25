package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.*;
import com.prolog.eis.dto.sxk.OutBoundSxStoreDto;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.service.enums.*;
import com.prolog.eis.service.impl.unbound.entity.DetailDataBean;
import com.prolog.eis.service.impl.unbound.handler.OutBoundSxStoreHandler;
import com.prolog.eis.service.sxk.SxStoreCkService;
import com.prolog.eis.util.ListHelper;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;
import java.util.function.Function;
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
    @Autowired
    ContainerTaskMapper containerTaskMapper;

    /**
     * 将以完成的出库订单转入历史表中
     *
     * @param outboundTask
     */
    public void deleteOutBoundAndInsertHistory(OutboundTask outboundTask) {
        OutboundTaskHistory outboundTaskHistory = new OutboundTaskHistory();
        outboundTask.setTaskState(1);
        BeanUtils.copyProperties(outboundTask, outboundTaskHistory);
        outBoundTaskHistoryMapper.save(outboundTaskHistory);
        outBoundTaskMapper.deleteById(outboundTask.getId(), OutboundTask.class);
    }


    /**
     * 将生成托盘任务的出库明细数据转入历史表中
     *
     * @param outboundTaskDetail
     * @param containerTaskDetail
     */
    public void deleteDetailAndInsertHistory(OutboundTaskDetail outboundTaskDetail, ContainerTaskDetail containerTaskDetail) {
        outboundTaskDetail.setFinishQty((float) containerTaskDetail.getQty());
        outboundTaskDetail.setEndTime(new Date(System.currentTimeMillis()));
        OutboundTaskDetailHistory outboundTaskDetailHistory = new OutboundTaskDetailHistory();
        BeanUtils.copyProperties(outboundTaskDetail, outboundTaskDetailHistory);
        outBoundTaskDetailHistoryMapper.save(outboundTaskDetailHistory);
        outBoundTaskDetailMapper.deleteById(outboundTaskDetail.getId(), OutboundTaskDetail.class);
    }

    /**
     * 将库存中不为已上架状态的出库明细数据转入历史表中
     *
     * @param outboundTaskDetail
     */
    public void deleteDetailAndInsertHistory(OutboundTaskDetail outboundTaskDetail) {
        outboundTaskDetail.setFinishQty(0);
        outboundTaskDetail.setEndTime(new Date(System.currentTimeMillis()));
        OutboundTaskDetailHistory outboundTaskDetailHistory = new OutboundTaskDetailHistory();
        BeanUtils.copyProperties(outboundTaskDetail, outboundTaskDetailHistory);
        outBoundTaskDetailHistoryMapper.save(outboundTaskDetailHistory);
        outBoundTaskDetailMapper.deleteById(outboundTaskDetail.getId(), OutboundTaskDetail.class);
    }


    /**
     * OutboundTaskDetail删除转明细history
     *
     * @param outboundTaskDetails
     */
    public void deleteBatchDetailAndInsertHistory(List<OutboundTaskDetail> outboundTaskDetails) {
        for (OutboundTaskDetail outboundTaskDetail : outboundTaskDetails) {
            this.deleteDetailAndInsertHistory(outboundTaskDetail);
        }
    }


    public void buildContainerTaskAndDetails(DetailDataBean detailDataBeand, float miniPackage, boolean isPickStation) {
        //TODO 需要出库的量
        float last = detailDataBeand.getLast();
        //TODO 需要出库的总量
        float allLast=last;

        if (last == 0) {
            LogServices.logSysBusiness(String.format("订单:%s错误，出库数量为0!", detailDataBeand.getBillNo()));
            return;
        }
        if (last < 0) {
            LogServices.logSysBusiness(String.format("订单:%s待出库库存:%s计算出来错误!", detailDataBeand.getBillNo(), last));
            return;
        }
        //TODO 判断库存是否满足
        float countQty = qcSxStoreMapper.getSxStoreCount(detailDataBeand.getItemId(), detailDataBeand.getLotId(), detailDataBeand.getOwnerId());
        if (countQty < last) {
            LogServices.logSysBusiness(String.format("库存:%s，不够订单:%s 出的量:%s!", countQty, detailDataBeand.getBillNo(), last));
            return;
        }


        float lqty=last%miniPackage;
        float zqty = last-lqty;
        OutBoundSxStoreHandler handler = new OutBoundSxStoreHandler();
        qcSxStoreMapper.getSxStoreByOrderEntity(detailDataBeand.getItemId(), detailDataBeand.getLotId(), detailDataBeand.getOwnerId(), miniPackage, handler);
        List<OutBoundSxStoreDto> list = handler.getList();
        List<OutBoundSxStoreDto> zList= ListHelper.where(list, x -> x.getLqty()== 0);
        list = ListHelper.where(list, x -> x.getLqty() != 0);
        //TODO 获取零散的全部总量
        float sumLqty = (float) list.parallelStream().mapToDouble(OutBoundSxStoreDto::getLqty).sum();
        //TODO 零散能出全部
        if (last <= sumLqty) {
             computeL(list, last);
        } else {
            if(last<=miniPackage){
                //TODO 零散出不了全部 则先出要出库的零散 lqty 在整出zqty
                last=computeL(list, lqty);
                //TODO 优先出半零散
                if(last==0){
                    List<OutBoundSxStoreDto> listx=ListHelper.where(list,x->x.getZqty()!=0);
                    list.removeAll(listx);
                    zList=ListUtils.union(listx,zList);
                    computeZ(zList, zqty);
                }else{
                    last=computeZ(list, last+zqty);
                    //TODO 出整
                    computeZ(zList, last);
                }
            }else{
                //TODO 零散可能出不完
                float y=computeL(list, lqty);
                List<OutBoundSxStoreDto> listx=ListHelper.where(list,x->x.getZqty()!=0);
                 list.removeAll(listx);
                 zList=ListUtils.union(listx,zList);
                 computeZ(zList, listx.isEmpty()?zqty+y:last-y);

            }



        }
        //TODO 出库校验
        List<OutBoundSxStoreDto> resultList=new ArrayList<>();

        resultList.addAll(zList);
        resultList.addAll(list);

        float sumQty = (float) resultList.parallelStream().mapToDouble(OutBoundSxStoreDto::getOutQty).sum();
        if(sumQty<=allLast){
            for(OutBoundSxStoreDto outBoundSxStoreDto:resultList){
                if(outBoundSxStoreDto.getOutQty()!=0){
                    this.build(detailDataBeand, outBoundSxStoreDto, outBoundSxStoreDto.getOutQty(), isPickStation);
                }
            }
        }



    }

    //TODO 计算零散
    private float computeL(List<OutBoundSxStoreDto> list, float last) {
        for (OutBoundSxStoreDto outBoundSxStoreDto : list) {
            if (outBoundSxStoreDto.getLqty()!=0&&outBoundSxStoreDto.getLqty() <= last) {
                outBoundSxStoreDto.setOutQty(outBoundSxStoreDto.getOutQty()+outBoundSxStoreDto.getLqty());
                last = last - outBoundSxStoreDto.getOutQty();
                if (last <= 0) {
                    break;
                } else {
                    continue;
                }
            }
            if (outBoundSxStoreDto.getLqty()!=0&&outBoundSxStoreDto.getLqty() > last) {
                float outQty=last;
                outBoundSxStoreDto.setOutQty(outBoundSxStoreDto.getOutQty()+outQty);
                last = last - outBoundSxStoreDto.getOutQty();
                break;
            }
        }
        return last;
    }
   //TODO 计算整包
    private float computeZ(List<OutBoundSxStoreDto> list,float last) {
        for (OutBoundSxStoreDto outBoundSxStoreDto : list) {
            if (outBoundSxStoreDto.getZqty()!=0&&outBoundSxStoreDto.getZqty() <= last) {
                outBoundSxStoreDto.setOutQty(outBoundSxStoreDto.getOutQty()+outBoundSxStoreDto.getZqty());
                last = last - outBoundSxStoreDto.getOutQty();
                if (last <= 0) {
                    break;
                } else {
                    continue;
                }
            }
            if (outBoundSxStoreDto.getZqty()!=0&&outBoundSxStoreDto.getZqty() > last) {
                float outQty=last;
                outBoundSxStoreDto.setOutQty(outBoundSxStoreDto.getOutQty()+outQty);
                last = last - outBoundSxStoreDto.getOutQty();
                break;
            }
        }
        return last;
    }

    /**
     * TODO 生成容器任务
     *
     * @param detailDataBeand 出库实体
     * @param sxStore1        库存数据
     * @param isPickStation   是否指定拣选站
     */
    private void build(DetailDataBean detailDataBeand, OutBoundSxStoreDto sxStore1, float outBoundQty, boolean isPickStation) {
        String pickCode = null;
        if (isPickStation) {
            //TODO 业务处理 指定拣选站出库 如果没传则 默认pickStation4
            pickCode = StringUtils.isEmpty(detailDataBeand.getPickCode()) ? "pickStation4" : detailDataBeand.getPickCode();
        }
        AgvStorageLocation agvStorageLocation = this.getPickStationAndLock(pickCode);
        if (agvStorageLocation == null) {
            LogServices.logSysBusiness(String.format("拣选站%s被锁定！", pickCode));
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
        ordercontainerTask.setQty(sxStore1.getQty());
        ordercontainerTask.setSource(sxStore1.getStoreNo());
        ordercontainerTask.setTaskState(1);
        ordercontainerTask.setContainerCode(sxStore1.getContainerNo());
        //sxStoreCkService.buildSxCkTaskByContainerTask(ordercontainerTask);
        containerTaskMapper.save(ordercontainerTask);
        //TODO 写出库明细
        String bill_no_String = detailDataBeand.getBillNo();
        HashSet<String> setList = new HashSet<String>();
        setList.addAll(Arrays.asList(bill_no_String.split(",")));

        for (String billNo : setList) {
            //TODO buildSxCkTaskByContainerTask方法未生成containerTask的taskCode 说明contanerTask没有生成 则跳过不保存此任务的明细
            /*if (StringUtils.isEmpty(ordercontainerTask.getTaskCode())) {
                break;
            }*/
                ContainerTaskDetail containerTaskDetail=new ContainerTaskDetail();
                BeanUtils.copyProperties(detailDataBeand, containerTaskDetail);
                containerTaskDetail.setBillNo(billNo);
                //TODO 设置SeqNo序号
              OutboundTaskDetail outboundTaskDetailTemp=outBoundTaskDetailMapper.findFirstByMap(MapUtils.
                    put("billNo", billNo)
                    .put("itemId", detailDataBeand.getItemId()).put("ownerId", detailDataBeand.getOwnerId()
                    ).put("lotId", detailDataBeand.getLotId()).getMap(), OutboundTaskDetail.class);
                containerTaskDetail.setSeqNo(outboundTaskDetailTemp.getSeqNo());

                containerTaskDetail.setContainerCode(sxStore1.getContainerNo());
                containerTaskDetail.setCreateTime(new java.util.Date());
                containerTaskDetail.setQty(outBoundQty);
                containerTaskDetailMapperMapper.save(containerTaskDetail);
            List<OutboundTaskDetail> listOutBoundTaskDetailList = outBoundTaskDetailMapper.findByMap(MapUtils.
                    put("billNo", billNo)
                    .put("itemId", detailDataBeand.getItemId()).put("ownerId", detailDataBeand.getOwnerId()
                    ).put("lotId", detailDataBeand.getLotId()).getMap(), OutboundTaskDetail.class);
            for (OutboundTaskDetail outboundTaskDetail : listOutBoundTaskDetailList) {
                outboundTaskDetail.setFinishQty(outboundTaskDetail.getFinishQty() + (float) containerTaskDetail.getQty());
                outBoundTaskDetailMapper.update(outboundTaskDetail);
            }

        }

        sxStore1.setOutOrNo(true);
    }


    /**
     * 获取拣选站不判断是否可用
     *
     * @return
     */
    public AgvStorageLocation getPickStationAndLock() {
        return this.getPickStationAndLock(null);
    }

    /**
     * TODO 获取拣选站 指定拣选站code
     *
     * @param pickCode
     * @return
     */
    public AgvStorageLocation getPickStationAndLock(String pickCode) {
        if (!StringUtils.isEmpty(pickCode)) {
            AgvStorageLocation agvStorageLocation = agvStorageLocationMapper.findByPickCodeAndLock(pickCode, AgvLocationLocationLockEnum.NO_LOCK.getLockTypeNumber(), AgvLocationTaskLockEnum.NO_LOCK.getLockTypeNumber());
            if (agvStorageLocation == null) {
                return null;
            } else {
                agvStorageLocation.setLocationLock(1);
                agvStorageLocationMapper.update(agvStorageLocation);
                return agvStorageLocation;
            }
        }
        List<PickStation> list = getAvailablePickStation();
        if (list.size() > 0) {
            String stationNo = list.get(0).getDeviceNo();
            AgvStorageLocation agvStorageLocation = agvStorageLocationMapper.findByPickCodeAndLock(stationNo, AgvLocationLocationLockEnum.NO_LOCK.getLockTypeNumber(), AgvLocationTaskLockEnum.NO_LOCK.getLockTypeNumber());
            agvStorageLocation.setLocationLock(1);
            agvStorageLocationMapper.update(agvStorageLocation);
            return agvStorageLocation;
        }
        return null;
    }

    /**
     * 获取所有能够作业的拣选站
     *
     * @return
     */
    public List<PickStation> getAvailablePickStation() {
        //获取所有能用的拣选站
        Criteria pickStationCriteria = Criteria.forClass(PickStation.class);
        Integer[] ios = new Integer[]{PickStationIOTypeEnum.IN.getIoType(), PickStationIOTypeEnum.IN_OUT.getIoType()};
        Integer[] taskTypes = new Integer[]{
                PickStationTaskTypeEnum.ORDER_TASK_TYPE.getTaskType(),
                PickStationTaskTypeEnum.ORDER_AND_MOVE_TASK_TYPE.getTaskType(),
                PickStationTaskTypeEnum.ORDER_AND_EMPTY_TASK_TYPE.getTaskType(),
                PickStationTaskTypeEnum.ALL_TASK_TYPE.getTaskType()
        };
        pickStationCriteria.setRestriction(Restrictions.in("io", ios));
        pickStationCriteria.setRestriction(Restrictions.in("taskType", taskTypes));
        pickStationCriteria.setRestriction(Restrictions.eq("isLock", PickStationLockEnum.NO_LOCK.getLockTypeNumber()));
        List<PickStation> listPickStation = pickStationMapper.findByCriteria(pickStationCriteria);
        //过滤掉不能用的拣选站
        listPickStation = listPickStation.stream().filter(x -> {
            String stationNo = x.getDeviceNo();
            AgvStorageLocation agvStorageLocation = agvStorageLocationMapper.findByPickCodeAndLock(stationNo, AgvLocationLocationLockEnum.NO_LOCK.getLockTypeNumber(), AgvLocationTaskLockEnum.NO_LOCK.getLockTypeNumber());
            if (null != agvStorageLocation) {

                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return listPickStation;
    }

}
