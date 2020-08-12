package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.*;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.util.BeanUtil;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.eis.util.PrologLocationUtils;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单出库 未指定拣选站
 */
@Component(OutBoundType.TASK_TYPE+1+OutBoundType.IF_SfReq+0)
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
    ContainerTaskDetailPoolMapper containerTaskDetailPoolMapper;

    @Autowired
    SimilarityDataEntityLoad similarityDataEntityLoad;

    @Autowired
    QcSxStoreMapper qcSxStoreMapper;
    @Autowired
    AgvStorageLocationMapper agvStorageLocationMapper;
    @Autowired
    PickStationMapper pickStationMapper;

    @Override
    public void unbound(OutboundTask outboundTask){


        Criteria criteria = Criteria.forClass(OutboundTaskDetail.class);
        criteria.setRestriction(Restrictions.eq("billNo", outboundTask.getBillNo()));
        List<OutboundTaskDetail> outboundTaskDetailList = outBoundTaskDetailMapper.findByCriteria(criteria);
        for (int i = 0; i < outboundTaskDetailList.size(); i++) {
            OutboundTaskDetail outboundTaskDetail = outboundTaskDetailList.get(i);
            Map<String, Object> map = new HashMap<>();
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
            //1.获取能够作业的拣选站
            List<PickStation> listPickStation=getAvailablePickStation();
            if(listPickStation.size()<1) continue; //没有拣选站 结束当前当前明细
            PickStation pickStation=listPickStation.get(0);//   取第一个可用的拣选站
            String pickCode=pickStation.getStationNo();
/***********************************以下逻辑同订单指定拣选站逻辑*************************************************************/
            //查找点位坐标


            AgvStorageLocation agvStorageLocation=agvStorageLocationMapper.findByPickCodeAndLock(pickCode,0,0);
            int LocationType=agvStorageLocation.getLocationType();
            String source= PrologLocationUtils.splicingXYStr(PrologCoordinateUtils.splicingStr(agvStorageLocation.getX(),agvStorageLocation.getY(),agvStorageLocation.getCeng())) ;
            for(Map<String,Object> sxStore1:listSxStore){
                float sxQty=(Float) sxStore1.get("qty");
                sxStoreAll+=sxQty;
                ContainerTask ordercontainerTask=new ContainerTask();
                BeanUtils.copyProperties(outboundTaskDetail,ordercontainerTask);
                ordercontainerTask.setContainerCode((String)sxStore1.get("containerNo"));
                ordercontainerTask.setCreateTime(new Date(System.currentTimeMillis()));
                ordercontainerTask.setTaskType(1);
                ordercontainerTask.setSource(PrologLocationUtils.splicingXYStr(PrologCoordinateUtils.splicingStr((Integer) sxStore1.get("x"),(Integer) sxStore1.get("y"),(Integer) sxStore1.get("layer"))));
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
