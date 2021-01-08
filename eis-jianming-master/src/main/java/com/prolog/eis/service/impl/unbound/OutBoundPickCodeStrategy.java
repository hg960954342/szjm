package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.*;
import com.prolog.eis.dao.baseinfo.PortInfoMapper;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.eis.service.enums.OutBoundEnum;
import com.prolog.eis.service.enums.OutBoundType;
import com.prolog.eis.service.impl.unbound.entity.DetailDataBean;
import com.prolog.eis.service.sxk.SxStoreCkService;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 订单出库  指定拣选站
 */
@Component(OutBoundType.TASK_TYPE + 1 + OutBoundType.IF_SF_REQ + 1)
public class OutBoundPickCodeStrategy extends DefaultOutBoundPickCodeStrategy {

    @Autowired
    AgvStorageLocationMapper agvStorageLocationMapper;
    @Autowired
    PickStationMapper pickStationMapper;

    @Autowired
    ContainerTaskMapper containerTaskMapper;


    @Autowired
    OutBoundTaskMapper outBoundTaskMapper;

    @Autowired
    OutBoundTaskDetailMapper outBoundTaskDetailMapper;

    @Autowired
    ContainerTaskDetailMapper containerTaskDetailMapperMapper;


    @Autowired
    QcSxStoreMapper qcSxStoreMapper;

    @Autowired
    PortInfoMapper portInfoMapper;

    @Autowired
    SxStoreCkService sxStoreCkService;

    @Autowired
    OutBoundContainerService outBoundContainerService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unbound(OutboundTask outboundTask) {
        List<OutboundTask> listCheckOuts=outBoundTaskMapper.findByMap(MapUtils.put("taskType",OutBoundEnum.TaskType.ORDER_CHECK_OUT_BOUND.getTaskTypeNumber()).getMap(),OutboundTask.class);
        if(listCheckOuts.size()>0){
            LogServices.logSysBusiness("盘点任务优先！");
            return;
        }
        SimilarityDataEntityLoadInterface similarityDataEntityListLoad = getsimilarityDataEntityListLoad(outboundTask);
        List<DetailDataBean> list = similarityDataEntityListLoad.getOutDetailList();
        List<String> listBillNo = new ArrayList<>();
        for (DetailDataBean detailDataBeand : list) {
            String bill_no_String = detailDataBeand.getBillNo();
            listBillNo.addAll(Arrays.asList(bill_no_String.split(",")));
            outBoundContainerService.buildContainerTaskAndDetails(detailDataBeand,detailDataBeand.getStandard(),true);
        }
        removeCompleteOrderAndUpdate(listBillNo, similarityDataEntityListLoad);
    }



}
