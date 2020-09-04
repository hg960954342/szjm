package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.OutBoundTaskDetailHistoryMapper;
import com.prolog.eis.dao.OutBoundTaskDetailMapper;
import com.prolog.eis.dao.OutBoundTaskHistoryMapper;
import com.prolog.eis.dao.OutBoundTaskMapper;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.util.BeanUtil;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class OutBoundContainerService {

    @Autowired
    OutBoundTaskHistoryMapper outBoundTaskHistoryMapper;
    @Autowired
    OutBoundTaskMapper outBoundTaskMapper;
    @Autowired
    OutBoundTaskDetailMapper outBoundTaskDetailMapper;
    @Autowired
    OutBoundTaskDetailHistoryMapper outBoundTaskDetailHistoryMapper;

    /**
     * 将以完成的出库订单转入历史表中
     * @param outboundTask
     */
    public void deleteOutBoundAndInsertHistory(OutboundTask outboundTask) {
        OutboundTaskHistory outboundTaskHistory = new OutboundTaskHistory();
        outboundTask.setTaskState(1);
        BeanUtils.copyProperties(outboundTask,outboundTaskHistory);
        outBoundTaskHistoryMapper.save(outboundTaskHistory);
        outBoundTaskMapper.deleteById(outboundTask.getId(),OutboundTask.class);
    }


    /**
     * 将生成托盘任务的出库明细数据转入历史表中
     * @param outboundTaskDetail
     * @param containerTaskDetail
     */
    public void deleteDetailAndInsertHistory(OutboundTaskDetail outboundTaskDetail, ContainerTaskDetail containerTaskDetail) {
        outboundTaskDetail.setFinishQty((float) containerTaskDetail.getQty());
        outboundTaskDetail.setEndTime(new Date(System.currentTimeMillis()));
        OutboundTaskDetailHistory outboundTaskDetailHistory = new OutboundTaskDetailHistory();
        BeanUtils.copyProperties(outboundTaskDetail,outboundTaskDetailHistory);
        outBoundTaskDetailHistoryMapper.save(outboundTaskDetailHistory);
        outBoundTaskDetailMapper.deleteById(outboundTaskDetail.getId(),OutboundTaskDetail.class);
    }

    /**
     * 将库存中不为已上架状态的出库明细数据转入历史表中
     * @param outboundTaskDetail
     */
    public void deleteDetailAndInsertHistory(OutboundTaskDetail outboundTaskDetail) {
        outboundTaskDetail.setFinishQty(0);
        outboundTaskDetail.setEndTime(new Date(System.currentTimeMillis()));
        OutboundTaskDetailHistory outboundTaskDetailHistory = new OutboundTaskDetailHistory();
        BeanUtils.copyProperties(outboundTaskDetail,outboundTaskDetailHistory);
        outBoundTaskDetailHistoryMapper.save(outboundTaskDetailHistory);
        outBoundTaskDetailMapper.deleteById(outboundTaskDetail.getId(),OutboundTaskDetail.class);
    }


    /**OutboundTaskDetail删除转明细history
     * @param outboundTaskDetails
     */
    public void deleteBatchDetailAndInsertHistory(List<OutboundTaskDetail> outboundTaskDetails) {
       for(OutboundTaskDetail outboundTaskDetail:outboundTaskDetails){
           this.deleteDetailAndInsertHistory(outboundTaskDetail);
       }
    }


}
