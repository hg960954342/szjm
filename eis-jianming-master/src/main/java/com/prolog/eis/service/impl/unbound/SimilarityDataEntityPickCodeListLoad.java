package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.OutBoundTaskDetailMapper;
import com.prolog.eis.dao.OutBoundTaskMapper;
import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.eis.service.enums.OutBoundType;
import com.prolog.eis.service.impl.unbound.entity.DetailDataBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Component(OutBoundType.IF_SF_REQ + 1)
@Scope(SCOPE_SINGLETON)
public class SimilarityDataEntityPickCodeListLoad extends SimilarityDataEntityListLoad {


    @Autowired
    OutBoundTaskMapper outBoundTaskMapper;

    @Autowired
    OutBoundTaskDetailMapper outBoundTaskDetailMapper;

    @Override
    public Set<String> getCrrentBillNoList() {
        return currentBillNoList;
    }
    @Override
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
