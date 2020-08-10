package com.prolog.eis.service.rcs.impl;

import com.prolog.eis.dao.AgvStorageLocationMapper;
import com.prolog.eis.dto.base.Coordinate;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.service.AgvStorageLocationService;
import com.prolog.eis.service.ContainerTaskService;
import com.prolog.eis.service.EisCallbackService;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prolog.eis.service.rcs.AgvCallbackService;

import java.util.Date;
import java.util.List;

@Service
public class AgvCallbackServiceImpl implements AgvCallbackService{

	@Autowired
	private ContainerTaskService containerTaskService;

	@Autowired
	private AgvStorageLocationMapper agvStorageLocationMapper;

	@Autowired
	private EisCallbackService eisCallbackService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void agvCallback(String taskCode,String method) throws Exception{

		//agv 回告 method ：start : 任务开始、outbin : 走出储位、end : 任务结束


		//根据任务号 查询 托盘任务
		List<ContainerTask> containerTasks = containerTaskService.selectByTaskCode(taskCode);
		for (ContainerTask containerTask : containerTasks) {

			Criteria criteria=Criteria.forClass(AgvStorageLocation.class);
			criteria.setRestriction(Restrictions.eq("rcsPositionCode",containerTask.getSource()));
			AgvStorageLocation currentPosition = agvStorageLocationMapper.findByCriteria(criteria).get(0);
			criteria.setRestriction(Restrictions.eq("rcsPositionCode",containerTask.getTarget()));
			AgvStorageLocation targetPosition = agvStorageLocationMapper.findByCriteria(criteria).get(0);
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
				//更新点位状态
				currentPosition.setTaskLock(0);
				currentPosition.setLocationLock(0);
				agvStorageLocationMapper.update(currentPosition);


			}
			if (method.equals("end")){
				//小车任务结束
				containerTask.setTaskState(1);//设置托盘到位
				containerTask.setEndTime(new Date());//设置小车结束时间
				containerTask.setSource(containerTask.getTarget());//修改当前位置
				containerTask.setSourceType(containerTask.getTargetType());//修改当前位置区域
				containerTask.setTarget("");//设置目的位置
				containerTaskService.update(containerTask);
				//判断托盘到位 区域
				if (containerTask.getTargetType() == 1){
					//托盘当前在 agv 区域
					if (containerTask.getTaskType() == 1){
						//出库完成 回告
						eisCallbackService.outBoundReport(containerTask);
						//锁定拣选站
						targetPosition.setLocationLock(1);
						agvStorageLocationMapper.update(targetPosition);
					}


				}else {
					//小车搬运后当前位置在入库输送线口
					//设置当前位置为输送线
					containerTask.setSourceType(2);
					containerTaskService.update(containerTask);
					//通知输送线运行
					// TODO Auto-generated method stub

				}
			}
		}


	}


}
