package com.prolog.eis.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prolog.eis.dto.eis.mcs.InBoundRequest;
import com.prolog.eis.dto.eis.mcs.TaskReturnInBoundRequestResponse;
import com.prolog.eis.service.store.QcInBoundTaskService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.PrologApiJsonHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

@RestController
@Api(tags = "MCS接口")
@RequestMapping("/wcs/task")
public class PrologJmMCSController {

	@Autowired
	private QcInBoundTaskService qcInBoundTaskService;
	
	@ApiOperation(value = "提升机任务回告", notes = "提升机任务回告")
	@PostMapping("/callback")
	public void taskReturn(@RequestBody String json, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		OutputStream out = response.getOutputStream();
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);

		try {
			FileLogHelper.WriteLog("McsInterfaceCallback", "MCS->EIS传输" + json);

			//下发任务号
			String taskId = helper.getString("taskId");
			//状态 1:任务开始 2：任务完成 3：任务异常
			int status = helper.getInt("status");
			//任务类型：1：入库 2：出库 3:移库 4:小车换层 5:输送线行走
			int type = helper.getInt("type");
			//料箱编号
			String containerNo = helper.getString("containerNo");
			//小车编号
			String rgvId = helper.getString("rgvId");
			//当前点位
			String address = helper.getString("address");
			
			try {
				qcInBoundTaskService.taskReturn(taskId,status,type,containerNo,rgvId,address);
				
				String resultStr = getLhMcsValue(true,"操作成功","200",new ArrayList<TaskReturnInBoundRequestResponse>());
				out.write(resultStr.getBytes("UTF-8"));
				out.flush();
				out.close();

				FileLogHelper.WriteLog("McsInterfaceCallback", "EIS->MCS返回" + resultStr);
			}catch (Exception e) {
				// TODO: handle exception
				
				FileLogHelper.WriteLog("McsInterfaceCallbackError", "mcs返回异常" + e.toString());
				String resultStr = getLhMcsValue(false,e.toString(),"100",new ArrayList<TaskReturnInBoundRequestResponse>());
				out.write(resultStr.getBytes("UTF-8"));
				out.flush();
				out.close();

				FileLogHelper.WriteLog("McsInterfaceCallback", "EIS->MCS返回" + resultStr);
			}
		} catch (Exception e) {

			FileLogHelper.WriteLog("McsInterfaceCallbackError", "mcs返回异常" + e.toString());
			String resultStr = getLhMcsValue(false,e.toString(),"100",new ArrayList<TaskReturnInBoundRequestResponse>());
			out.write(resultStr.getBytes("UTF-8"));
			out.flush();
			out.close();

			FileLogHelper.WriteLog("McsInterfaceCallback", "EIS->MCS返回" + resultStr);
		}
	}
	
	private <T> String getLhMcsValue(Boolean success,String msg,String code, List<T> data) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success", success);
		jsonObject.put("message", msg);
		jsonObject.put("code", code);
		jsonObject.put("data", data);
		return jsonObject.toString();
	}
}
