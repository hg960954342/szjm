package com.prolog.eis.service.mcs;

import com.prolog.eis.model.mcs.MCSTask;

import java.util.List;

public interface McsInterfaceService {

	//String sendMcsTask(int type,String stockId,String source,String target,String weight,int priority)throws Exception;

	String sendMcsTaskWithOutPathAsyc(int type, String containerNo, String source, String target, String weight, String priority,int state)
			throws Exception;
	
	void recall(MCSTask mcsTask)throws Exception;
	
	List<MCSTask> findFailMCSTask()throws Exception;
	
	boolean getExitStatus(String position) throws Exception;
}
