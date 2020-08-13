package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.*;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.eis.util.PrologLocationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 订单出库  指定拣选站
 *
 */
@Component(OutBoundType.TASK_TYPE+1+OutBoundType.IF_SfReq+1)
@SuppressWarnings("all")
public class OutBoundPickCodeStrategy extends DefaultOutBoundPickCodeStrategy {




    @Autowired
    OutBoundTaskMapper outBoundTaskMapper;

    @Autowired
    OutBoundTaskDetailMapper outBoundTaskDetailMapper;


    @Autowired
    ContainerTaskMapper containerTaskMapper;

    @Autowired
    ContainerTaskDetailMapper containerTaskDetailMapperMapper;




    @Autowired
    QcSxStoreMapper qcSxStoreMapper;
    @Autowired
    AgvStorageLocationMapper agvStorageLocationMapper;

    @Override
    public void unbound(OutboundTask outboundTask) {
        //获取出库明细
        List<DetailDataBean> list = similarityDataEntityListLoad.getOutDetailList(this.getClass());

        for (DetailDataBean detailDataBeand : list) {

            //1.获取指定拣选站

            String pickCode =detailDataBeand.getPickCode();

            AgvStorageLocation agvStorageLocation = agvStorageLocationMapper.findByPickCodeAndLock(pickCode, 0, 0);

            String target = PrologLocationUtils.splicingXYStr(PrologCoordinateUtils.splicingStr(agvStorageLocation.getX(), agvStorageLocation.getY(), agvStorageLocation.getCeng()));

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
            if (listSxStore.size() < 1) return; //没有库存结束
            Map<String, Object> sxStore1 = listSxStore.stream().filter(x -> {
                float qty = (float) x.get("qty");
                if (qty >= last) return true;
                return false;

            }).min(Comparator.comparingLong(entry -> (Long) entry.get("deptNum"))).get();
            ordercontainerTask.setSource(PrologLocationUtils.splicingXYStr(PrologCoordinateUtils.splicingStr((Integer) sxStore1.get("x"), (Integer) sxStore1.get("y"), (Integer) sxStore1.get("layer"))));
            ordercontainerTask.setContainerCode((String)sxStore1.get("containerNo"));
            int LocationType = agvStorageLocation.getLocationType();
            if((float) sxStore1.get("qty")==last&&(LocationType==3 ||LocationType==5 )&&!this.isExistTask(target)){ //出整托
                containerTaskMapper.save(ordercontainerTask);
                List<ContainerTaskDetail> listContainerTaskDetail=outBoundTaskDetailMapper.
                        getOutBoundContainerTaskDetail(String.join(",", similarityDataEntityListLoad.currentBillNoList));
                containerTaskDetailMapperMapper.saveBatch(listContainerTaskDetail);
                outBoundTaskMapper.updateOutBoundTaskBySQL(String.join(",",similarityDataEntityListLoad.currentBillNoList));

            }
            if((float) sxStore1.get("qty")>last&&(LocationType==4 ||LocationType==5 )&&!this.isExistTask(target)){ //非整托
                containerTaskMapper.save(ordercontainerTask);
                List<ContainerTaskDetail> listContainerTaskDetail=outBoundTaskDetailMapper.getOutBoundContainerTaskDetail
                        (String.join(",", similarityDataEntityListLoad.currentBillNoList));
                containerTaskDetailMapperMapper.saveBatch(listContainerTaskDetail);
                outBoundTaskMapper.updateOutBoundTaskBySQL(String.join(",",similarityDataEntityListLoad.currentBillNoList));
            }


        }
    }





}
