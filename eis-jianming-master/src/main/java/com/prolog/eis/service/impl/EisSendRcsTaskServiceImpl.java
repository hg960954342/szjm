package com.prolog.eis.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.prolog.eis.dao.AgvStorageLocationMapper;
import com.prolog.eis.dto.rcs.RcsRequestResultDto;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.service.ContainerTaskService;
import com.prolog.eis.service.EisSendRcsTaskService;
import com.prolog.eis.service.rcs.RcsRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class EisSendRcsTaskServiceImpl implements EisSendRcsTaskService {
    @Autowired
    private ContainerTaskService containerTaskService;


    @Autowired
    private AgvStorageLocationMapper agvStorageLocationMapper;
    @Autowired
    EisSendRcsTaskServiceSend eisSendRcsTaskServiceSend;
    public void updateAgvTask(ResultAgvDto rsultAgvDto){

//        for(ResultAgvDto resultAgvDto:list){
            ContainerTask containerTask=rsultAgvDto.getContainerTask();
            RcsRequestResultDto  rcsRequestResultDto =rsultAgvDto.getRcsRequestResultDto();
            String restCode = rcsRequestResultDto.getCode();

            if (restCode.equals("0")) {
                //更新发送给设备的时间
                //获取参数
                String taskCode = containerTask.getTaskCode();

                containerTask.setSendTime(new Date());
                containerTask.setTaskCode(taskCode);
                //更新任务状态
                containerTask.setTaskState(2);//已发送给下游设备
                containerTaskService.update(containerTask);
                //更新点位状态 为 任务锁定
                AgvStorageLocation targetPosition = agvStorageLocationMapper.findByRcs(containerTask.getTarget());
                targetPosition.setTaskLock(1);
                agvStorageLocationMapper.update(targetPosition);
            } else {
                //agv接收失败
                String restJson = JSONObject.toJSONString(rcsRequestResultDto) ;
                String resultMsg = "EIS->RCS [RCSInterface] 返回JSON：[message]:" + restJson;
                LogServices.logSysBusiness(resultMsg);

            }
//        }

    }

}
