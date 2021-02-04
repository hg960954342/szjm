package com.prolog.eis.service.impl;

import com.prolog.eis.dao.ContainerTaskMapper;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.model.wms.OutboundTaskDetail;
import com.prolog.eis.model.wms.OutboundTaskDetailD;
import com.prolog.eis.service.CheckContainerTaskService;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CheckContainerTaskServiceImpl implements CheckContainerTaskService {

    @Autowired
    private ContainerTaskMapper containerTaskMapper;
    @Override
    public List<String> findByContainerCode(List<OutboundTaskDetailD> details) {
        List<String> containerCodes = new ArrayList<>();
        for (OutboundTaskDetailD detail : details) {
            List<ContainerTask> containerTasks = containerTaskMapper.findByMap(MapUtils.put("containerCode", detail.getContainerCode()).getMap(), ContainerTask.class);
            if (containerTasks!=null && containerTasks.size()>0){
                containerCodes.add(detail.getContainerCode());
            }
        }
       return containerCodes;
    }
}
