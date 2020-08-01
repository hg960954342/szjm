package com.prolog.eis.scheduler;

import com.prolog.eis.dto.rcs.RcsRequestResultDto;
import com.prolog.eis.model.eis.ContainerTask;
import com.prolog.eis.service.ContainerTaskService;
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

	/**
	 * 获取四向库作业出库任务
	 * 
	 * @author yf
	 * @throws Exception
	 */
	/*@Scheduled(initialDelay = 3000, fixedDelay = 5000)
	public void buildCkTask() throws Exception {

		try {
			//获取wms下发过来的出库任务
			List<WmsOutboundTask> outboundList = wmsOutboundTaskMapper.getSxkCkTask();

			try {
				//检查空托出库的
				List<WmsOutboundTask> emptyTasks = ListHelper.where(outboundList, p->p.getTaskType() == 30 && p.getFinished() == 0 && p.getWmsPush() == 0);
				if(!emptyTasks.isEmpty()) {
					ckDispatchService.buildEmptyTask(emptyTasks);
				}
			}
			catch (Exception e) {
				// TODO: handle exception
				FileLogHelper.WriteLog("buildEmptyTaskError", e.toString());
			}

			try {
				//质检任务从暂存区到作业区
				//质检任务单独处理
				List<WmsOutboundTask> iqcTasks = ListHelper.where(outboundList, p->(p.getTaskType() == 20 || p.getTaskType() == 10) && p.getFinished() == 50);
				List<TempPortZtTaskDto> tempPortZtTaskList = ztckContainerMapper.getTempPort();
				if(!iqcTasks.isEmpty() && !tempPortZtTaskList.isEmpty()) {
					ckDispatchService.iqcTask(iqcTasks,tempPortZtTaskList);
				}
			}catch (Exception e) {
				// TODO: handle exception
				FileLogHelper.WriteLog("iqcTaskError", e.toString());
			}

			List<WmsOutboundTask> taskList = ListHelper.where(outboundList, p->p.getTaskType() != 30 || (p.getTaskType() == 30 && p.getWmsPush() == 1));
			if(taskList.isEmpty())
				return;

			try {
				synchronized("kucun".intern()) {
					ckCheckService.buildCkTask(taskList);	
				}
			} catch (Exception e) {
				// TODO: handle exception
				FileLogHelper.WriteLog("ckCheckBuildCkTaskError", e.toString());
			}
		}catch (Exception e) {
			// TODO: handle exception
			FileLogHelper.WriteLog("buildCkTask", e.toString());
		}
	}*/

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
		List<ContainerTask> containerTasks = containerTaskService.selectByTaskState("1", "2");
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
					FileLogHelper.WriteLog("sendTask2RcsLog", "EIS->RCS返回：" + restJson);
					String restCode = rcsRequestResultDto.getCode();

					if (restCode.equals("0")) {
						containerTask.setSendTime(new Date());
						//设备接收成功,更新发送给设备的时间
						containerTaskService.updateSendTime(containerTask);
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
