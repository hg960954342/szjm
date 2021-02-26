package com.prolog.eis.service;

import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.eis.model.wms.OutboundTaskDetailDto;

public interface OutboundDataService {


    void insertOutboundTask(OutboundTask datum);

    void insertOutboundTaskDetail(OutboundTaskDetailDto detail);

    void insertMoveTask(OutboundTask datum);

    void insertMoveTaskDetail(OutboundTaskDetailDto detail);

    void insertCheckOutTaskDetail(OutboundTaskDetailDto detail);

    void insertEmptyBoxOutStockTask(OutboundTask datum);

    void insertEmptyBoxOutStockTaskDetail(OutboundTaskDetailDto detail);

}
