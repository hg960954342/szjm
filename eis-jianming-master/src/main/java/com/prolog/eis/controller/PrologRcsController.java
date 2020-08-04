package com.prolog.eis.controller;

import com.prolog.eis.service.rcs.AgvCallbackService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.PrologApiJsonHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@RestController
@Api(tags = "海康rcs接口")
@RequestMapping("/api/v1/agv/agvCallbackService")
public class PrologRcsController {

	@Autowired
	private AgvCallbackService agvCallbackService;
	
	@ApiOperation(value = "Rcs回告", notes = "Rcs回告")
	@PostMapping("/agvCallback")
	public void agvCallback(@RequestBody String json, HttpServletResponse response) throws Exception {
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		OutputStream out = response.getOutputStream();
		
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		FileLogHelper.WriteLog("RCSInterface","RCS-> EIS[agvCallback]請求" + json);
		String reqCode = helper.getString("reqCode");
		
		try {
			String taskCode = helper.getString("taskCode");
			/*start : 任务开始
			outbin :
			走出储位
			end :
			任务结束*/
			String method = helper.getString("method");

			agvCallbackService.agvCallback(taskCode, method);

			String resultStr = returnSuccess(reqCode);
			
			out.write(resultStr.getBytes("UTF-8"));
			out.flush();
			out.close();
			FileLogHelper.WriteLog("RCSInterface","RCS-> EIS[agvCallback]返回" + reqCode +" json:" + resultStr);
		}
		catch (Exception e) {
			String resultStr = returnError(reqCode,e.getMessage());
			
			String errorMsg = "RCS-> EIS[agvCallback]返回" + reqCode +" json:" + resultStr;
			
			FileLogHelper.WriteLog("RCSInterface",errorMsg);
			FileLogHelper.WriteLog("RCSInterfaceError",errorMsg);

			out.write(resultStr.getBytes("UTF-8"));
			out.flush();
			out.close();
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
