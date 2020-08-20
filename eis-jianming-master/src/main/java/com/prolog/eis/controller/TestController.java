package com.prolog.eis.controller;

import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import com.prolog.eis.service.store.QcInBoundTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.prolog.eis.dao.led.LedPortParamMapper;
import com.prolog.eis.dto.eis.led.LedPortParamDto;
import com.prolog.eis.service.test.TestService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.eis.util.led.LedUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

@RestController
@Api(tags = "test")
@RequestMapping("/api/v1/master/sxk/test")
public class TestController {
	
	@Autowired
	private TestService testService;
	
	@Autowired
	private LedPortParamMapper ledPortParamMapper;
	
	private Boolean isEmpty;
	
	public Boolean getIsEmpty() {
		return isEmpty;
	}

	public void setIsEmpty(Boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	@ApiOperation(value = "setIsEmpty", notes = "setIsEmpty")
	@PostMapping("/setIsEmpty")
	public void setIsEmpty(@RequestBody String json, HttpServletResponse response) throws Exception {

		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		OutputStream out = response.getOutputStream();
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		
		boolean isEmpty = helper.getBoolean("isEmpty");
		this.setIsEmpty(isEmpty);
		
		out.write("".getBytes("UTF-8"));
		out.flush();
		out.close();
	}

	@ApiOperation(value = "getExitStatus", notes = "getExitStatus")
	@PostMapping("/getExitStatus")
	public void getExitStatus(@RequestBody String json, HttpServletResponse response) throws Exception {

		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		OutputStream out = response.getOutputStream();
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		
		String result = "{\r\n" + 
				"	\"ret\": true,\r\n" + 
				"	\"msg\": \"操作成功\",\r\n" + 
				"	\"data\": [{\r\n" + 
				"		\"coord\": \"1029150002\",\r\n" + 
				"        \"isEmpty\": "+this.getIsEmpty()+",\r\n" + 
				"\r\n" + 
				"	}]\r\n" + 
				"}\r\n" + 
				"";
		
		out.write(result.getBytes("UTF-8"));
		out.flush();
		out.close();
	}

	@ApiOperation(value = "initLed", notes = "initLed")
	@PostMapping("/initLed")
	public void initLed(@RequestBody String json, HttpServletResponse response) throws Exception {

		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		OutputStream out = response.getOutputStream();
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		
		String ip = helper.getString("ip");
		String title = helper.getString("title");
		String stateTitle = helper.getString("stateTitle");
		String messageTitle = helper.getString("messageTitle");
		String ledSize = helper.getString("ledSize");
		
		LedUtil.initLed(ip, title, stateTitle, messageTitle, ledSize);
		
		out.write("success".getBytes("UTF-8"));
		out.flush();
		out.close();
	}

	@ApiOperation(value = "sendMsgToLed", notes = "sendMsgToLed")
	@PostMapping("/sendMsgToLed")
	public void sendMsgToLed(@RequestBody String json, HttpServletResponse response) throws Exception {

		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		OutputStream out = response.getOutputStream();
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		
		String ip = helper.getString("ip");
		String stateStr = helper.getString("stateStr");
		int messageType = helper.getInt("messageType");
		String message = helper.getString("message");
		String ledSize = helper.getString("ledSize");
		
		LedPortParamDto title = ledPortParamMapper.getLedPortParamByLedIp(ip);
		
		LedUtil.sendMsg(ip, stateStr, messageType, message, ledSize, title);
		
		out.write("".getBytes("UTF-8"));
		out.flush();
		out.close();
	}
	
	@ApiOperation(value = "移位", notes = "移位")
	@PostMapping("/storeLocationMove")
	public void storeLocationMove(@RequestBody String json, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		OutputStream out = response.getOutputStream();
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);

		try {
			FileLogHelper.WriteLog("storeLocationMove", json);
			String containerSubNo = helper.getString("containerSubNo");
			String binNo = helper.getString("binNo");
			
			//testService.storeLocationMove(containerSubNo, binNo);
			
			JSONObject jsonObject = new JSONObject();

			out.write(jsonObject.toString().getBytes("UTF-8"));
			out.flush();
			out.close();
		} catch (Exception e) {
			FileLogHelper.WriteLog("storeLocationMove", "异常："+e.getMessage());
			out.write(e.getMessage().getBytes("UTF-8"));
			out.flush();
			out.close();
		}
	}

	
	@ApiOperation(value = "库存删除接口", notes = "库存删除接口")
	@PostMapping("/deletestore")
	public void deleteStore(@RequestBody String json, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		OutputStream out = response.getOutputStream();
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);

		try {
			FileLogHelper.WriteLog("deletestore", json);
			String containerNo = helper.getString("containerNo");
			
			testService.deleteStore(containerNo);
			
			JSONObject jsonObject = new JSONObject();

			out.write(jsonObject.toString().getBytes("UTF-8"));
			out.flush();
			out.close();
		} catch (Exception e) {
			FileLogHelper.WriteLog("deletestoreError", "异常："+e.getMessage());
			out.write(e.getMessage().getBytes("UTF-8"));
			out.flush();
			out.close();
		}
	}

	@Autowired
	QcInBoundTaskService qcInBoundTaskService;
	//入库失败更新库存测试接口
	@RequestMapping("/update")
	@ResponseBody
	public String updateInBound(@RequestParam("id")String containerNo)throws Exception{

		return PrologApiJsonHelper.toJson(qcInBoundTaskService.rukuSxStoreUpdate(containerNo));
	}
}
