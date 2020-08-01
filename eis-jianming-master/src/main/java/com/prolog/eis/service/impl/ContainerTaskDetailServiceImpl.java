package com.prolog.eis.service.impl;

import com.prolog.eis.dao.ContainerTaskDetailMapper;
import com.prolog.eis.model.wms.ContainerTaskDetail;
import com.prolog.eis.service.ContainerTaskDetailService;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class ContainerTaskDetailServiceImpl implements ContainerTaskDetailService {

    @Autowired
    private ContainerTaskDetailMapper containerTaskDetailMapper;

    /**
     * 根据托盘号查询托盘任务明细
     * @param containerCode 托盘号
     * @return
     */
    @Override
    public List<ContainerTaskDetail> selectByContainerCode(int containerCode) {
        Map<String, Object> map = MapUtils.put("container_code", containerCode).getMap();
        return containerTaskDetailMapper.findByMap(map,ContainerTaskDetail.class);
    }
}
