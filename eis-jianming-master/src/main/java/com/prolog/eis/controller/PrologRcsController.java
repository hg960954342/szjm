package com.prolog.eis.controller;

import com.prolog.eis.logs.LogServices;
import com.prolog.eis.service.enums.AgvMove;
import com.prolog.eis.service.rcs.AgvCallbackService;
import com.prolog.eis.util.PrologApiJsonHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "海康rcs接口")
@RequestMapping("/api/v1/agv/agvCallbackService")
public class PrologRcsController {

	@Autowired
	private AgvCallbackService agvCallbackService;
	
	@ApiOperation(value = "Rcs回告", notes = "Rcs回告")
	@PostMapping("/agvCallback")
	public String agvCallback(@RequestBody String json) throws Exception {
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		String reqCode = helper.getString("reqCode");
		try {
			String taskCode = helper.getString("taskCode");
			String method = helper.getString("method");
			if(StringUtils.isNotEmpty(taskCode)&&taskCode.endsWith(AgvMove.agvMoveTaskCodeEndPrex)){  //手动调用Agv搬运任务
				return returnSuccess(reqCode);
			}
			agvCallbackService.agvCallback(taskCode, method);
			String resultStr = returnSuccess(reqCode);
			return  resultStr;
		}
		catch (Exception e) {
			String resultStr = returnError(reqCode,e.getMessage());
			//String errorMsg = "RCS-> EIS[agvCallback]返回" + reqCode +" json:" + resultStr;
			LogServices.logSys(e);
            return resultStr;
		}
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
