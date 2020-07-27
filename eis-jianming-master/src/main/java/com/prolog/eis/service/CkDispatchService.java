package com.prolog.eis.service;

import java.util.List;

import com.prolog.eis.dto.eis.PortInfoDto;
import com.prolog.eis.dto.eis.PortTemsInfoDto;
import com.prolog.eis.dto.eis.SxStoreDto;
import com.prolog.eis.dto.eis.TempPortZtTaskDto;
import com.prolog.eis.model.eis.WmsOutboundTask;

public interface CkDispatchService {

	/**
	 *查找质检回库任务
	 */
	void buildRkIqcTask();
	
	void buildEmptyTask(List<WmsOutboundTask> emptyTasks) throws Exception;
	
	void iqcTask(List<WmsOutboundTask> iqcTasks,List<TempPortZtTaskDto> tempPortZtTaskList) throws Exception;
	
	void xiafaChuKuTask(WmsOutboundTask wmsOutboundTask, PortInfoDto portInfoDto,SxStoreDto sxStore) throws Exception;
	
	void xiafaTenpTask(int taskId,List<PortTemsInfoDto> temPortTemsInfoDto,SxStoreDto sxStore) throws Exception;
}
