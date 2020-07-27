package scheduler;

import org.springframework.stereotype.Component;

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
}
