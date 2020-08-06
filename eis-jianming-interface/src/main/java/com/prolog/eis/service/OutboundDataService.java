package com.prolog.eis.service;

import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.eis.model.wms.OutboundTaskDetail;

public interface OutboundDataService {


    void insertOutboundTask(OutboundTask datum);

    void insertOutboundTaskDetail(OutboundTaskDetail detail);

    void insertMoveTask(OutboundTask datum);

    void insertMoveTaskDetail(OutboundTaskDetail detail);

    void insertCheckOutTaskDetail(OutboundTaskDetail detail);

    void insertEmptyBoxOutStockTask(OutboundTask datum);

    void insertEmptyBoxOutStockTaskDetail(OutboundTaskDetail detail);
}
