package com.prolog.eis.service.sxk;

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
}
