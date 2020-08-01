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
	private ContainerTaskDetailService containerTaskDetailService;
	@Autowired
	private AgvStorageLocationService agvStorageLocationService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void agvCallback(String taskCode,String method) throws Exception{

		//method ：start : 任务开始、outbin : 走出储位、end : 任务结束

		//1.判断agv回告结果
		//1.1
		// agv回告结果ok agv搬运到位
		//触发eis回告wms
		//	封装回告参数
		//		根据回告的托盘任务号 查询托盘任务 获取托盘id
		//		根据托盘id 查询托盘中任务订单明细
		//		封装成回告所需参数
		//	回告wms
		//修改托盘当前位置，
		//
		ContainerTask containerTask = containerTaskService.selectByTaskCode(taskCode);
		//判断小车状态
		if (method.equals("start")){
			//小车任务开始
			containerTask.setTaskState("3");//下游设备回告开始
			containerTask.setStartTime(new Date());
			containerTaskService.update(containerTask);

		}
		if (method.equals("outbin")){
			//小车离开原存储位
			containerTask.setTaskState("4");//下游设备离开原存储位
			containerTask.setMoveTime(new Date());
			containerTaskService.update(containerTask);

		}
		if (method.equals("end")){
			//小车任务结束
			containerTask.setTaskState("1");//托盘到位
			containerTask.setEndTime(new Date());//
			//获取target坐标
			String target = containerTask.getTarget();
			Coordinate analysis = PrologCoordinateUtils.analysis(target);

			//判断托盘当前位置
			AgvStorageLocation agvStorageLocation = agvStorageLocationService.findByCoord(analysis);
			if (agvStorageLocation.getLocationType() == 2){//小车搬运后当前位置在入库输送线口
				//更改
				containerTask.setSourceType("2");//设置当前位置为输送线
				containerTaskService.update(containerTask);
				//通知输送线运行

			}
			if (agvStorageLocation.getLocationType() != 2){//小车搬运后当前位置不在入库输送线口
				String json = this.encapsulation(containerTask);
				String url ="";//回告地址
				this.eisCallback(url,json);
			}
		}

	}

	/**
	 * 回告wms
	 */
	private void eisCallback(String url,String json) throws Exception {
		FileLogHelper.WriteLog("EisCallback","EIS->WMS回告:"+json);
		String restJson = HttpUtils.post(url, json);
		FileLogHelper.WriteLog("EisCallback","EIS->WMS返回："+restJson);

	}

	/**
	 * 封装回告数据
	 * @param containerTask
	 */
	private String encapsulation(ContainerTask containerTask) throws Exception {
		EisReportDto eisReportDto = new EisReportDto();
		List<EisReport> data = new ArrayList<>();

		List<ContainerTaskDetail> containerTaskDetails = containerTaskDetailService.selectByContainerCode(containerTask.getContainerCode());
		for (ContainerTaskDetail containerTaskDetail : containerTaskDetails) {
			//按订单号封装
			EisReport eisReport = new EisReport();
			eisReport.setContainerCode(containerTaskDetail.getContaineCode());
			eisReport.setBillNo(containerTaskDetail.getBillNo());
			eisReport.setItemId(containerTaskDetail.getItemId());
			eisReport.setLotId(containerTaskDetail.getLotId());
			eisReport.setQty(containerTaskDetail.getQty());
			eisReport.setAgvLoc(containerTask.getSource());

			data.add(eisReport);
		}

		String json = PrologApiJsonHelper.toJson(eisReportDto);
		return json;
	}

}
