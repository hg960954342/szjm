package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.*;
import com.prolog.eis.model.wms.ContainerTaskDetail;
import com.prolog.eis.model.wms.ContainerTaskDetailPool;
import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.eis.model.wms.OutboundTaskDetail;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单出库  名称必须符合 taskType+数据库中配置的类型数字 否则不执行
 */
@Component("taskType1")
public class OrderBoundStrategy implements UnBoundStragtegy {


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

    @Override
    public void unbound(OutboundTask outboundTask) {
         outboundTask = getSimilarityResult(outboundTask);

        Criteria criteria = Criteria.forClass(OutboundTaskDetail.class);
        criteria.setRestriction(Restrictions.eq("billno", outboundTask.getBillno()));
        List<OutboundTaskDetail> outboundTaskDetailList = outBoundTaskDetailMapper.findByCriteria(criteria);
        for (int i = 0; i < outboundTaskDetailList.size(); i++) {
            OutboundTaskDetail outboundTaskDetail = outboundTaskDetailList.get(i);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("billNo", outboundTaskDetail.getBillno());
            map.put("seqNo", outboundTaskDetail.getSeqno());
            map.put("itemId", outboundTaskDetail.getItemid());
            map.put("lotid", outboundTaskDetail.getLotid());
            map.put("ownerid", outboundTaskDetail.getOwnerid());
            List<ContainerTaskDetail> list = containerTaskDetailMapperMapper.findByMap(map, ContainerTaskDetail.class);
            double qtyDouble = list.stream().mapToDouble((x) -> x.getQty()).sum();
            double last = outboundTaskDetail.getQty() - qtyDouble; //还要去掉已经出库的数量！！！！！！
            //加入订单池
            ContainerTaskDetailPool containerTaskDetailPool = new ContainerTaskDetailPool();
            BeanUtils.copyProperties(outboundTaskDetail, containerTaskDetailPool);
            containerTaskDetailPool.setQty(last);
            containerTaskDetailPoolMapper.save(containerTaskDetailPool);

        }


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
        map.put("billNo", OutboundTask.getBillno());
        long count = containerTaskDetailMapperMapper.findCountByMap(map, ContainerTaskDetail.class);
        long billNoCount = containerTaskDetailPoolMapper.getContainerTaskDetailPoolCountByBillNo(OutboundTask.getBillno());
        long poolCount = containerTaskDetailPoolMapper.findCountByCriteria(Criteria.forClass(ContainerTaskDetailPool.class));
        if (count >= poolCount) {
            result = billNoCount / count;
        } else {
            result = billNoCount / poolCount;
        }
        return result;


    }
}
