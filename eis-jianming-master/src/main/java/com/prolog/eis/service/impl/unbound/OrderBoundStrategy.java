package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.*;
import com.prolog.eis.dao.baseinfo.PortInfoMapper;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.service.enums.OutBoundEnum;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.eis.util.PrologStringUtils;
import com.prolog.framework.utils.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 订单出库 未指定拣选站
 */
@Component(OutBoundType.TASK_TYPE+1+OutBoundType.IF_SfReq+0)
@Transactional(rollbackFor=Exception.class,timeout = 1000)
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




    @Override
    public void unbound(OutboundTask outboundTask) {

        List<PickStation> lists=getAvailablePickStation();
        if(lists.size()<1) { LogServices.logSysBusiness("无可用拣选站");return;}

        String pickCode=lists.get(0).getDeviceNo();
        SimilarityDataEntityLoadInterface similarityDataEntityListLoad=getsimilarityDataEntityListLoad(outboundTask);

        List<DetailDataBean> list = similarityDataEntityListLoad.getOutDetailList();


        for (DetailDataBean detailDataBeand : list) {
                ContainerTask ordercontainerTask = new ContainerTask();
                ordercontainerTask.setLotId(detailDataBeand.getLotId());
                ordercontainerTask.setCreateTime(new Date(System.currentTimeMillis()));
                ordercontainerTask.setOwnerId(detailDataBeand.getOwnerId());
                ordercontainerTask.setItemId(detailDataBeand.getItemId());
                ordercontainerTask.setTaskType(1);
                ordercontainerTask.setSourceType(1);


                ordercontainerTask.setTargetType(OutBoundEnum.TargetType.AGV.getNumber()); //Agv目标区域
                float last = detailDataBeand.getLast();           //获取需要出库的总量

                Float countQty=qcSxStoreMapper.getSxStoreCount(detailDataBeand.getItemId(), detailDataBeand.getLotId(), detailDataBeand.getOwnerId());
                if(countQty==null) countQty=0f;
                if (countQty<last) {LogServices.logSysBusiness("库存:"+countQty+"不够出:"+last+"！"); return; }
               //更新任务锁
                AgvStorageLocation agvStorageLocation = agvStorageLocationMapper.findByPickCodeAndLock(pickCode, 0, 0);
              //  agvStorageLocation.setTaskLock(1);
              //  agvStorageLocationMapper.update(agvStorageLocation);
                  if(agvStorageLocation==null){ LogServices.logSysBusiness(pickCode+"拣选站点位已经锁定！");return ;}
                int  LocationType= agvStorageLocation.getLocationType();
                if(!this.isExistTask(agvStorageLocation.getRcsPositionCode())){
                 /*   List<String> listBillNos=new ArrayList<String>();
                    listBillNos.addAll(similarityDataEntityListLoad.currentBillNoList);
                    int seqno=0;*/
                List<Map<String, Object>> listSxStore = qcSxStoreMapper.getSxStoreByOrder(detailDataBeand.getItemId(), detailDataBeand.getLotId(), detailDataBeand.getOwnerId());

                for (Map<String, Object> sxStore1 : listSxStore) {
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
                         ordercontainerTask.setTaskCode(PrologStringUtils.newGUID());
                        containerTaskMapper.save(ordercontainerTask);
                  List<OutboundTaskDetail> listOutBoundTaskDetailList=outBoundTaskDetailMapper.findByMap(MapUtils.
                          put("billNo",detailDataBeand.getBillNo())
                          .put("itemId",detailDataBeand.getItemId()).put("ownerId",detailDataBeand.getOwnerId()
                          ).put("lotId",detailDataBeand.getLotId()).getMap(),OutboundTaskDetail.class);
                      for(OutboundTaskDetail outboundTaskDetail:listOutBoundTaskDetailList){
                          ContainerTaskDetail containerTaskDetail=new ContainerTaskDetail();
                          BeanUtils.copyProperties(detailDataBeand,containerTaskDetail);
                          containerTaskDetail.setBillNo(outboundTaskDetail.getBillNo());
                          containerTaskDetail.setSeqNo(outboundTaskDetail.getSeqNo());
                          containerTaskDetail.setContainerCode((String) sxStore1.get("containerNo"));
                          containerTaskDetail.setCreateTime(new java.sql.Date(System.currentTimeMillis()));
                          if(((BigDecimal) sxStore1.get("qty")).floatValue()<outboundTaskDetail.getQty()){
                              containerTaskDetail.setQty(((BigDecimal) sxStore1.get("qty")).floatValue());
                          }else{
                              containerTaskDetail.setQty(outboundTaskDetail.getQty());
                          }

                          containerTaskDetailMapperMapper.save(containerTaskDetail);
                      }

                        outBoundTaskMapper.updateOutBoundTaskBySQL(String.join(",", similarityDataEntityListLoad.getCrrentBillNoList()));
                        similarityDataEntityListLoad.getCrrentBillNoList().remove(","+detailDataBeand.getBillNo()+"'");

                      if (last <= 0) break;
                    }
                    if (((BigDecimal) sxStore1.get("qty")).floatValue() > last && (LocationType == 4 || LocationType == 5) ) { //非整托
                        //非整托
                        last = ((BigDecimal) sxStore1.get("qty")).floatValue()-last ;
                        String target = agvStorageLocation.getRcsPositionCode();
                        ordercontainerTask.setTarget(target);
                        ordercontainerTask.setQty(((BigDecimal) sxStore1.get("qty")).floatValue());
                        String sourceLocation = PrologCoordinateUtils.splicingStr((Integer) sxStore1.get("x"), (Integer) sxStore1.get("y"), (Integer) sxStore1.get("layer"));
                        ordercontainerTask.setSource(sourceLocation);
                        ordercontainerTask.setTaskState(1);
                        ordercontainerTask.setContainerCode((String) sxStore1.get("containerNo"));
                         ordercontainerTask.setTaskCode(PrologStringUtils.newGUID());
                        containerTaskMapper.save(ordercontainerTask);


                        List<OutboundTaskDetail> listOutBoundTaskDetailList=outBoundTaskDetailMapper.findByMap(MapUtils.
                                put("billNo",detailDataBeand.getBillNo())
                                .put("itemId",detailDataBeand.getItemId()).put("ownerId",detailDataBeand.getOwnerId()
                                ).put("lotId",detailDataBeand.getLotId()).getMap(),OutboundTaskDetail.class);
                        for(OutboundTaskDetail outboundTaskDetail:listOutBoundTaskDetailList){
                            ContainerTaskDetail containerTaskDetail=new ContainerTaskDetail();
                            BeanUtils.copyProperties(detailDataBeand,containerTaskDetail);
                            containerTaskDetail.setBillNo(outboundTaskDetail.getBillNo());
                            containerTaskDetail.setSeqNo(outboundTaskDetail.getSeqNo());
                            containerTaskDetail.setContainerCode((String) sxStore1.get("containerNo"));
                            containerTaskDetail.setCreateTime(new java.sql.Date(System.currentTimeMillis()));
                            if(((BigDecimal) sxStore1.get("qty")).floatValue()<outboundTaskDetail.getQty()){
                                containerTaskDetail.setQty(((BigDecimal) sxStore1.get("qty")).floatValue());
                            }else{
                                containerTaskDetail.setQty(outboundTaskDetail.getQty());
                            }

                            containerTaskDetailMapperMapper.save(containerTaskDetail);
                        }

                        outBoundTaskMapper.updateOutBoundTaskBySQL(String.join(",", similarityDataEntityListLoad.getCrrentBillNoList()));
                        similarityDataEntityListLoad.getCrrentBillNoList().remove(","+detailDataBeand.getBillNo()+"'");
                       break;
                    }

                }
            }







        }


    }

}