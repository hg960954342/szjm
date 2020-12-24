package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.*;
import com.prolog.eis.dao.baseinfo.PortInfoMapper;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.service.enums.ContainerTaskTaskTypeEnum;
import com.prolog.eis.service.enums.OutBoundEnum;
import com.prolog.eis.service.enums.OutBoundType;
import com.prolog.eis.service.impl.unbound.entity.DetailDataBean;
import com.prolog.eis.service.sxk.SxStoreCkService;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.framework.utils.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;

/**
 * 订单出库 未指定拣选站
 */
@Component(OutBoundType.TASK_TYPE + 1 + OutBoundType.IF_SF_REQ + 0)
@Slf4j
@SuppressWarnings("all")
public class OrderBoundStrategy extends DefaultOutBoundPickCodeStrategy {

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
    SxStoreCkService sxStoreCkService;


    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 1000)
    public void unbound(OutboundTask outboundTask) {

        List<OutboundTask> listCheckOuts=outBoundTaskMapper.findByMap(MapUtils.put("taskType",OutBoundEnum.TaskType.ORDER_CHECK_OUT_BOUND.getTaskTypeNumber()).getMap(),OutboundTask.class);
        if(listCheckOuts.size()>0){
            //存在盘点任务
            LogServices.logSysBusiness("盘点任务优先！");
            return;
        }
        List<PickStation> listPickStations = getAvailablePickStation();
        if (listPickStations.size() < 1) {
            LogServices.logSysBusiness("无可用拣选站");
            return;
        }
        SimilarityDataEntityLoadInterface similarityDataEntityListLoad = getsimilarityDataEntityListLoad(outboundTask);
        List<DetailDataBean> list = similarityDataEntityListLoad.getOutDetailList();

        List<String> listBillNo = new ArrayList<>();
        for (DetailDataBean detailDataBeand : list) {
            ContainerTask ordercontainerTask = new ContainerTask();
            ordercontainerTask.setLotId(detailDataBeand.getLotId());
            ordercontainerTask.setCreateTime(new Date(System.currentTimeMillis()));
            ordercontainerTask.setOwnerId(detailDataBeand.getOwnerId());
            ordercontainerTask.setItemId(detailDataBeand.getItemId());
            ordercontainerTask.setItemName(detailDataBeand.getItemName());
            ordercontainerTask.setLot(detailDataBeand.getLot());
            ordercontainerTask.setTaskType(ContainerTaskTaskTypeEnum.ORDER_OUT_BOUND.getTaskType());
            ordercontainerTask.setSourceType(1);


            ordercontainerTask.setTargetType(OutBoundEnum.TargetType.AGV.getNumber()); //Agv目标区域
            float last = detailDataBeand.getLast();           //获取需要出库的总量

            Float countQty = qcSxStoreMapper.getSxStoreCount(detailDataBeand.getItemId(), detailDataBeand.getLotId(), detailDataBeand.getOwnerId());
            if (countQty == null) {countQty = 0f;}
            if (countQty < last) {
                LogServices.logSysBusiness("库存:" + countQty + "不够出:" + last + "！");
                return;
            }

            List<Map<String, Object>> listSxStore = qcSxStoreMapper.getSxStoreByOrder(detailDataBeand.getItemId(), detailDataBeand.getLotId(), detailDataBeand.getOwnerId());
            String bill_no_String = detailDataBeand.getBillNo();
            listBillNo .addAll(Arrays.asList(bill_no_String.split(",")));
            HashSet<String> setList=new HashSet<String>();
            setList.addAll(listBillNo);

            for (Map<String, Object> sxStore1 : listSxStore) {
                AgvStorageLocation agvStorageLocation = this.getPickStationAndLock();
                if (agvStorageLocation == null) {
                    LogServices.logSysBusiness("无可用拣选站");
                    return;
                }
                int LocationType = agvStorageLocation.getLocationType();
                if (!this.isExistTask(agvStorageLocation.getRcsPositionCode())) {
                    if (((BigDecimal) sxStore1.get("qty")).floatValue() <= last && (LocationType == 3 || LocationType == 5)) { //出整托
                        //出整托
                        last = last - ((BigDecimal) sxStore1.get("qty")).floatValue();
                        String target = agvStorageLocation.getRcsPositionCode();
                        ordercontainerTask.setTarget(target);
                        ordercontainerTask.setQty(((BigDecimal) sxStore1.get("qty")).floatValue());
                        String sourceLocation = PrologCoordinateUtils.splicingStr((Integer) sxStore1.get("x"), (Integer) sxStore1.get("y"), (Integer) sxStore1.get("layer"));
                        ordercontainerTask.setSource(sourceLocation);
                        ordercontainerTask.setTaskState(1);
                        ordercontainerTask.setContainerCode((String) sxStore1.get("containerNo"));
                        ordercontainerTask.setTaskCode(null);
                        sxStoreCkService.buildSxCkTaskByContainerTask(ordercontainerTask);
                        //出明细
                        for (String billNo : setList) {
                            List<OutboundTaskDetail> listOutBoundTaskDetailList = outBoundTaskDetailMapper.findByMap(MapUtils.
                                    put("billNo", billNo)
                                    .put("itemId", detailDataBeand.getItemId()).put("ownerId", detailDataBeand.getOwnerId()
                                    ).put("lotId", detailDataBeand.getLotId()).getMap(), OutboundTaskDetail.class);
                            for (OutboundTaskDetail outboundTaskDetail : listOutBoundTaskDetailList) {
                                ContainerTaskDetail containerTaskDetail = new ContainerTaskDetail();
                                BeanUtils.copyProperties(detailDataBeand, containerTaskDetail);
                                containerTaskDetail.setBillNo(billNo);
                                containerTaskDetail.setSeqNo(outboundTaskDetail.getSeqNo());
                                containerTaskDetail.setContainerCode((String) sxStore1.get("containerNo"));
                                containerTaskDetail.setCreateTime(new java.util.Date());
                                List<ContainerTaskDetail> listContainerTaskDetails = containerTaskDetailMapperMapper.findByMap(MapUtils.
                                        put("billNo", billNo)
                                        .put("itemId", detailDataBeand.getItemId()).put("ownerId", detailDataBeand.getOwnerId()
                                        ).put("lotId", detailDataBeand.getLotId()).getMap(), ContainerTaskDetail.class);
                                double doubleCurrent = listContainerTaskDetails.stream().mapToDouble(ContainerTaskDetail::getQty).sum();
                                if (((BigDecimal) sxStore1.get("qty")).floatValue() < (outboundTaskDetail.getQty() - doubleCurrent)) {
                                    containerTaskDetail.setQty(((BigDecimal) sxStore1.get("qty")).floatValue());
                                } else {
                                    containerTaskDetail.setQty((outboundTaskDetail.getQty() - doubleCurrent));
                                }
                              if((outboundTaskDetail.getQty() - doubleCurrent)>0){
                                   containerTaskDetailMapperMapper.save(containerTaskDetail);
                               }

                                outboundTaskDetail.setFinishQty(outboundTaskDetail.getFinishQty()+(float) containerTaskDetail.getQty());
                                outBoundTaskDetailMapper.update(outboundTaskDetail);
                            }

                        }


                        if (last <= 0) {
                            break;
                        } else {
                            continue;
                        }
                    }
                    if (((BigDecimal) sxStore1.get("qty")).floatValue() > last && (LocationType == 4 || LocationType == 5)) { //非整托
                        //非整托
                        last = ((BigDecimal) sxStore1.get("qty")).floatValue() - last;
                        String target = agvStorageLocation.getRcsPositionCode();
                        ordercontainerTask.setTarget(target);
                        ordercontainerTask.setQty(((BigDecimal) sxStore1.get("qty")).floatValue());
                        String sourceLocation = PrologCoordinateUtils.splicingStr((Integer) sxStore1.get("x"), (Integer) sxStore1.get("y"), (Integer) sxStore1.get("layer"));
                        ordercontainerTask.setSource(sourceLocation);
                        ordercontainerTask.setTaskState(1);
                        ordercontainerTask.setTaskCode(null);
                        ordercontainerTask.setContainerCode((String) sxStore1.get("containerNo"));
                        sxStoreCkService.buildSxCkTaskByContainerTask(ordercontainerTask);
                        for (String billNo : setList) {
                            List<OutboundTaskDetail> listOutBoundTaskDetailList = outBoundTaskDetailMapper.findByMap(MapUtils.
                                    put("billNo", billNo)
                                    .put("itemId", detailDataBeand.getItemId()).put("ownerId", detailDataBeand.getOwnerId()
                                    ).put("lotId", detailDataBeand.getLotId()).getMap(), OutboundTaskDetail.class);
                            for (OutboundTaskDetail outboundTaskDetail : listOutBoundTaskDetailList) {
                                float finishQty=0;
                                ContainerTaskDetail containerTaskDetail = new ContainerTaskDetail();
                                BeanUtils.copyProperties(detailDataBeand, containerTaskDetail);
                                containerTaskDetail.setBillNo(billNo);
                                containerTaskDetail.setSeqNo(outboundTaskDetail.getSeqNo());
                                containerTaskDetail.setContainerCode((String) sxStore1.get("containerNo"));
                                containerTaskDetail.setCreateTime(new Date(System.currentTimeMillis()));
                                List<ContainerTaskDetail> listContainerTaskDetails = containerTaskDetailMapperMapper.findByMap(MapUtils.
                                        put("billNo", billNo)
                                        .put("itemId", detailDataBeand.getItemId()).put("ownerId", detailDataBeand.getOwnerId()
                                        ).put("lotId", detailDataBeand.getLotId()).getMap(), ContainerTaskDetail.class);
                                double doubleCurrent = listContainerTaskDetails.stream().mapToDouble(ContainerTaskDetail::getQty).sum();
                                if (((BigDecimal) sxStore1.get("qty")).floatValue() < (outboundTaskDetail.getQty() - doubleCurrent)) {
                                    containerTaskDetail.setQty(((BigDecimal) sxStore1.get("qty")).floatValue());

                                } else {
                                    containerTaskDetail.setQty(outboundTaskDetail.getQty() - doubleCurrent);
                                }
                                if((outboundTaskDetail.getQty() - doubleCurrent)>0){
                                containerTaskDetailMapperMapper.save(containerTaskDetail);
                                }

                                outboundTaskDetail.setFinishQty(outboundTaskDetail.getFinishQty()+(float) containerTaskDetail.getQty());
                                outBoundTaskDetailMapper.update(outboundTaskDetail);


                            }





                        }

                               break;
                    }

                }
            }


        }

        removeCompleteOrderAndUpdate(listBillNo, similarityDataEntityListLoad);
    }



}