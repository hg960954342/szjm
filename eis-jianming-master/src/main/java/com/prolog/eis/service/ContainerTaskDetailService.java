package com.prolog.eis.service;

import com.prolog.eis.model.wms.ContainerTaskDetail;

import java.util.List;

public interface ContainerTaskDetailService {
    List<ContainerTaskDetail> selectByContainerCode(int containerCode);
}
