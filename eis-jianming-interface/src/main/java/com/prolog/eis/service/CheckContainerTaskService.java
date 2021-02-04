package com.prolog.eis.service;

import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.model.wms.OutboundTaskDetail;
import com.prolog.eis.model.wms.OutboundTaskDetailD;

import java.util.List;

public interface CheckContainerTaskService {

    List<String> findByContainerCode(List<OutboundTaskDetailD> details);
}
