package com.prolog.eis.service.impl;

import com.prolog.eis.dao.AgvStorageLocationMapper;
import com.prolog.eis.dto.rcs.RcsRequestResultDto;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.service.ContainerTaskService;
import com.prolog.eis.service.EisCallbackService;
import com.prolog.eis.service.EisSendRcsTaskService;
import com.prolog.eis.service.RepeatReportService;
import com.prolog.eis.service.rcs.RcsRequestService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.PrologApiJsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class EisSendRcsTaskServiceImpl implements EisSendRcsTaskService {
    @Autowired
    private ContainerTaskService containerTaskService;

    @Autowired
    private RcsRequestService rcsRequestService;

    @Autowired
    private AgvStorageLocationMapper agvStorageLocationMapper;

    @Override
    public void sendTask(List<ContainerTask> containerTasks) {
        for (ContainerTask containerTask : containerTasks) {
            //获取参数
            String taskCode = containerTask.getTaskCode();
            if (StringUtils.isEmpty(taskCode)) {
                taskCode = UUID.randomUUID().toString().replaceAll("-", "");
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

                    RcsRequestResultDto rcsRequestResultDto = null;
                    //获取任务终点，判断小车任务模板
                    int targetType = containerTask.getTargetType();
                    if (containerTask.getTaskType() == 6) {
                        //补空托
                        rcsRequestResultDto = rcsRequestService.sendTask(taskCode, containerCode, source, target, "F01", "4");
                    } else {
                        rcsRequestResultDto = rcsRequestService.sendTask(taskCode, containerCode, source, target, "F01", "3");
                    }
					/*if (targetType == 1) {//目标地点位为 agv区域
						if (containerTask.getTaskType()==6){
							//补空托
							rcsRequestResultDto = rcsRequestService.sendTask(taskCode, containerCode, source, target, "F01", "4");
						}else {
							rcsRequestResultDto = rcsRequestService.sendTask(taskCode, containerCode, source, target, "F01", "3");
						}
					} else {//目的地点位 为输送线
						rcsRequestResultDto = rcsRequestService.sendTask(taskCode, containerCode, source, target, "M10", "3");
					}
*/
                    String restJson = PrologApiJsonHelper.toJson(rcsRequestResultDto);
                    String restCode = rcsRequestResultDto.getCode();

                    if (restCode.equals("0")) {
                        //更新发送给设备的时间
                        containerTask.setSendTime(new Date());
                        containerTask.setTaskCode(taskCode);
                        //更新任务状态
                        containerTask.setTaskState(2);//已发送给下游设备
                        containerTaskService.update(containerTask);
                        //更新点位状态 为 任务锁定
                        targetPosition.setTaskLock(1);
                        agvStorageLocationMapper.update(targetPosition);
                    } else {
                        //agv接收失败
                        String resultMsg = "EIS->RCS [RCSInterface] 返回JSON：[message]:" + restJson;
                        FileLogHelper.WriteLog("RCSRequestErr", resultMsg);

                    }
                } catch (Exception e) {
                    //任务下发失败
                    String resultMsg = "EIS->RCS [RCSInterface] 任务下发 rcs 失败：请求rcs失败";
                    FileLogHelper.WriteLog("RCSRequestErr", resultMsg);
                }
            }
        }
    }
}
