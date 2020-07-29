package com.prolog.eis.service.rcs;

import com.prolog.eis.dto.rcs.RcsRequestResultDto;

public interface RcsRequestService {

	/**
	 * 給Rcs发送搬运任务
	 * @param reqCode 任务编号
	 * @param containerNo 容器编号
	 * @param startPosition 起点位置
	 * @param endPosition 终点位置
	 * @param taskTyp  任务模板
	 * @param priority 优先级
	 * @return
	 */
	RcsRequestResultDto sendTask(String reqCode,String containerNo,String startPosition,String endPosition,String taskTyp,String priority);
}
