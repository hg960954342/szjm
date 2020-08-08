package com.prolog.eis.scheduler;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.prolog.eis.dao.AgvStorageLocationMapper;
import com.prolog.eis.dao.ContainerTaskDetailMapper;
import com.prolog.eis.dto.rcs.RcsRequestResultDto;
import com.prolog.eis.model.mcs.MCSTask;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.model.wms.RepeatReport;
import com.prolog.eis.service.ContainerTaskService;
import com.prolog.eis.service.EisCallbackService;
import com.prolog.eis.service.InBoundTaskService;
import com.prolog.eis.service.OutBoundTaskService;
import com.prolog.eis.service.RepeatReportService;
import com.prolog.eis.service.mcs.McsInterfaceService;
import com.prolog.eis.service.rcs.RcsRequestService;
import com.prolog.eis.service.sxk.SxStoreCkService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;

@Component
public class TimeTask {

    @Autowired
	InBoundTaskService inBoundTaskService;

	@Autowired
	OutBoundTaskService outBoundTaskService;
	@Autowired
	private McsInterfaceService mcsInterfaceService;
	@Autowired
	private SxStoreCkService sxStoreCkService;
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

	@Scheduled(initialDelay = 3000, fixedDelay = 5000)
	public void buildAndSendSxCkTask() throws Exception {

		try {
			synchronized("kucun".intern()) {
				sxStoreCkService.buildSxCkTask();
			}
			sxStoreCkService.sendSxCkTask();
		}catch (Exception e) {
			// TODO: handle exception
			FileLogHelper.WriteLog("生成四向库出库任务错误", e.toString());
		}
	}



	/*@Scheduled(initialDelay = 3000, fixedDelay = 8000)
	public void sendWcsTask() throws Exception {

		synchronized ("sendwcstask".intern()) {
			wcsTaskPriorityService.sendWcsTask();
		}
	}*/

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

	/*@Scheduled(initialDelay = 3000, fixedDelay = 5000)
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

	@Autowired
	private RepeatReportService repeatReportService;
	@Autowired
	private EisCallbackService eisCallbackService;

	@Autowired
	private AgvStorageLocationMapper agvStorageLocationMapper;

	//定时给agv小车下分任务
//	@Scheduled(initialDelay = 3000,fixedDelay = 5000)
	public void sendTask2Rcs() throws Exception {
		List<ContainerTask> containerTasks = containerTaskService.selectByTaskStateAndSourceType("1", "2");
		if (!containerTasks.isEmpty() && containerTasks.size() > 0){
			this.sendTask(containerTasks);
		}
	}

	/**
	 * 回告wms未成功 重复回告
	 * @throws Exception
	 */
//	@Scheduled(initialDelay = 3000, fixedDelay = 5000)
	public void resendReport()throws Exception{
		List<RepeatReport> repeatReports = repeatReportService.findByState(0);
		if (repeatReports != null && repeatReports.size()>0) {
			for (RepeatReport repeatReport : repeatReports) {
				eisCallbackService.recall(repeatReport);
			}
		}
	}
	private void sendTask(List<ContainerTask> containerTasks) throws Exception {
		for (ContainerTask containerTask : containerTasks) {
			//获取参数
			String taskCode = containerTask.getTaskCode();
			String containerCode = containerTask.getContainerCode();
			String source = containerTask.getSource();
			String target = containerTask.getTarget();

			//判断目的地位置不为空
			if (!containerTask.getTarget().equals("") && containerTask.getTarget() != null) {
				/*//封装参数
				Map<Object, Object> map = new HashMap<>();
				map.put("task_code", taskCode);
				map.put("container_code", containerCode);
				map.put("source", source);
				map.put("target", target);*/

				//获取目标点位
				Criteria criteria=Criteria.forClass(AgvStorageLocation.class);
				criteria.setRestriction(Restrictions.eq("rcsPositionCode",containerTask.getTarget()));
				AgvStorageLocation targetPosition = agvStorageLocationMapper.findByCriteria(criteria).get(0);

				try {
					/*//添加任务下发前日志
					String data = PrologApiJsonHelper.toJson(map);
					FileLogHelper.WriteLog("sendTask2Rcs", "EIS->RCS任务：" + data);*/

					RcsRequestResultDto rcsRequestResultDto = null;
					//获取任务终点，判断小车任务模板
					int targetType = containerTask.getTargetType();
					if (targetType == 1) {//目标地点位为 agv区域
						rcsRequestResultDto = rcsRequestService.sendTask(taskCode, containerCode, source, target, "F01", "3");
					} else {//目的地点位 为输送线
						rcsRequestResultDto = rcsRequestService.sendTask(taskCode, containerCode, source, target, "F02", "3");
					}

					String restJson = PrologApiJsonHelper.toJson(rcsRequestResultDto);
					String restCode = rcsRequestResultDto.getCode();

					if (restCode.equals("0")) {
						//更新发送给设备的时间
						containerTask.setSendTime(new Date());
						//更新任务状态
						containerTask.setTaskState(2);//已发送给下游设备
						containerTaskService.update(containerTask);
						//更新点位状态 为 任务锁定
						targetPosition.setTaskLock(1);
						agvStorageLocationMapper.update(targetPosition);
					}else {
						//agv接收失败
						String resultMsg = "EIS->RCS [RCSInterface] 返回JSON：[message]:" + restJson;
						FileLogHelper.WriteLog("RCSRequestErr",resultMsg);

					}
				} catch (Exception e) {
					e.printStackTrace();
					//任务下发失败
					String resultMsg = "EIS->RCS [RCSInterface] 任务下发 rcs 失败：请求rcs失败";
					FileLogHelper.WriteLog("RCSRequestErr",resultMsg);
				}
			}
		}
	}

	/**
	 * 补空托托盘
	 */
	@Scheduled(initialDelay = 3000, fixedDelay = 5000)
	public void replenishContainer()throws Exception{
		//agv_storagelocation 楼层为 3， 位置类型为 存储位 ，task_lock 为空闲， lock 为不锁定
		//判断是否需要补空托盘
			//判断是否有空托盘
				//生成容器任务container_task 托盘号uuid,task_type 待定，source....

	}


	@Scheduled(initialDelay = 3000, fixedDelay = 5000)
	public void testReport()throws Exception{
		ContainerTask containerTask = new ContainerTask();
//		containerTask.setContainerCode("800011");
//		containerTask.setContainerCode("800012");
//		containerTask.setContainerCode("700010");
		/*containerTask.setTaskType(2);
		containerTask.setItemId("SPH00001363");
		containerTask.setOwnerId("008");*/
		eisCallbackService.inBoundReport("6000002");
//		eisCallbackService.outBoundReport(containerTask);
//		eisCallbackService.moveBoundReport(containerTask);
//		eisCallbackService.checkBoundReport("PDC00000101");

	}



}
