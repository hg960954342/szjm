package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.*;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.eis.util.PrologLocationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 订单出库 未指定拣选站
 */
@Component(OutBoundType.TASK_TYPE+1+OutBoundType.IF_SfReq+0)
@Transactional(rollbackFor=Exception.class)
@SuppressWarnings("all")
@Slf4j
public class OrderBoundStrategy extends DefaultOutBoundPickCodeStrategy {

    //订单超时处理时间默认值 半个小时 单位为min
    private static final long overTime=30;



    @Autowired
    OutBoundTaskMapper outBoundTaskMapper;

    @Autowired
    OutBoundTaskDetailMapper outBoundTaskDetailMapper;


    @Autowired
    ContainerTaskMapper containerTaskMapper;

    @Autowired
    ContainerTaskDetailMapper containerTaskDetailMapperMapper;





    @Autowired
    SimilarityDataEntityListLoad similarityDataEntityListLoad;

    @Autowired
    QcSxStoreMapper qcSxStoreMapper;
    @Autowired
    AgvStorageLocationMapper agvStorageLocationMapper;
    @Autowired
    PickStationMapper pickStationMapper;

    @Override
    public void unbound(OutboundTask outboundTask) {
        //获取出库明细
        List<DetailDataBean> list = similarityDataEntityListLoad.getOutDetailList(this.getClass());

        for (DetailDataBean detailDataBeand : list) {
            //1.获取能够作业的拣选站
            List<PickStation> listPickStation = getAvailablePickStation();
            if (listPickStation.size() < 1) {  log.info("未找能够作业的拣选站！");continue; }//没有拣选站 结束当前
            PickStation pickStation = listPickStation.get(0);//   取第一个可用的拣选站
            String pickCode = pickStation.getStationNo();

            AgvStorageLocation agvStorageLocation = agvStorageLocationMapper.findByPickCodeAndLock(pickCode, 0, 0);

            String target = agvStorageLocation.getRcsPositionCode();

            ContainerTask ordercontainerTask = new ContainerTask();
            ordercontainerTask.setLotId(detailDataBeand.getLotId());
            ordercontainerTask.setCreateTime(new Date(System.currentTimeMillis()));
            ordercontainerTask.setOwnerId(detailDataBeand.getOwnerId());
            ordercontainerTask.setItemId(detailDataBeand.getItemId());
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            ordercontainerTask.setTaskCode(uuid);
            ordercontainerTask.setTaskType(1);
            ordercontainerTask.setSourceType(1);
            ordercontainerTask.setTarget(target);
            ordercontainerTask.setTargetType(1);
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
            Map<String, Object> sxStore1 = listSxStore.stream().min(Comparator.comparingLong(entry -> {
                 Object objectDeptNum= entry.get("deptNum");
                 if(objectDeptNum!=null){
                     return (Long) entry.get("deptNum");
                 }
               return 0;
            })).get();
            String sourceLocation=PrologCoordinateUtils.splicingStr((Integer) sxStore1.get("x"),(Integer) sxStore1.get("y"),(Integer) sxStore1.get("layer"));
            ordercontainerTask.setSource(sourceLocation);
            ordercontainerTask.setContainerCode((String)sxStore1.get("containerNo"));
            int LocationType = agvStorageLocation.getLocationType();
            if(((BigDecimal) sxStore1.get("qty")).floatValue()==last&&(LocationType==3 ||LocationType==5 )&&!this.isExistTask(target)){ //出整托
                containerTaskMapper.save(ordercontainerTask);
                List<ContainerTaskDetail> listContainerTaskDetail=outBoundTaskDetailMapper.
                        getOutBoundContainerTaskDetail(String.join(",", similarityDataEntityListLoad.currentBillNoList));
                containerTaskDetailMapperMapper.saveBatch(listContainerTaskDetail);
                outBoundTaskMapper.updateOutBoundTaskBySQL(String.join(",",similarityDataEntityListLoad.currentBillNoList));


            }
            if(((BigDecimal) sxStore1.get("qty")).floatValue()==last&&(LocationType==4 ||LocationType==5 )&&!this.isExistTask(target)){ //非整托
                 containerTaskMapper.save(ordercontainerTask);
                List<ContainerTaskDetail> listContainerTaskDetail=outBoundTaskDetailMapper.getOutBoundContainerTaskDetail
                        (String.join(",", similarityDataEntityListLoad.currentBillNoList));
                containerTaskDetailMapperMapper.saveBatch(listContainerTaskDetail);

                outBoundTaskMapper.updateOutBoundTaskBySQL(String.join(",",similarityDataEntityListLoad.currentBillNoList));
            }


        }
    }





}
