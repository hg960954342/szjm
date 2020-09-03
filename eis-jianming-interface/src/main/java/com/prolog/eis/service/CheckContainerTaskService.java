package com.prolog.eis.service;

import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.model.wms.OutboundTaskDetail;

import java.util.List;

public interface CheckContainerTaskService {

    List<String> findByContainerCode(List<OutboundTaskDetail> details);
}
