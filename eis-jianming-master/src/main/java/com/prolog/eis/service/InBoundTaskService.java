package com.prolog.eis.service;

import com.prolog.eis.model.wms.InboundTask;

import java.util.List;
import java.util.Map;

public interface InBoundTaskService {

    public void inboundTask() ;

    InboundTask selectByContainerCode(String containerCode);

    void update(InboundTask inboundTask);
}
