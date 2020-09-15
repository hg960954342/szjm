package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.OutBoundTaskDetailMapper;
import com.prolog.eis.dao.OutBoundTaskMapper;
import com.prolog.eis.model.wms.OutboundTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Component(OutBoundType.IF_SfReq + 0)
@Scope(SCOPE_SINGLETON)
@SuppressWarnings("all")
public class SimilarityDataEntityListLoad implements SimilarityDataEntityLoadInterface {


    public Set<String> currentBillNoList = Collections.synchronizedSet(new HashSet<>());//当前执行的billNoString
    public int maxSize = 1; //订单池处理最大数量


    @Autowired
    OutBoundTaskMapper outBoundTaskMapper;

    @Autowired
    OutBoundTaskDetailMapper outBoundTaskDetailMapper;
    //订单超时处理时间默认值 半个小时 单位为min
    public static final long overTime = 1;


    public Set<String> getCrrentBillNoList() {
        return currentBillNoList;
    }

    public int getMaxSize() {
        return maxSize;
    }


    /**
     * 超时任务优先
     * 1.订单池未满
     *
     * @param outboundTask
     * @return
     */
    public synchronized void addOutboundTask(OutboundTask outboundTask) {
        if (getCrrentBillNoList().size() <= maxSize && outboundTask.getSfReq() == 0) {
            getCrrentBillNoList().add("'" + outboundTask.getBillNo() + "'");
        }
    }


    public List<DetailDataBean> getOutDetailList() {
        return outBoundTaskDetailMapper.getOuntBoundDetailAll(String.join(",", getCrrentBillNoList()));


    }

    /**
     * 获取相识度最高的出库任务
     *
     * @return
     */
    private OutboundTask getSimilarityDataList() {
        List<OutboundTask> outboundTaskList = outBoundTaskMapper.getListOutboundTask();
        outboundTaskList= outboundTaskList.stream().filter(x->{
            if(getCrrentBillNoList().contains( String.format("'%s'", x.getBillNo()))){
                return false;
            }else{
                return true;
            }
        }).collect(Collectors.toList());
        List<SimilarityDataEntity> list = new ArrayList<SimilarityDataEntity>();

        for (OutboundTask outboundTask : outboundTaskList) {
            Float count = outBoundTaskDetailMapper.getPoolItemCount(String.join(",", getCrrentBillNoList()));
            Float countSame = outBoundTaskDetailMapper.getPoolSameItemCount(String.join(",", getCrrentBillNoList()), outboundTask.getBillNo());
            Float currentCount = outBoundTaskDetailMapper.getPoolItemCount(String.format("'%s'", outboundTask.getBillNo()));
            if(countSame==null){ countSame=0f;};
            if(currentCount==null){ currentCount=0f;};
            if(count==null){ count=0f;};
            float similarity = countSame / (currentCount >= count ? currentCount : count);
            SimilarityDataEntity similarityDataEntity = new SimilarityDataEntity();
            similarityDataEntity.setOutboundTask(outboundTask);
            similarityDataEntity.setSimilarity(similarity);
            list.add(similarityDataEntity);
        }
        return list.stream().max(Comparator.comparing(SimilarityDataEntity::getSimilarity)).orElse(null).getOutboundTask();


    }

}
