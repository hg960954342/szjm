package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.*;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 订单出库必须继承此类 名称必须符合 OutBoundType.TASK_TYPE+数据库中配置的类型数字
 *  * IF_PICKCODE_EXISTS 是否指定拣选站 1指定 0不指定
 */
@Component("taskType1")
@Slf4j
public class DefaultOutBoundPickCodeStrategy implements UnBoundStragtegy {

    @Autowired
    AgvStorageLocationMapper agvStorageLocationMapper;
    @Autowired
    PickStationMapper pickStationMapper;

    @Autowired
    ContainerTaskMapper containerTaskMapper;

    @Autowired
    SimilarityDataEntityLoad similarityDataEntityLoad;


    @Autowired
    ContainerTaskDetailPoolMapper containerTaskDetailPoolMapper;

    @Autowired
    ContainerTaskDetailMapper containerTaskDetailMapperMapper;

    @Autowired
    private  Map<String, DefaultOutBoundPickCodeStrategy> strategyMap  ;

    public DefaultOutBoundPickCodeStrategy getDefaultOutBoundPickCodeStrategy(OutboundTask OutboundTask){
        int isPickCode=OutboundTask.getSfReq();
        return strategyMap.get(OutBoundType.TASK_TYPE+OutboundTask.getTaskType()+OutBoundType.IF_SfReq+isPickCode);
    }

    @Override
    public void unbound(OutboundTask outboundTask) {
        DefaultOutBoundPickCodeStrategy defaultOutBoundPickCodeStrategy=this.getDefaultOutBoundPickCodeStrategy(outboundTask);
        if(null!=defaultOutBoundPickCodeStrategy){
            log.info(defaultOutBoundPickCodeStrategy.getClass().getName());
        defaultOutBoundPickCodeStrategy.unbound(outboundTask);}

    }

    /**
     *获取所有能够作业的拣选站
     * @return
     */
    public List<PickStation> getAvailablePickStation(){
        //获取所有能用的拣选站
        Criteria pickStationCriteria=Criteria.forClass(PickStation.class);
        Long[] ios=new Long[]{1L,3L};
        Long[] taskTypes=new Long[]{1L,3L,5L,7L};
        pickStationCriteria.setRestriction(Restrictions.in("io",ios));
        pickStationCriteria.setRestriction(Restrictions.in("taskType",taskTypes));
        pickStationCriteria.setRestriction(Restrictions.eq("lock",0));
        List<PickStation> listPickStation= pickStationMapper.findByCriteria(pickStationCriteria);
        //过滤掉不能用的拣选站
        listPickStation=listPickStation.stream().filter(x->{
            String stationNo=x.getStationNo();
            AgvStorageLocation agvStorageLocation=agvStorageLocationMapper.findByPickCodeAndLock(stationNo,0,0);
            if(null!=agvStorageLocation){

                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return listPickStation;
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
        if (similarity != 0) {
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
    public long getPoolTask(OutboundTask OutboundTask) {
        long result;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("billNo", OutboundTask.getBillNo());
        long count = containerTaskDetailMapperMapper.findCountByMap(map, ContainerTaskDetail.class);
        if(count==0) {
            FileLogHelper.WriteLog("outBoundTask","单号",OutboundTask.getBillNo(),"没有出库明细");
            return 0;
        }
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
