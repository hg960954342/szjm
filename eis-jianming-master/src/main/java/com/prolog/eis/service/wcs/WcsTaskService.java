package com.prolog.eis.service.wcs;

import java.util.List;

import com.prolog.eis.dto.path.SxPathPlanningTaskDto;
import com.prolog.eis.model.path.SxPathPlanningTaskHz;

public interface WcsTaskService {

	//void sendWcsTask(Map<Integer, List<SxPathPlanningTaskDto>> sxPathPlanningTaskMxMap) throws Exception;
	
	List<SxPathPlanningTaskHz> findAll(SxPathPlanningTaskHz hz)throws Exception;
	
	void sendGcs(Integer taskHzId,List<SxPathPlanningTaskDto> vs,int priority) throws Exception;
	
	void sendMcs(List<SxPathPlanningTaskDto> vs) throws Exception;
}
