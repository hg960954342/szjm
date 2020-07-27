package com.prolog.eis.service;

import java.util.List;

import com.prolog.eis.model.eis.WmsOutboundTask;

public interface CkCheckService {

	/**
	 * 下发出库任务
	 * @param taskList
	 */
	void buildCkTask(List<WmsOutboundTask> taskList);
}
