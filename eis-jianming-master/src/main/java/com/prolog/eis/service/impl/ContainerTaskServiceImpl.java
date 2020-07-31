package com.prolog.eis.service.impl;

import com.prolog.eis.dao.ContainerTaskMapper;
import com.prolog.eis.model.eis.ContainerTask;
import com.prolog.eis.service.ContainerTaskService;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ContainerTaskServiceImpl implements ContainerTaskService {

    @Autowired
    private ContainerTaskMapper containerTaskMapper;


    @Override
    public List<ContainerTask> selectByTaskState(String taskState, String sourceType) {
        Map<String, Object> map = MapUtils.put("task_state", taskState).put("source_type", sourceType).getMap();
        return containerTaskMapper.findByMap(map,ContainerTask.class);
    }
}
