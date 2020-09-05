package com.prolog.eis.service.impl;

import com.prolog.eis.dao.AgvStorageLocationMapper;
import com.prolog.eis.dto.rcs.RcsRequestResultDto;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.service.EisSendRcsTaskService;
import com.prolog.eis.service.rcs.RcsRequestService;
import com.prolog.eis.util.PrologStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class EisSendRcsTaskServiceSend   {

    @Autowired
    private RcsRequestService rcsRequestService;

    @Autowired
    private AgvStorageLocationMapper agvStorageLocationMapper;

    @Autowired
    EisSendRcsTaskService eisSendRcsTaskService;


     public void sendTask(List<ContainerTask> containerTasks) {
        for (ContainerTask containerTask : containerTasks) {
            //获取参数
            String taskCode = containerTask.getTaskCode();
           if (StringUtils.isEmpty(taskCode)) {
                taskCode = PrologStringUtils.newGUID();
                containerTask.setTaskCode(taskCode);
            }
            String containerCode = containerTask.getContainerCode();
            String source = containerTask.getSource();
            String target = containerTask.getTarget();

            //判断目的地位置不为空
            if (!containerTask.getTarget().equals("") && containerTask.getTarget() != null) {

                AgvStorageLocation targetPosition = agvStorageLocationMapper.findByRcs(containerTask.getTarget());
                if (targetPosition.getTaskLock()==1){continue;}

                try {
					/*//添加任务下发前日志
					String data = PrologApiJsonHelper.toJson(map);
					FileLogHelper.WriteLog("sendTask2Rcs", "EIS->RCS任务：" + data);*/
                    ResultAgvDto rsultAgvDto =new ResultAgvDto();
                    RcsRequestResultDto rcsRequestResultDto;
                    //获取任务终点，判断小车任务模板
                    int targetType = containerTask.getTargetType();
                    if (containerTask.getTaskType() == 6) {
                        //补空托
                         rcsRequestResultDto = rcsRequestService.sendTask(taskCode, containerCode, source, target, "F01", "3");

                    } else {
                         rcsRequestResultDto = rcsRequestService.sendTask(taskCode, containerCode, source, target, "F01", "1");
                    }

                    rsultAgvDto.setRcsRequestResultDto(rcsRequestResultDto);
                    rsultAgvDto.setContainerTask(containerTask);
                    eisSendRcsTaskService.updateAgvTask(rsultAgvDto);
                } catch (Exception e) {
                    //任务下发失败
                    //String resultMsg = "EIS->RCS [RCSInterface] 任务下发 rcs 失败：请求rcs失败";
                    //FileLogHelper.WriteLog("RCSRequestErr", resultMsg);
                    LogServices.logSys(e);
                    break;
                }
            }
        }



    }
}
