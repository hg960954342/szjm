package com.prolog.eis.service.rcs.impl;

import com.prolog.eis.dto.base.Coordinate;
import com.prolog.eis.dto.eis.EisReport;
import com.prolog.eis.dto.eis.EisReportDto;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.ContainerTaskDetail;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.service.AgvStorageLocationService;
import com.prolog.eis.service.ContainerTaskDetailService;
import com.prolog.eis.service.ContainerTaskService;
import com.prolog.eis.service.EisCallbackService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.HttpUtils;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.eis.util.PrologCoordinateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prolog.eis.service.rcs.AgvCallbackService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AgvCallbackServiceImpl implements AgvCallbackService{

	@Autowired
	private ContainerTaskService containerTaskService;

	@Autowired
	private AgvStorageLocationService agvStorageLocationService;

	@Autowired
	private EisCallbackService eisCallbackService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void agvCallback(String taskCode,String method) throws Exception{

		//agv 回告 method ：start : 任务开始、outbin : 走出储位、end : 任务结束

		//根据任务号 查询 托盘任务
		List<ContainerTask> containerTasks = containerTaskService.selectByTaskCode(taskCode);
		for (ContainerTask containerTask : containerTasks) {
			//判断小车状态
			if (method.equals("start")){
				//小车任务开始
				containerTask.setTaskState(3);//设置下游设备回告开始
				containerTask.setStartTime(new Date());
				containerTaskService.update(containerTask);

			}
			if (method.equals("outbin")){
				//小车离开原存储位
				containerTask.setTaskState(4);//设置下游设备离开原存储位
				containerTask.setMoveTime(new Date());
				containerTaskService.update(containerTask);

			}
			if (method.equals("end")){
				//小车任务结束
				containerTask.setTaskState(1);//设置托盘到位
				containerTask.setEndTime(new Date());//设置小车结束时间
				//获取target坐标
				String target = containerTask.getTarget();
				Coordinate analysis = PrologCoordinateUtils.analysis(target);

				//判断托盘当前位置
				AgvStorageLocation agvStorageLocation = agvStorageLocationService.findByCoord(analysis);
				if (agvStorageLocation.getLocationType() == 2){
					//小车搬运后当前位置在入库输送线口
					//设置当前位置为输送线
					containerTask.setSourceType(2);
					containerTaskService.update(containerTask);
					//通知输送线运行
					// TODO Auto-generated method stub

				}else {
					if (containerTask.getTaskType() == 1){
						eisCallbackService.outBoundReport(containerTask);
					}
				}
			}
		}


	}


}
