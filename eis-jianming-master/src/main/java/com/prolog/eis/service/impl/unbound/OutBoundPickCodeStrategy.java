package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.*;
import com.prolog.eis.dao.baseinfo.PortInfoMapper;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.service.enums.OutBoundEnum;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.framework.utils.MapUtils;
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
@Transactional(rollbackFor = Exception.class)
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

    @Override
    public void unbound(OutboundTask outboundTask) {

        SimilarityDataEntityLoadInterface similarityDataEntityListLoad = getsimilarityDataEntityListLoad(outboundTask);

        List<DetailDataBean> list = similarityDataEntityListLoad.getOutDetailList();


        for (DetailDataBean detailDataBeand : list) {
            ContainerTask ordercontainerTask = new ContainerTask();
            ordercontainerTask.setLotId(detailDataBeand.getLotId());
            ordercontainerTask.setCreateTime(new java.util.Date());
            ordercontainerTask.setOwnerId(detailDataBeand.getOwnerId());
            ordercontainerTask.setItemId(detailDataBeand.getItemId());
            ordercontainerTask.setItemName(detailDataBeand.getItemName());
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

            List<String> listBillNo = Arrays.asList(bill_no_String.split(","));
            List<String> listBillNoRemove = new ArrayList<>();
            for (String remove : listBillNo) {
                listBillNoRemove.add(String.format("'%s'", remove));
            }

            String pickCode = detailDataBeand.getPickCode();
            AgvStorageLocation agvStorageLocation = agvStorageLocationMapper.findByPickCodeAndLock(pickCode, 0, 0);
            agvStorageLocation.setLocationLock(1);
            agvStorageLocationMapper.update(agvStorageLocation);




            if (agvStorageLocation == null) {
                LogServices.logSysBusiness(pickCode + "拣选站点位已经锁定！");
                outboundTask.setTaskState(1);
                outBoundTaskMapper.save(outboundTask);
                //移除此缓存条目
                if (listBillNoRemove != null && listBillNoRemove.size() > 0)
                    similarityDataEntityListLoad.getCrrentBillNoList().removeAll(listBillNoRemove);
                return;
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
                        containerTaskMapper.save(ordercontainerTask);
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
                                if (((BigDecimal) sxStore1.get("qty")).floatValue() < outboundTaskDetail.getQty()) {
                                    containerTaskDetail.setQty(((BigDecimal) sxStore1.get("qty")).floatValue());
                                } else {
                                    containerTaskDetail.setQty(outboundTaskDetail.getQty());
                                }

                                containerTaskDetailMapperMapper.save(containerTaskDetail);
                            }
                        }

                        if (listBillNoRemove != null && listBillNoRemove.size() > 0) {
                            outBoundTaskMapper.updateOutBoundTaskBySQL(String.join(",", listBillNoRemove));
                        }
                        similarityDataEntityListLoad.getCrrentBillNoList().removeAll(listBillNoRemove);

                        if (last <= 0) break;
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
                        containerTaskMapper.save(ordercontainerTask);

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
                                if (((BigDecimal) sxStore1.get("qty")).floatValue() < outboundTaskDetail.getQty()) {
                                    containerTaskDetail.setQty(((BigDecimal) sxStore1.get("qty")).floatValue());
                                } else {
                                    containerTaskDetail.setQty(outboundTaskDetail.getQty());
                                }

                                containerTaskDetailMapperMapper.save(containerTaskDetail);
                            }
                        }


                        if (listBillNoRemove != null && listBillNoRemove.size() > 0) {
                            outBoundTaskMapper.updateOutBoundTaskBySQL(String.join(",", listBillNoRemove));
                        }
                        similarityDataEntityListLoad.getCrrentBillNoList().removeAll(listBillNoRemove);
                        break;
                    }

                }
            }


        }


    }


}
