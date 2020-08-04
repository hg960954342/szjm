package com.prolog.eis.service.mcs;

import com.prolog.eis.dto.mcs.McsGroupDirectionDto;
import com.prolog.eis.dto.mcs.McsHoistStatusDto;
import com.prolog.eis.model.caracross.SxCarAcrossTask;
import com.prolog.eis.model.mcs.MCSTask;

import java.util.List;

public interface McsInterfaceService {

	String sendMcsTask(int type,String stockId,String source,String target,String weight,int priority)throws Exception;

	String sendMcsTaskWithOutPathAsyc(int type, String containerNo, String source, String target, String weight, int priority,int state)
			throws Exception;
	
	void recall(MCSTask mcsTask)throws Exception;
	
	List<MCSTask> findFailMCSTask()throws Exception;
	
	boolean getExitStatus(String position) throws Exception;
	
	/**
	 * 提升机状态确认
	 * @param hoistId
	 * @return
	 * @throws Exception
	 */
	McsHoistStatusDto getHoistStatus(String hoistId) throws Exception;
	
	void firstFloorQcPort()throws Exception;

	/**
	 * 提升机状态确认
	 * @throws Exception
	 *//*
	public boolean checkMcsStatus(SxCarAcrossTask sxCarAcrossTask, SxCarAcross sxCarAcross) throws Exception;*/

	/**
	 * 发送提升机小车跨层任务
	 * @param sxCarAcrossTask
	 */
	void sendMcsCarAcrossPush(SxCarAcrossTask sxCarAcrossTask);


	/**N
	 * MCS跨层任务回告
	 * @param sxCarAcrossTask
	 */
	//void crossLayerReport(SxCarAcrossTask sxCarAcrossTask) throws Exception;

	/**
	 * 查询提升机方向
	 */
	McsGroupDirectionDto selectDirectionByExist(String coord) throws Exception;

	/**
	 * 编辑PLC出入库方向
	 */
	boolean updatePlcVariableByCoord(String coord, int direction) throws Exception;
}
