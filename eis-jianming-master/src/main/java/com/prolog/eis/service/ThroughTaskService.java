package com.prolog.eis.service;

public interface ThroughTaskService {

	/**
	 * 添加借道任务
	 * @param palletId
	 * @param containerCode
	 * @param materielNo
	 * @param factoryNo
	 * @param materielType
	 * @param materielName
	 * @param startStations
	 * @param endStations
	 */
	void addThroughTask(String palletId,String containerCode,
			String materielNo,String factoryNo,String materielType,
			String materielName,String startStations,String endStations) throws Exception;
}
