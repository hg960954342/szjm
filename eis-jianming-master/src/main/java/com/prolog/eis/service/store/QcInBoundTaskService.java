package com.prolog.eis.service.store;

import com.prolog.eis.dto.eis.mcs.InBoundRequest;
import com.prolog.eis.dto.eis.mcs.McsRequestTaskDto;

public interface QcInBoundTaskService {

	McsRequestTaskDto inBoundTask(InBoundRequest inBoundRequest) throws Exception;
	
	//void taskReturn(InBoundRequest inBoundRequest) throws Exception;
}
