package com.prolog.eis.service.rcs.impl;

import com.prolog.eis.model.eis.ContainerTask;
import com.prolog.eis.util.PrologApiJsonHelper;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prolog.eis.service.rcs.AgvCallbackService;

import java.util.List;

@Service
public class AgvCallbackServiceImpl implements AgvCallbackService{

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void agvCallback(String taskCode,String method) throws Exception{
		
		
	}

	/**
	 * 回告wms
	 */
	private void eisCallback(List<ContainerTask> containerTasks) throws Exception {
		String json = PrologApiJsonHelper.toJson(containerTasks);

	}
	private String returnSuccess(String reqCode) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", "0");
		jsonObject.put("message", "成功");
		jsonObject.put("reqCode", reqCode);
		jsonObject.put("data", "");

		return jsonObject.toString();
	}

	private String returnError(String reqCode,String errorMsg) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", "99");
		jsonObject.put("message", errorMsg);
		jsonObject.put("reqCode", reqCode);
		jsonObject.put("data", "");

		return jsonObject.toString();
	}
}
