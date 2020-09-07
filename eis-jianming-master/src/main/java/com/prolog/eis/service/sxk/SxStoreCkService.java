package com.prolog.eis.service.sxk;

import com.prolog.eis.model.wms.ContainerTask;

public interface SxStoreCkService {

	/**
	 * 生成出库任务
	 * @throws Exception
	 */
	void buildSxCkTask() throws Exception;
	
	/**
	 * 发送出库任务
	 */
	void sendSxCkTask();

	void buildSxCkTaskByContainerTask(ContainerTask containerTask) ;
}
