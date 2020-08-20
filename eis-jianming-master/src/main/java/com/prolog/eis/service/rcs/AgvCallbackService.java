package com.prolog.eis.service.rcs;

public interface AgvCallbackService {

	/**
	 * agv状态回告
	 * @param taskCode
	 * @param method
	 * @throws Exception
	 */
	void agvCallback(String taskCode,String method);
}