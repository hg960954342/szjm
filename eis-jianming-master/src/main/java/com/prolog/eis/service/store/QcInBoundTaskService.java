package com.prolog.eis.service.store;

import com.prolog.eis.dto.eis.mcs.InBoundRequest;
import com.prolog.eis.dto.eis.mcs.McsRequestTaskDto;

public interface QcInBoundTaskService {

	McsRequestTaskDto inBoundTask(InBoundRequest inBoundRequest) throws Exception;
	
	/**
	 * 任务回告
	 * @param taskId 下发任务号
	 * @param status 状态 1:任务开始 2：任务完成 3：任务异常
	 * @param type 任务类型：1：入库 2：出库 3:移库 4:小车换层 5:输送线行走
	 * @param containerNo 容器号
	 * @param rgvId 小车编号
	 * @param address 当前点位
	 * @throws Exception
	 */
	void taskReturn(String taskId,int status,int type,String containerNo,String rgvId,String address) throws Exception;
	
	/**
	 *agv到达输送线点位，发送mcs前进消息
	 * @param containerCode 容器号
	 * @param agvLocationId agv区域点位
	 */
	void rcsCompleteForward(String containerCode,int agvLocationId) throws Exception;
}
