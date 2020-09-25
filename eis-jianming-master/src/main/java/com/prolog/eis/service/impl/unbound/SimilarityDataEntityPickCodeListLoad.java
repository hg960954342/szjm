package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.OutBoundTaskDetailMapper;
import com.prolog.eis.dao.OutBoundTaskMapper;
import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.eis.service.enums.OutBoundType;
import com.prolog.eis.service.impl.unbound.entity.DetailDataBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Component(OutBoundType.IF_SfReq + 1)
@Scope(SCOPE_SINGLETON)
public class SimilarityDataEntityPickCodeListLoad extends SimilarityDataEntityListLoad {

    public Set<String> currentBillNoList = Collections.synchronizedSet(new HashSet<>()); //当前执行的billNoString
    public int maxSize = 1; //订单池处理最大数量*/


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
    @Override
    public synchronized void addOutboundTask(OutboundTask outboundTask) {
        if (getCrrentBillNoList().size() < maxSize && outboundTask.getSfReq() == 1) {
            getCrrentBillNoList().add(String.format("'%s'", outboundTask.getBillNo()));
        }


    }

    @Override
    public List<DetailDataBean> getOutDetailList() {
        return outBoundTaskDetailMapper.getOuntBoundDetailAll(String.join(",", getCrrentBillNoList()));

    }


}
