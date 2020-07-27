package com.prolog.eis.controller;

import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prolog.eis.service.MCSLineService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.PrologApiJsonHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

@RestController
@Api(tags = "输送线体和设备相关的接口")
@RequestMapping("/api/v1/sxk/mcsline")
public class PrologMCSLineController {

	/*@Autowired
	private MCSLineService mCSLineService;

	@ApiOperation(value = "WCS-EIS叠盘机叠满请求入库", notes = "WCS-EIS叠盘机叠满请求入库")
	@PostMapping("/foldInBound")
	public void foldInBound(@RequestBody String json, HttpServletResponse response) throws Exception {

		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		OutputStream out = response.getOutputStream();
		try {
			PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);

			FileLogHelper.WriteLog("mcsfoldInBound", "MCS->EIS请求" + json);

			String deviceNo = helper.getString("deviceNo");
			String containerNo = helper.getString("containerNo");

			//生成空托盘任务
			mCSLineService.emptyPalletRequest(deviceNo, containerNo);

			String resultStr = this.getMcsValue(true,"请求成功","200",null);

			out.write(resultStr.getBytes("UTF-8"));
			out.flush();
			out.close();
			
			FileLogHelper.WriteLog("mcsfoldInBound", "MCS->EIS" + resultStr);
		}
		catch (Exception e) {
			// TODO: handle exception
			String resultStr = this.getMcsValue(true,"执行失败:" + e.getMessage(),"200",null);

			out.write(resultStr.getBytes("UTF-8"));
			out.flush();
			out.close();
			
			FileLogHelper.WriteLog("mcsfoldInBoundError", "MCS->EIS返回" + e.getMessage());
		}
	}

	@ApiOperation(value = "WCS-EIS拆盘机为空请求出库", notes = "WCS-EIS拆盘机为空请求出库")
	@PostMapping("/splitOutBound")
	public void splitOutBound(@RequestBody String json, HttpServletResponse response) throws Exception {
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		OutputStream out = response.getOutputStream();
		try {
			PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
			FileLogHelper.WriteLog("mcssplitOutBound", "MCS->EIS请求" + json);

			String deviceNo = helper.getString("deviceNo");

			mCSLineService.splitOutBound(deviceNo);

			String resultStr = this.getMcsValue(true,"请求成功","200",null);
			
			out.write(resultStr.getBytes("UTF-8"));
			out.flush();
			out.close();
			
			FileLogHelper.WriteLog("mcssplitOutBound", "MCS->EIS请求返回" + resultStr);
		}catch (Exception e) {
			// TODO: handle exception
			FileLogHelper.WriteLog("mcssplitOutBoundError", "MCS->EIS返回" + e.toString());

			String resultStr = this.getMcsValue(true,"执行失败" + e.getMessage(),"100",null);
			
			out.write(resultStr.getBytes("UTF-8"));
			out.flush();
			out.close();
		}
	}

	@ApiOperation(value = "BCR扫码请求", notes = "BCR扫码请求")
	@PostMapping("/bcrrequest")
	public void bcrRequest(@RequestBody String json, HttpServletResponse response) throws Exception {
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		OutputStream out = response.getOutputStream();
		try {
			PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);

			FileLogHelper.WriteLog("mcsbcrrequest", "MCS->EIS请求" + json);

			String deviceNo = helper.getString("deviceNo");
			String containerNo = helper.getString("containerNo");

			String result = "";
			synchronized ("sxkchuku".intern()) {
				result = mCSLineService.bcrRequest(deviceNo,containerNo);
			}
			
			String resultStr = this.getMcsValue(true,"请求成功","200",result);
			
			out.write(resultStr.getBytes("UTF-8"));
			out.flush();
			out.close();
			
			FileLogHelper.WriteLog("mcsbcrrequest", "MCS->EIS请求返回" + resultStr);
		}catch (Exception e) {
			// TODO: handle exception
			String errorStack = e.getStackTrace().length > 0 ? e.getStackTrace()[0].getClassName() + e.getStackTrace()[0].getLineNumber() : "";
			FileLogHelper.WriteLog("mcsbcrrequestError", "MCS->EIS请求" + errorStack);

			String resultStr = this.getMcsValue(true,"请求失败" + e.getMessage(),"100","0");
			
			out.write(resultStr.getBytes("UTF-8"));
			out.flush();
			out.close();
		}
	}

	@ApiOperation(value = "WCS-EIS叠盘机前面线体状态", notes = "WCS-EIS叠盘机前面线体状态")
	@PostMapping("/foldemptystate")
	public void foldEmptyState(@RequestBody String json, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		OutputStream out = response.getOutputStream();
		
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		String deviceNo = helper.getString("deviceNo");
		int state = helper.getInt("state");

		if(state == 1) {
			//空闲
			//agvStorageLocationService.foldEmptyState(deviceNo);
		}

		String resultStr = this.getMcsValue(true,"请求成功","200",null);
		
		out.write(resultStr.getBytes("UTF-8"));
		out.flush();
		out.close();
	}

	private <T> String getMcsValue(Boolean success,String msg,String code, T data) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("ret", success);
		jsonObject.put("msg", msg);
		jsonObject.put("code", code);
		jsonObject.put("data", data);
		return jsonObject.toString();
	}*/
}
