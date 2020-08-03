package com.prolog.eis.service.impl;

import com.prolog.eis.dao.ContainerTaskMapper;
import com.prolog.eis.model.wms.ContainerTask;
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

    /**
     * 根据托盘状态和托盘当前区域查询托盘任务
     * @param taskState
     * @param sourceType
     * @return
     */
    @Override
    public List<ContainerTask> selectByTaskStateAndSourceType(String taskState, String sourceType) {
        Map<String, Object> map = MapUtils.put("task_state", taskState).put("source_type", sourceType).getMap();
        return containerTaskMapper.findByMap(map,ContainerTask.class);
    }

    /**
     * 更新发送给设备的时间
     * @param containerTask 托盘任务
     */
    @Override
    public void update(ContainerTask containerTask) {
        containerTaskMapper.update(containerTask);
    }

    /**
     * 根据托盘任务号查询托盘任务
     * @param taskCode 托盘任务号
     * @return
     */
    @Override
    public ContainerTask selectByTaskCode(String taskCode) {
        return containerTaskMapper.selectByTaskCode(taskCode);
    }
}
