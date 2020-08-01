package com.prolog.eis.service;

import com.prolog.eis.model.wms.ContainerTask;

import java.util.List;

public interface ContainerTaskService {

    List<ContainerTask> selectByTaskStateAndSourceType(String taskState, String sourceType);

    void update(ContainerTask containerTask);

    ContainerTask selectByTaskCode(String taskCode);
}
