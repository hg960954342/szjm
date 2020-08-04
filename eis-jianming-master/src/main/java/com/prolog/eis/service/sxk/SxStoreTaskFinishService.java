package com.prolog.eis.service.sxk;

import com.prolog.eis.model.sxk.SxStore;

public interface SxStoreTaskFinishService {

	/**
	 * 重新计算货位
	 * @param containerNo
	 * @throws Exception
	 */
	public void computeLocation(SxStore sxStore)throws Exception;
	//入库完成
	public void inBoundTaskFinish(String containerNo)throws Exception;
	//出库完成
	public void outBoundTaskFinish(String containerNo)throws Exception;
	
	/**
	 * 移库任务完成
	 * @param locationiId
	 * @throws Exception
	 */
	public void moveTaskFinish(Integer locationiId)throws Exception;
	
	/**
	 * 计算所有货位的入库货位
	 * @throws Exception
	 */
	public void computeIsInBoundLocation()throws Exception;
	
	public void computeIsInBoundLocationTest()throws Exception;
}
