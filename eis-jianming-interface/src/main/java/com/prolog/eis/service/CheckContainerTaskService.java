package com.prolog.eis.service;

import com.prolog.eis.model.wms.OutboundTaskDetailDto;

import java.util.List;

public interface CheckContainerTaskService {

    List<String> findByContainerCode(List<OutboundTaskDetailDto> details);
}
