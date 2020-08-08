package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.*;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.util.BeanUtil;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 订单出库  指定拣选站
 *
 */
@Component(OutBoundType.TASK_TYPE+1+OutBoundType.IF_SfReq+1)
public class OutBoundPickCodeStrategy extends DefaultOutBoundPickCodeStrategy {

    //订单超时处理时间默认值 半个小时 单位为min
    public static final long overTime=30;



    @Autowired
    OutBoundTaskMapper outBoundTaskMapper;

    @Autowired
    OutBoundTaskDetailMapper outBoundTaskDetailMapper;


    @Autowired
    ContainerTaskMapper containerTaskMapper;

    @Autowired
    ContainerTaskDetailMapper containerTaskDetailMapperMapper;

    @Autowired
    ContainerTaskDetailPoolMapper containerTaskDetailPoolMapper;

    @Autowired
    SimilarityDataEntityLoad similarityDataEntityLoad;

    @Autowired
    QcSxStoreMapper qcSxStoreMapper;
    @Autowired
    AgvStorageLocationMapper agvStorageLocationMapper;

    @Override
    public void unbound(OutboundTask outboundTask) {
         List<OutboundTask> listOverTimeBoundTask=containerTaskDetailPoolMapper.getOutBoudTaskOverTime(overTime);
         for(OutboundTask overTimeBoundTask: listOverTimeBoundTask){
             unBoundTask(overTimeBoundTask);
         }
         outboundTask = getSimilarityResult(outboundTask);
          unBoundTask(outboundTask);
    }


    public void unBoundTask(OutboundTask outboundTask){


        Criteria criteria = Criteria.forClass(OutboundTaskDetail.class);
        criteria.setRestriction(Restrictions.eq("billNo", outboundTask.getBillNo()));
        List<OutboundTaskDetail> outboundTaskDetailList = outBoundTaskDetailMapper.findByCriteria(criteria);
        for (int i = 0; i < outboundTaskDetailList.size(); i++) {
            OutboundTaskDetail outboundTaskDetail = outboundTaskDetailList.get(i);
            Map<String, Object> map = new HashMap<String, Object>();
            ContainerTaskDetail detail=new ContainerTaskDetail();
            detail.setBillNo(outboundTaskDetail.getBillNo());
            detail.setSeqNo(outboundTaskDetail.getSeqNo());
            detail.setItemId(outboundTaskDetail.getItemId());
            detail.setLotId(outboundTaskDetail.getLotId());
            detail.setOwnerId(outboundTaskDetail.getOwnerId());

            map= BeanUtil.describe(detail);

            List<ContainerTaskDetail> list = containerTaskDetailMapperMapper.findByMap(map, ContainerTaskDetail.class);
            double qtyDouble = list.stream().mapToDouble((x) -> x.getQty()).sum();
            double last = outboundTaskDetail.getQty() - qtyDouble-outboundTaskDetail.getFinishQty();

            long sxStoreAll=0l;
            //根据出库明细找库存
            List<Map<String,Object>> listSxStore=qcSxStoreMapper.getSxStoreByOrder(outboundTaskDetail.getItemId(),outboundTaskDetail.getLotId(),outboundTaskDetail.getOwnerId());
            //1.目标拣选站是否有任务
            String pickCode=outboundTask.getPickCode();

            //查找点位


            AgvStorageLocation agvStorageLocation=agvStorageLocationMapper.findByPickCodeAndLock(pickCode,0,0);
            if(null==agvStorageLocation) continue;
            int LocationType=agvStorageLocation.getLocationType();
            String source= PrologCoordinateUtils.splicingStr(agvStorageLocation.getX(),agvStorageLocation.getY(),agvStorageLocation.getCeng());
            for(Map<String,Object> sxStore1:listSxStore){
                float sxQty=(Float) sxStore1.get("qty");
                sxStoreAll+=sxQty;
                ContainerTask ordercontainerTask=new ContainerTask();
                BeanUtils.copyProperties(outboundTaskDetail,ordercontainerTask);
                ordercontainerTask.setContainerCode((String)sxStore1.get("containerNo"));
                ordercontainerTask.setCreateTime(new Date(System.currentTimeMillis()));
                ordercontainerTask.setTaskType(1);
                ordercontainerTask.setSource(PrologCoordinateUtils.splicingStr((Integer) sxStore1.get("x"),(Integer) sxStore1.get("y"),(Integer) sxStore1.get("layer")));
                ordercontainerTask.setSourceType(1);
                ordercontainerTask.setTarget(source);
                ordercontainerTask.setTargetType(1);
                String uuid = UUID.randomUUID().toString().replaceAll("-","");
                ordercontainerTask.setTaskCode(uuid);
                ContainerTaskDetail containerTaskDetail=new ContainerTaskDetail();
                BeanUtils.copyProperties(outboundTaskDetail,containerTaskDetail);
                containerTaskDetail.setContainerCode((String)sxStore1.get("containerNo"));
                if(sxStoreAll<=last){  //出整托任务
                    ordercontainerTask.setQty(sxStoreAll);
                     if(LocationType==3 ||LocationType==5  ){
                    //2.判断目标点是否存在任务
                         if(!this.isExistTask(source))  {
                             containerTaskMapper.save(ordercontainerTask);
                             containerTaskDetail.setQty(sxStoreAll);
                             containerTaskDetailMapperMapper.save(containerTaskDetail);
                         }
                     }

                }else{ //非整托任务
                    ordercontainerTask.setQty(sxStoreAll-last);
                    if(LocationType==4 ||LocationType==5  ){
                        //2.判断目标点是否存在任务
                        if(!this.isExistTask(source)) {
                            containerTaskMapper.save(ordercontainerTask);
                            containerTaskDetail.setQty(sxStoreAll);
                            containerTaskDetailMapperMapper.save(containerTaskDetail);
                        }
                    }
                }




            }


            //加入订单池
            ContainerTaskDetailPool containerTaskDetailPool = new ContainerTaskDetailPool();
            containerTaskDetailPool.setBillNo(outboundTaskDetail.getBillNo());
            containerTaskDetailPool.setCreateTime(new java.sql.Date(System.currentTimeMillis()));
            containerTaskDetailPoolMapper.save(containerTaskDetailPool);












        }
    }


}
