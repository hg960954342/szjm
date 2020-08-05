package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.*;
import com.prolog.eis.model.sxk.SxStore;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单出库  名称必须符合 taskType+数据库中配置的类型数字 否则不执行
 */
@Component("taskType1")
public class OrderBoundStrategy implements UnBoundStragtegy {

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
        criteria.setRestriction(Restrictions.eq("billno", outboundTask.getBillNo()));
        List<OutboundTaskDetail> outboundTaskDetailList = outBoundTaskDetailMapper.findByCriteria(criteria);
        for (int i = 0; i < outboundTaskDetailList.size(); i++) {
            OutboundTaskDetail outboundTaskDetail = outboundTaskDetailList.get(i);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("billNo", outboundTaskDetail.getBillNo());
            map.put("seqNo", outboundTaskDetail.getSeqNo());
            map.put("itemId", outboundTaskDetail.getItemId());
            map.put("lotid", outboundTaskDetail.getLotId());
            map.put("ownerid", outboundTaskDetail.getOwnerId());
            List<ContainerTaskDetail> list = containerTaskDetailMapperMapper.findByMap(map, ContainerTaskDetail.class);
            double qtyDouble = list.stream().mapToDouble((x) -> x.getQty()).sum();
            double last = outboundTaskDetail.getQty() - qtyDouble-outboundTaskDetail.getFinishQty();

            long sxStoreAll=0l;
            //根据出库明细找库存
            List<Map<String,Object>> listSxStore=qcSxStoreMapper.getSxStoreByOrder(outboundTaskDetail.getItemId(),outboundTaskDetail.getLotId(),outboundTaskDetail.getOwnerId());
            //1.目标拣选站是否有任务
            String pickCode=outboundTaskDetail.getPickCode();
            //查找点位
            Criteria CriteriaAgvStorageLocation=Criteria.forClass(AgvStorageLocation.class);
            CriteriaAgvStorageLocation.setRestriction(Restrictions.eq("deviceNo",pickCode));
            CriteriaAgvStorageLocation.setRestriction(Restrictions.eq("lock",0));
            AgvStorageLocation agvStorageLocation=agvStorageLocationMapper.findByCriteria(CriteriaAgvStorageLocation).get(0);
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
                ordercontainerTask.setSourceType("1");
                ordercontainerTask.setTarget(source);
                ordercontainerTask.setTargetType("1");
                ordercontainerTask.setCreateTime(new Date(System.currentTimeMillis()));
                String uuid = UUID.randomUUID().toString().replaceAll("-","");
                ordercontainerTask.setTaskCode(uuid);
                ContainerTaskDetail containerTaskDetail=new ContainerTaskDetail();
                BeanUtils.copyProperties(outboundTaskDetail,containerTaskDetail);
                containerTaskDetail.setContainerCode((String)sxStore1.get("containerNo"));
                ordercontainerTask.setCreateTime(new Date(System.currentTimeMillis()));
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

    /**
     * 判断目标点位是否有任务
     * @param
     * @return boolean
     */
    public boolean isExistTask(String source){
        List<ContainerTask> list=containerTaskMapper.selectBySource(source);
        if(list!=null&&list.size()>0) return true;
        return false;
    }

    /**
     * 获取相邻订单的相似度高的任务
     *
     * @param outboundTask
     * @return
     */
    public OutboundTask getSimilarityResult(OutboundTask outboundTask) {
        long similarity = similarityDataEntityLoad.getSimilarity();
         if (similarity != 0l) {
            long current = this.getPoolTask(outboundTask);
            if (current >= similarity) {
                similarityDataEntityLoad.setSimilarity(this.getPoolTask(outboundTask));
                similarityDataEntityLoad.setOutboundTask(outboundTask);
                return outboundTask;
            } else {
                OutboundTask result = similarityDataEntityLoad.getOutboundTask();
                similarityDataEntityLoad.setSimilarity(this.getPoolTask(outboundTask));
                similarityDataEntityLoad.setOutboundTask(outboundTask);
                return result;
            }

        } else {
            similarityDataEntityLoad.setSimilarity(this.getPoolTask(outboundTask));
            similarityDataEntityLoad.setOutboundTask(outboundTask);
            return outboundTask;
        }
    }


    /**
     * 获取当前订单相似度的计算
     *
     * @param OutboundTask
     * @return
     */
    private long getPoolTask(OutboundTask OutboundTask) {
        long result;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("billNo", OutboundTask.getBillNo());
        long count = containerTaskDetailMapperMapper.findCountByMap(map, ContainerTaskDetail.class);
        long billNoCount = containerTaskDetailPoolMapper.getContainerTaskDetailPoolCountByBillNo(OutboundTask.getBillNo());
        long poolCount = containerTaskDetailPoolMapper.findCountByCriteria(Criteria.forClass(ContainerTaskDetailPool.class));
        if (count >= poolCount) {
            result = billNoCount / count;
        } else {
            result = billNoCount / poolCount;
        }
        return result;


    }
}
