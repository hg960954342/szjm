package com.prolog.eis.service.mcs;

import java.util.List;

import com.prolog.eis.dto.mcs.McsGroupDirectionDto;
import com.prolog.eis.dto.mcs.McsHoistStatusDto;
import com.prolog.eis.model.caracross.SxCarAcrossTask;
import com.prolog.eis.model.mcs.MCSTask;

public interface McsInterfaceService {

	public String sendMcsTask(int type,String stockId,String source,String target,String weight,int priority)throws Exception;

	public String sendMcsTaskWithOutPathAsyc(int type, String stockId, String source, String target, String weight, int priority)
			throws Exception;
	
	public void recall(MCSTask mcsTask)throws Exception;
	
	public List<MCSTask> findFailMCSTask()throws Exception;
	
	public boolean getExitStatus(String position) throws Exception;
	
	/**
	 * 提升机状态确认
	 * @param hoistId
	 * @return
	 * @throws Exception
	 */
	public McsHoistStatusDto getHoistStatus(String hoistId) throws Exception;
	
	public void firstFloorQcPort()throws Exception;

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
	public McsGroupDirectionDto selectDirectionByExist(String coord) throws Exception;

	/**
	 * 编辑PLC出入库方向
	 */
	public boolean updatePlcVariableByCoord(String coord, int direction) throws Exception;
}
