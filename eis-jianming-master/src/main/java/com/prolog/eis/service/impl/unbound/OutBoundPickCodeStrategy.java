package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.*;
import com.prolog.eis.dao.baseinfo.PortInfoMapper;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.service.enums.OutBoundEnum;
import com.prolog.eis.service.sxk.SxStoreCkService;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 订单出库  指定拣选站
 */
@Component(OutBoundType.TASK_TYPE + 1 + OutBoundType.IF_SfReq + 1)
@Slf4j
@SuppressWarnings("all")
public class OutBoundPickCodeStrategy extends DefaultOutBoundPickCodeStrategy {

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
    @Transactional(rollbackFor = Exception.class)
    public void unbound(OutboundTask outboundTask) {

        SimilarityDataEntityLoadInterface similarityDataEntityListLoad = getsimilarityDataEntityListLoad(outboundTask);

        List<DetailDataBean> list = similarityDataEntityListLoad.getOutDetailList();

        List<String> listBillNo = new ArrayList<>();
        for (DetailDataBean detailDataBeand : list) {
            ContainerTask ordercontainerTask = new ContainerTask();
            ordercontainerTask.setLotId(detailDataBeand.getLotId());
            ordercontainerTask.setCreateTime(new java.util.Date());
            ordercontainerTask.setOwnerId(detailDataBeand.getOwnerId());
            ordercontainerTask.setItemId(detailDataBeand.getItemId());
            ordercontainerTask.setItemName(detailDataBeand.getItemName());
            ordercontainerTask.setLot(detailDataBeand.getLot());
            ordercontainerTask.setTaskType(1);
            ordercontainerTask.setSourceType(1);


            ordercontainerTask.setTargetType(OutBoundEnum.TargetType.AGV.getNumber()); //Agv目标区域
            float last = detailDataBeand.getLast();           //获取需要出库的总量

            Float countQty = qcSxStoreMapper.getSxStoreCount(detailDataBeand.getItemId(), detailDataBeand.getLotId(), detailDataBeand.getOwnerId());
            if (countQty == null) countQty = 0f;
            if (countQty < last) {
                LogServices.logSysBusiness("库存:" + countQty + "不够出:" + last + "！");
                return;
            }
            //更新任务锁

            String bill_no_String = detailDataBeand.getBillNo();

             listBillNo = Arrays.asList(bill_no_String.split(","));

            //pickstation给默认值
            String pickCode = StringUtils.isEmpty(detailDataBeand.getPickCode())?"pickStation4":detailDataBeand.getPickCode();
           /* List<PickStation> listPickStations=pickStationMapper.findByMap(MapUtils.put("deviceNo",pickCode).getMap(),PickStation.class);
            if(listPickStations.size()>1||listPickStations.size()<1){
                LogServices.logSysBusiness(pickCode + "拣选站已经锁定！");
                return;
            }*/
            AgvStorageLocation agvStorageLocation = agvStorageLocationMapper.findByPickCodeAndLock(pickCode, 0, 0);
            if (agvStorageLocation == null) {
                LogServices.logSysBusiness(pickCode + "拣选站点位已经锁定！");
                return;
            }else{
                agvStorageLocation.setLocationLock(1);
                agvStorageLocationMapper.update(agvStorageLocation);
            }
            List<Map<String, Object>> listSxStore = qcSxStoreMapper.getSxStoreByOrder(detailDataBeand.getItemId(), detailDataBeand.getLotId(), detailDataBeand.getOwnerId());
            for (Map<String, Object> sxStore1 : listSxStore) {
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
                         sxStoreCkService.buildSxCkTaskByContainerTask(ordercontainerTask);
                        //出明细
                        for (String billNo : listBillNo) {
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

                                if((outboundTaskDetail.getQty() - doubleCurrent)!=0){
                                containerTaskDetailMapperMapper.save(containerTaskDetail);}
                            }
                        }



                        if (last <= 0){ break;} else {continue;}
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
                        ordercontainerTask.setContainerCode((String) sxStore1.get("containerNo"));
                         sxStoreCkService.buildSxCkTaskByContainerTask(ordercontainerTask);
                        for (String billNo : listBillNo) {
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
                                containerTaskDetail.setCreateTime(new Date(System.currentTimeMillis()));
                                List< ContainerTaskDetail> listContainerTaskDetails=containerTaskDetailMapperMapper.findByMap(MapUtils.
                                        put("billNo",billNo)
                                        .put("itemId",detailDataBeand.getItemId()).put("ownerId",detailDataBeand.getOwnerId()
                                        ).put("lotId",detailDataBeand.getLotId()).getMap(),ContainerTaskDetail.class);
                                double doubleCurrent= listContainerTaskDetails.stream().mapToDouble(ContainerTaskDetail::getQty).sum();
                                if(((BigDecimal) sxStore1.get("qty")).floatValue()<(outboundTaskDetail.getQty()-doubleCurrent)){
                                    containerTaskDetail.setQty(((BigDecimal) sxStore1.get("qty")).floatValue());
                                }else{
                                    containerTaskDetail.setQty(outboundTaskDetail.getQty()-doubleCurrent);
                                }
                                if((outboundTaskDetail.getQty() - doubleCurrent)!=0){
                                containerTaskDetailMapperMapper.save(containerTaskDetail);}
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
