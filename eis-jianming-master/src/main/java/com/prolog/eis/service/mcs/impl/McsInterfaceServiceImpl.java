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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class McsInterfaceServiceImpl implements McsInterfaceService{

	@Autowired
	private RestTemplate restTemplate;
	@Value("${prolog.mcs.url:}")
	private String mcsUrl;

	@Value("${prolog.mcs.port:}")
	private String mcsPort;
	@Autowired
	private MCSTaskMapper mcsTaskMapper;

	/*public McsInterfaceServiceImpl() {
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(60000);
		httpRequestFactory.setConnectTimeout(60000);
		httpRequestFactory.setReadTimeout(60000);
		this.restTemplate=new RestTemplate(httpRequestFactory);
	}*/

	@Override
	@Transactional
	@Async
	public String sendMcsTaskWithOutPathAsyc(int type, String containerNo, String address, String target, String weight, String priority,int state)
			throws Exception {
		List<McsSendTaskDto> mcsSendTaskDtos = new ArrayList<McsSendTaskDto>();
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
		String data = PrologApiJsonHelper.toJson(map);
		String restJson = "";
		try {
			String postUrl = String.format("http://%s:%s%s", mcsUrl, mcsPort, "/Interface/Request");
			FileLogHelper.WriteLog("sendMCSTask", "EIS->MCS任务："+data);
			restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(data), String.class);
			FileLogHelper.WriteLog("sendMCSTask", "EIS->MCS返回："+restJson);
			PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
			Boolean sucssess = helper.getBoolean("ret");
			String message = helper.getString("msg");
			
			if (!sucssess) {
				LogServices.log(postUrl,data,message,restJson);
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
				mcsTask.setErrMsg(message);
				mcsTaskMapper.save(mcsTask);
			}
			
			return taskId;
		} catch (Exception e) {
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
			mcsTask.setErrMsg(e.getMessage());
			mcsTaskMapper.save(mcsTask);
			return taskId;


		}
	}

	/*@Override
	public String sendMcsTask(int type, String stockId, String source, String target, String weight, int priority)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}*/

	@Override
	public List<MCSTask> findFailMCSTask() throws Exception {
		// TODO Auto-generated method stub
		List<MCSTask> mcsTasks = mcsTaskMapper.findByMap(MapUtils.put("taskState", 2).getMap(), MCSTask.class);
		return mcsTasks;
	}

	@Override
	public void recall(MCSTask mcsTask) throws Exception {
		List<McsSendTaskDto> mcsSendTaskDtos = new ArrayList<McsSendTaskDto>();
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
		String data = PrologApiJsonHelper.toJson(map);
		String restJson = "";
		try {
			String postUrl = String.format("http://%s:%s%s", mcsUrl, mcsPort, "/Interface/Request");
			FileLogHelper.WriteLog("sendMCSTask", "EIS->MCS任务："+data);

			restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(data), String.class);
			FileLogHelper.WriteLog("sendMCSTask", "EIS->MCS返回："+restJson);
			PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
			Boolean sucssess = helper.getBoolean("ret");
			String message = helper.getString("msg");
			if (sucssess) {
				mcsTaskMapper.deleteById(mcsTask.getId(), MCSTask.class);
			} else {
				LogServices.log(postUrl,data,message,restJson);
				mcsTask.setTaskState(2);
				mcsTask.setErrMsg(message);
				mcsTask.setSendCount(mcsTask.getSendCount()+1);
				mcsTaskMapper.update(mcsTask);
			}
		} catch (Exception e) {
			mcsTask.setSendCount(mcsTask.getSendCount()+1);
			mcsTask.setTaskState(2);
			mcsTask.setErrMsg(e.getMessage());
			mcsTaskMapper.update(mcsTask);
		}
	}
	
	@Override
	public boolean getExitStatus(String position) throws Exception {

		List<Map<String, Object>> coord = new ArrayList<Map<String, Object>>();
		coord.add(MapUtils.put("coord", position).getMap());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("carryList", coord);
		String requestData = PrologApiJsonHelper.toJson(map);
		String restJson = "";
		try {
			String postUrl = String.format("http://%s:%s%s", mcsUrl, mcsPort, "/Interface/getExitStatus");
			FileLogHelper.WriteLog("getExitStatus", "EIS->MCS接驳口状态查询，请求参数："+requestData);
			restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(requestData), String.class);
			FileLogHelper.WriteLog("getExitStatus", "EIS->MCS接驳口状态查询，返回参数："+restJson);

			PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
			Boolean sucssess = helper.getBoolean("ret");
			String message = helper.getString("msg");
			List<Map> data = helper.getObjectList("data", Map.class);

			if (sucssess && !data.isEmpty()) {
				Map<String, Object> m = data.get(0);
				// isEmpty：Boolean【是否为空 true为空，false 不为空】
				boolean isEmpty = (boolean) m.get("empty");
				return isEmpty;
			}else {
				LogServices.log(postUrl,requestData,message,restJson);
				FileLogHelper.WriteLog("getExitStatusError", "EIS->MCS接驳口状态查询，响应失败："+message);
				return false;
			}
		} catch (Exception e) {
			FileLogHelper.WriteLog("getExitStatusError", "EIS->MCS接驳口状态查询，接口调用异常："+e.getMessage());
			return false;
		}
	}
}
