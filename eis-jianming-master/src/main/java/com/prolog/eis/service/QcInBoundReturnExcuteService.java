package com.prolog.eis.service;

import com.prolog.eis.dto.base.Coordinate;
import com.prolog.eis.dto.eis.PortInfoDto;
import com.prolog.eis.model.eis.PortInfo;
import com.prolog.eis.model.eis.WmsInboundTask;
import com.prolog.eis.model.eis.WmsOutboundTask;
import com.prolog.eis.model.store.SxConnectionRim;

public interface QcInBoundReturnExcuteService {

	/**
	 * 质检，一般出库
	 * @param task
	 * @param containerNo
	 * @param targetPosition
	 * @param targetLayer
	 * @param targetX
	 * @param targetY
	 * @throws Exception
	 */
	void taskReturnOutBound(WmsOutboundTask task,String containerNo,String targetPosition,int targetLayer,int targetX,int targetY) throws Exception;

	/**
	 * 
	 * @param task
	 * @param containerNo
	 * @param targetPosition
	 * @param targetLayer
	 * @param targetX
	 * @param targetY
	 * @throws Exception
	 */
	void taskReturnInbound(SxConnectionRim sxConnectionRim,WmsInboundTask wmsInboundTask,String taskId,Double weight,String containerNo,String source,int sourceLayer,int sourceX,int sourceY) throws Exception;

	/**
	 * 更新完成状态
	 * @param containerNo
	 * @param position
	 * @return
	 * @throws Exception
	 */
	int updateCompleteTask(String containerNo, String position,boolean showError) throws Exception;
	
	/**
	 * 执行借道任务
	 * @param task
	 * @param inBoundRequest
	 */
	void excuteThroughTask(SxConnectionRim sxConnectionRim,String containerNo,Coordinate coordinate,PortInfoDto portInfoDto) throws Exception;
	
	/**
	 *到达西码头入库口
	 * @param containerNo
	 * @param portInfo
	 */
	void arrivePort(String containerNo,PortInfo portInfo);
	
	/**
	 * 
	 * @param taskId
	 * @throws Exception
	 */
	void setTaskStart(String taskId) throws Exception;
}
