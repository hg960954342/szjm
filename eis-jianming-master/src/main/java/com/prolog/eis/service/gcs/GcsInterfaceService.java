package com.prolog.eis.service.gcs;

import java.util.List;

import com.prolog.eis.dto.gcs.GcsAlarmReqDto;
import com.prolog.eis.dto.gcs.GcsOrderReportReqDto;
import com.prolog.eis.model.caracross.SxCarAcrossTask;
import com.prolog.eis.model.gcs.GcsTask;

public interface GcsInterfaceService {

	/**
	 * 推送Gcs任务指令
	 * @param containerNo
	 * @param layer
	 * @param taskType
	 * @param priority
	 * @param locIdFrom
	 * @param locIdTo
	 * @throws Exception
	 */
	public String sendGcsTaskPush(String containerNo,int layer,int taskType,
			int priority,String locIdFrom,String locIdTo) throws Exception;
	
	/**
	 * GCS任务回告
	 * @param gcsOrderReportReqDto
	 * @throws Exception
	 */
	public void gcsTaskReport(GcsOrderReportReqDto gcsOrderReportReqDto) throws Exception;
	
	/**
	 * GCS故障上报
	 * @param gcsAlarmReqDto
	 * @throws Exception
	 */
	public void gcsAlarm(GcsAlarmReqDto gcsAlarmReqDto)throws Exception;
	
	/**
	 * 修改或者新增
	 * @param containerNo
	 * @param layer
	 * @param taskType
	 * @param priority
	 * @param locIdFrom
	 * @param locIdTo
	 * @param uuid
	 * @param taskState
	 * @param sendCount
	 * @param errMsg
	 * @param type 1:新增 2：修改
	 * @throws Exception
	 */
	public void saveOrUpdateGcsTask(String containerNo,int layer,int taskType,
			int priority,String locIdFrom,String locIdTo,String uuid,
			int taskState,int sendCount,String errMsg,int type) throws Exception;
	
	/**
	 * 任务完成
	 * @param id
	 */
	public void completeGcsTask(String id) throws Exception ;
	
	/**
	 * 重发
	 * @param gcsTask
	 * @throws Exception
	 */
	public void recall(GcsTask gcsTask)throws Exception;
	
	/**
	 * 查询需要重发的GCS任务
	 * @return
	 * @throws Exception
	 */
	public List<GcsTask> findRecallGCSTask()throws Exception;


	/**
	 * 回告gcs跨层完成
	 * @param carTaskId
	 */
	void sendCarAcrossReport(String carTaskId);

	/**
	 * 发送gcs小车跨层任务
	 *//*
	void sendGcsCarAcrossPush(SxCarAcrossTask sxCarAcrossTask, SxCarAcross sxCarAcross) throws Exception;*/

	/**
	 * GCS跨层任务回告
	 * @param sxCarAcrossTask
	 * @throws Exception
	 */
	void crossLayerReport(SxCarAcrossTask sxCarAcrossTask) throws Exception;

	/**
	 * 同步gcs小车数据
	 */
	void syncGcsCar() throws Exception;
}
