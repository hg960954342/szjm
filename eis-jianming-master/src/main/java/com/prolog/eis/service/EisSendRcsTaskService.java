package com.prolog.eis.service;

import com.prolog.eis.model.wms.ContainerTask;

import java.util.List;

public interface EisSendRcsTaskService {
    void sendTask(List<ContainerTask> containerTasks);
}
