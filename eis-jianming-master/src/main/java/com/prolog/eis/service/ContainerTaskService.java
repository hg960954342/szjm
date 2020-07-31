package com.prolog.eis.service;

import com.prolog.eis.model.eis.ContainerTask;

import java.util.List;

public interface ContainerTaskService {

    List<ContainerTask> selectByTaskState(String taskState, String sourceType);
}
