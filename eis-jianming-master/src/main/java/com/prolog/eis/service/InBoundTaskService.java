package com.prolog.eis.service;

import com.prolog.eis.dto.base.Coordinate;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.InboundTask;

import java.util.List;

public interface InBoundTaskService {

    public void inboundTask() ;

    List<InboundTask> selectByContainerCode(String containerCode);

    void update(InboundTask inboundTask);
    void delete(InboundTask inboundTask);
 }
