package com.prolog.eis.service.mcs.impl;

import com.prolog.eis.dao.ContainerTaskMapper;
import com.prolog.eis.dao.mcs.MCSTaskMapper;
import com.prolog.eis.model.mcs.MCSTask;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.service.mcs.McsInterfaceService;
import com.prolog.eis.util.PrologStringUtils;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class McsInterfaceServiceImpl implements McsInterfaceService{


	@Autowired
	private MCSTaskMapper mcsTaskMapper;
	@Autowired
	private ContainerTaskMapper containerTaskMapper;

	@Override
	public void updateBuildEmptyContainerSupply(boolean exit,String rcsPositionCode){
		if(!exit) {
			//创建agv区域的空托补给任务
			ContainerTask containerTask = new ContainerTask();
			//空托盘没有托盘号
			containerTask.setContainerCode(PrologStringUtils.newGUID());
			containerTask.setTaskType(6);
			containerTask.setSource(rcsPositionCode);
			containerTask.setSourceType(2);
			containerTask.setTarget("");
			containerTask.setTargetType(0);
			containerTask.setTaskState(1);
			containerTask.setQty(1d);
			containerTask.setCreateTime(new Date());

			containerTaskMapper.save(containerTask);
		}
	}

	@Override
	public List<MCSTask> findFailMCSTask(){
 		List<MCSTask> mcsTasks = mcsTaskMapper.findByMap(MapUtils.put("taskState", 2).getMap(), MCSTask.class);
		return mcsTasks;
	}

	@Override
public void updatesendMcsTaskWithOutPathAsyc(McsTaskWithOutPathAsycDto mcsTaskWithOutPathAsycDto,int type, String containerNo, String address, String target, String weight, String priority,int state )
{
 	String taskId=mcsTaskWithOutPathAsycDto.getTaskId();
	boolean isSuccess=mcsTaskWithOutPathAsycDto.isSuccess();
	if (!isSuccess) {

		//失败记重发表
		MCSTask mcsTask = new MCSTask();
		mcsTask.setTaskId(taskId);
		mcsTask.setBankId(1);
		mcsTask.setPriority(priority);
		mcsTask.setAddress(address);
		mcsTask.setContainerNo(containerNo);
		mcsTask.setTarget(target);
		mcsTask.setType(type);
		mcsTask.setWeight(weight);
		mcsTask.setStatus(state);
		mcsTask.setSendCount(1);
		mcsTask.setCreateTime(new Date());
		mcsTask.setTaskState(2);
		mcsTask.setErrMsg(mcsTaskWithOutPathAsycDto.getMessage());
		mcsTaskMapper.save(mcsTask);


	}

}
     @Override
	public void updateRecallMcsTask(RecallMcsTaskDto recallMcsTaskDto,MCSTask mcsTask){
 		if (recallMcsTaskDto.isSuccess()) {
			mcsTaskMapper.deleteById(mcsTask.getId(), MCSTask.class);
		} else {
			mcsTaskMapper.update(mcsTask);
		}

	}





}
