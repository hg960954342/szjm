package com.prolog.eis.scheduler;

import com.prolog.eis.dto.rcs.RcsRequestResultDto;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.service.ContainerTaskService;
import com.prolog.eis.service.InBoundTaskService;
import com.prolog.eis.service.OutBoundTaskService;
import com.prolog.eis.service.rcs.RcsRequestService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.PrologApiJsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TimeTask {

    @Autowired
	InBoundTaskService inBoundTaskService;

	@Autowired
	OutBoundTaskService outBoundTaskService;
	/**
	 * 定时处理入库任务
	 * @throws Exception
	 */
	@Scheduled(initialDelay = 3000, fixedDelay = 5000)
	public void buildCkTask() throws Exception  {
		inBoundTaskService.inboundTask();
	}



	/**
	 * 定时出库任务
	 * @throws Exception
	 */
	@Scheduled(initialDelay = 3000, fixedDelay = 5000)
	public void buildUnTask() throws Exception  {
		outBoundTaskService.unboundTask();

	}





	/*@Scheduled(initialDelay = 3000, fixedDelay = 8000)
	public void sendWcsTask() throws Exception {

		synchronized ("sendwcstask".intern()) {
			wcsTaskPriorityService.sendWcsTask();
		}
	}

	@Scheduled(initialDelay = 3000, fixedDelay = 5000)
	public void resendMcsTask()throws Exception{
		List<MCSTask> mcsTasks = mcsInterfaceService.findFailMCSTask();
		for (MCSTask mcsTask : mcsTasks) {
			try {
				mcsInterfaceService.recall(mcsTask);
			}catch (Exception e) {
				FileLogHelper.WriteLog("resendMcsTask", "MCS重发异常:"+e.toString());
			}
		}
	}

	@Scheduled(initialDelay = 3000, fixedDelay = 5000)
	public void resendGcsTask()throws Exception{
		List<GcsTask> gcsTasks = gcsInterfaceService.findRecallGCSTask();
		for (GcsTask gcsTask : gcsTasks) {
			try {
				gcsInterfaceService.recall(gcsTask);
			}catch (Exception e) {
				FileLogHelper.WriteLog("resendGcsTask", "GCS重发异常:"+e.toString());
			}
		}
	}*/

	@Autowired
	private ContainerTaskService containerTaskService;

	@Autowired
	private RcsRequestService rcsRequestService;

	//定时给agv小车下分任务
	@Scheduled(initialDelay = 3000,fixedDelay = 5000)
	public void sendTask2Rcs() throws Exception {
		List<ContainerTask> containerTasks = containerTaskService.selectByTaskStateAndSourceType("1", "2");
		if (!containerTasks.isEmpty() && containerTasks.size() > 0){
			this.sendTask(containerTasks);
		}
	}

	private void sendTask(List<ContainerTask> containerTasks) throws Exception {
		for (ContainerTask containerTask : containerTasks) {
			//获取参数
			String taskCode = containerTask.getTaskCode();
			String containerCode = Integer.toString(containerTask.getContainerCode());
			String source = containerTask.getSource();
			String target = containerTask.getTarget();

			//判断目的地位置不为空
			if (!containerTask.getTarget().equals("") && containerTask.getTarget() != null) {
				//封装参数
				Map<Object, Object> map = new HashMap<>();
				map.put("task_code", taskCode);
				map.put("container_code", containerCode);
				map.put("source", source);
				map.put("target", target);

				try {
					//添加任务下发前日志
					String data = PrologApiJsonHelper.toJson(map);
					FileLogHelper.WriteLog("sendTask2Rcs", "EIS->RCS任务：" + data);

					RcsRequestResultDto rcsRequestResultDto = null;
					//获取任务终点，判断小车任务模板
					int targetType = Integer.parseInt(containerTask.getTargetType());
					if (targetType == 1) {//目标地点位为 agv区域
						rcsRequestResultDto = rcsRequestService.sendTask(taskCode, containerCode, source, target, "01", "3");

					} else {//目的地点位 为输送线
						rcsRequestResultDto = rcsRequestService.sendTask(taskCode, containerCode, source, target, "01", "3");
					}

					String restJson = PrologApiJsonHelper.toJson(rcsRequestResultDto);
					//添加日志文件
					FileLogHelper.WriteLog("sendTask2Rcs", "EIS->RCS返回：" + restJson);
					String restCode = rcsRequestResultDto.getCode();

					if (restCode.equals("0")) {
						//更新发送给设备的时间
						containerTask.setSendTime(new Date());
						//更新任务状态
						containerTask.setTaskType(2);//已发送给下游设备
						containerTaskService.update(containerTask);
					}else {
						//agv接收失败
						FileLogHelper.WriteLog("sendToRcsErr","EIS->RCS错误："+restJson);

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
