package com.prolog.eis.service;

import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.eis.model.wms.OutboundTaskDetail;

public interface OutboundTestDataService {


    void insertOutboundTask(OutboundTask datum);

    void insertOutboundTaskDetail(OutboundTaskDetail detail);
}
