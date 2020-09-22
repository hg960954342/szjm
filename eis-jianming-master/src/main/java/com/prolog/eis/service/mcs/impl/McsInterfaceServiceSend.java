package com.prolog.eis.service.mcs.impl;

import com.prolog.eis.dao.mcs.MCSTaskMapper;
import com.prolog.eis.dto.eis.mcs.McsSendTaskDto;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.mcs.MCSTask;
import com.prolog.eis.service.mcs.McsInterfaceService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.eis.util.PrologHttpUtils;
import com.prolog.eis.util.PrologTaskIdUtils;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class McsInterfaceServiceSend  {

	@Autowired
	private RestTemplate restTemplate;
	@Value("${prolog.mcs.url:}")
	private String mcsUrl;

	@Value("${prolog.mcs.port:}")
	private String mcsPort;
	@Autowired
	private MCSTaskMapper mcsTaskMapper;
	@Autowired
	private McsInterfaceService  mcsInterfaceService;


	public void sendMcsTaskWithOutPathAsyc(int type, String containerNo, String address, String target, String weight, String priority,int state)
		{
		List<McsSendTaskDto> mcsSendTaskDtos = new ArrayList<McsSendTaskDto>();
		McsTaskWithOutPathAsycDto mcsTaskWithOutPathAsycDto=new McsTaskWithOutPathAsycDto();
		McsSendTaskDto mcsSendTaskDto = new McsSendTaskDto();
		String taskId = PrologTaskIdUtils.getTaskId();
		mcsSendTaskDto.setTaskId(taskId);
		mcsSendTaskDto.setType(type);
		mcsSendTaskDto.setBankId(1);
		mcsSendTaskDto.setContainerNo(containerNo);
		mcsSendTaskDto.setAddress(address);
		mcsSendTaskDto.setTarget(target);
		mcsSendTaskDto.setPriority(priority);
		mcsSendTaskDto.setWeight(weight);
		mcsSendTaskDto.setStatus(state);
		mcsSendTaskDtos.add(mcsSendTaskDto);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("carryList", mcsSendTaskDtos);
		try {
			String data = PrologApiJsonHelper.toJson(map);
			String restJson = "";
			String postUrl = String.format("http://%s:%s%s", mcsUrl, mcsPort, "/Interface/Request");
			restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(data), String.class);
			LogServices.log(postUrl,data,"",restJson);
			PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
			Boolean sucssess = helper.getBoolean("ret");
			String message = helper.getString("msg");
			mcsTaskWithOutPathAsycDto.setSuccess(sucssess);
			mcsTaskWithOutPathAsycDto.setMessage(message);
			LogServices.log(postUrl,data,message,restJson);

			mcsTaskWithOutPathAsycDto.setTaskId(taskId);
 		} catch (Exception e) {

			LogServices.logSys(e);
			mcsTaskWithOutPathAsycDto.setSuccess(false);
			mcsTaskWithOutPathAsycDto.setMessage(LogServices.spliitString(e.getMessage()));
			mcsTaskWithOutPathAsycDto.setTaskId(taskId);


		}
			mcsInterfaceService.updatesendMcsTaskWithOutPathAsyc(mcsTaskWithOutPathAsycDto,type,  containerNo,  address,  target,  weight,  priority, state);

	}





	public void recall(MCSTask mcsTask)   {
		List<McsSendTaskDto> mcsSendTaskDtos = new ArrayList<McsSendTaskDto>();
		RecallMcsTaskDto recallMcsTaskDto=new RecallMcsTaskDto();
		McsSendTaskDto mcsSendTaskDto = new McsSendTaskDto();
		mcsSendTaskDto.setTaskId(mcsTask.getTaskId());
		mcsSendTaskDto.setType(mcsTask.getType());
		mcsSendTaskDto.setContainerNo(mcsTask.getContainerNo());
		mcsSendTaskDto.setTarget(mcsTask.getTarget());
		mcsSendTaskDto.setAddress(mcsTask.getAddress());
		mcsSendTaskDto.setPriority(mcsTask.getPriority());
		mcsSendTaskDto.setWeight(mcsTask.getWeight());
		mcsSendTaskDtos.add(mcsSendTaskDto);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("carryList", mcsSendTaskDtos);
		try {
			String data = PrologApiJsonHelper.toJson(map);
			String restJson = "";
			String postUrl = String.format("http://%s:%s%s", mcsUrl, mcsPort, "/Interface/Request");
			restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(data), String.class);
			PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
			Boolean sucssess = helper.getBoolean("ret");
			String message = helper.getString("msg");
			if (!sucssess){
				LogServices.log(postUrl,data,message,restJson);
				mcsTask.setTaskState(2);
				mcsTask.setErrMsg(message);
				mcsTask.setSendCount(mcsTask.getSendCount()+1);
 			}
			LogServices.log(postUrl,data,message,restJson);
			recallMcsTaskDto.setSuccess(sucssess);
			recallMcsTaskDto.setMcsTask(mcsTask);

		} catch (Exception e) {
			mcsTask.setSendCount(mcsTask.getSendCount()+1);
			mcsTask.setTaskState(2);
			mcsTask.setErrMsg(e.getMessage());
			LogServices.logSys(e);
			recallMcsTaskDto.setSuccess(false);
			recallMcsTaskDto.setMcsTask(mcsTask);
 		}
 		mcsInterfaceService.updateRecallMcsTask(recallMcsTaskDto,mcsTask);
	}
	

	public boolean getExitStatus(String position) throws Exception {
		List<Map<String, Object>> coord = new ArrayList<Map<String, Object>>();
		coord.add(MapUtils.put("coord", position).getMap());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("carryList", coord);
		String requestData = PrologApiJsonHelper.toJson(map);
		String restJson = "";
		try {
			String postUrl = String.format("http://%s:%s%s", mcsUrl, mcsPort, "/Interface/getExitStatus");
 			restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(requestData), String.class);

			PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
			Boolean sucssess = helper.getBoolean("ret");
			String message = helper.getString("msg");
			List<Map> data = helper.getObjectList("data", Map.class);
			LogServices.log(postUrl,requestData,message,restJson);
			if (sucssess && !data.isEmpty()) {
				Map<String, Object> m = data.get(0);
				// isEmpty：Boolean【是否为空 true为空，false 不为空】
				boolean isEmpty = (boolean) m.get("empty");
				return isEmpty;
			}else {
				LogServices.log(postUrl,requestData,message,restJson);
				//FileLogHelper.WriteLog("getExitStatusError", "EIS->MCS接驳口状态查询，响应失败："+message);
				return true;
			}
		} catch (Exception e) {
			LogServices.logSys(e);
			//FileLogHelper.WriteLog("getExitStatusError", "EIS->MCS接驳口状态查询，接口调用异常："+e.getMessage());
			return true;
		}
	}
}
