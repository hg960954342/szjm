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
import com.prolog.eis.dto.eis.mcs.InBoundRequestResponse;
import com.prolog.eis.dto.eis.mcs.McsRequestTaskDto;
import com.prolog.eis.dto.eis.mcs.TaskReturnInBoundRequestResponse;
import com.prolog.eis.service.mcs.McsInterfaceService;
import com.prolog.eis.service.store.QcInBoundTaskService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.PrologApiJsonHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

/**
 * MCS接口
 * 
 * @author clarence_she
 *
 */
@RestController
@Api(tags = "MCS接口")
@RequestMapping("/api/v1/sxk/mcs")
public class PrologMCSController {

	@Autowired
	private QcInBoundTaskService qcInBoundTaskService;
	@Autowired
	private McsInterfaceService mcsInterfaceService;

	@ApiOperation(value = "提升机请求", notes = "提升机请求")
	@PostMapping("/mcsRequest")
	public void mcsRequest(@RequestBody String json, HttpServletResponse response) throws Exception {

		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		OutputStream out = response.getOutputStream();
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		try {
			FileLogHelper.WriteLog("McsInterface", "MCS->EIS请求" + json);

			List<InBoundRequest> inBoundRequests = helper.getObjectList("carryList", InBoundRequest.class);

			String errorMsg = "";

			List<McsRequestTaskDto> sendList = new ArrayList<McsRequestTaskDto>(); 
			for(int i=0;i<inBoundRequests.size();i++) {
				synchronized ("kucun".intern()) {
					try {
						McsRequestTaskDto mcsRequestTaskDto = qcInBoundTaskService.inBoundTask(inBoundRequests.get(i));
						if(null != mcsRequestTaskDto) {
							sendList.add(mcsRequestTaskDto);

							if(!mcsRequestTaskDto.isSuccess()) {
								errorMsg = errorMsg + " " + mcsRequestTaskDto.getErrorMessage();
							}
						}
					}
					catch (Exception e) {
						// TODO: handle exception
						errorMsg = errorMsg + " " + e.getMessage();
					}
				}
			}

			String resultStr = getQcMcsValue(true,errorMsg,"200",new ArrayList<InBoundRequestResponse>());
			out.write(resultStr.getBytes("UTF-8"));
			out.flush();
			out.close();

			FileLogHelper.WriteLog("McsInterface", "MCS->EIS返回" + resultStr);

			//int type, String containerNo, String address, String target, String weight, String priority,int state
			//给mcs发指令
			for (McsRequestTaskDto mcsRequestTaskDto : sendList) {
				mcsInterfaceService.sendMcsTaskWithOutPathAsyc(1, 
						mcsRequestTaskDto.getStockId(), 
						mcsRequestTaskDto.getSource(),
						mcsRequestTaskDto.getTarget(),
						"", "99",0);
			}
		}catch (Exception e) {
			// TODO: handle exception
			FileLogHelper.WriteLog("McsInterfaceError", "入库异常，错误信息：\n" + e.toString());
			String resultStr = getQcMcsValue(false,e.toString(),"100",new ArrayList<InBoundRequestResponse>());
			out.write(resultStr.getBytes("UTF-8"));
			out.flush();
			out.close();

			FileLogHelper.WriteLog("mcsRequest", "MCS->EIS返回" + resultStr);
		}
	}
	
	/*@ApiOperation(value = "提升机任务回告", notes = "提升机任务回告")
	@PostMapping("/taskReturn")
	public void taskReturn(@RequestBody String json, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		OutputStream out = response.getOutputStream();
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);

		try {
			FileLogHelper.WriteLog("McsReturnInterface", "MCS->EIS传输" + json);

			List<InBoundRequest> inBoundRequests = helper.getObjectList("carryList", InBoundRequest.class);
			List<TaskReturnInBoundRequestResponse> errorList = new ArrayList<TaskReturnInBoundRequestResponse>();
			String errorMsg = "";

			for(int i=0;i<inBoundRequests.size();i++) {
				try {
					//qcInBoundTaskService.taskReturn(inBoundRequests.get(i));
				}
				catch (Exception e) {
					errorMsg = errorMsg + " " + e.getMessage();
					// TODO: handle exception
					TaskReturnInBoundRequestResponse taskReturnInBoundRequestResponse = new TaskReturnInBoundRequestResponse();
					taskReturnInBoundRequestResponse.setTaskId(inBoundRequests.get(i).getTaskId());
					taskReturnInBoundRequestResponse.setErrMsg(e.getMessage());

					errorList.add(taskReturnInBoundRequestResponse);
				}
			}

			if(errorList.isEmpty()) {
				String resultStr = getLhMcsValue(true,"操作成功","200",new ArrayList<TaskReturnInBoundRequestResponse>());
				out.write(resultStr.getBytes("UTF-8"));
				out.flush();
				out.close();

				FileLogHelper.WriteLog("mcstaskReturn", "EIS->MCS返回" + resultStr);
			}else {
				FileLogHelper.WriteLog("mcstaskReturnError", "入库异常，错误信息：\n" + errorMsg);
				String resultStr = getLhMcsValue(true,errorMsg,"100",errorList);
				out.write(resultStr.getBytes("UTF-8"));
				out.flush();
				out.close();

				FileLogHelper.WriteLog("mcstaskReturn", "EIS->MCS返回" + resultStr);
			}
		} catch (Exception e) {

			FileLogHelper.WriteLog("mcstaskReturnError", "mcs返回异常" + e.toString());
			String resultStr = getLhMcsValue(false,e.toString(),"100",new ArrayList<TaskReturnInBoundRequestResponse>());
			out.write(resultStr.getBytes("UTF-8"));
			out.flush();
			out.close();

			FileLogHelper.WriteLog("mcstaskReturn", "EIS->MCS返回" + resultStr);
		}
	}*/

	/*@ApiOperation(value = "提升机任务回告", notes = "提升机任务回告")
	@PostMapping("/taskReturn")
	public void taskReturn(@RequestBody String json, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		OutputStream out = response.getOutputStream();
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);

		try {
			FileLogHelper.WriteLog("mcstaskReturn", "MCS->EIS传输" + json);

			List<InBoundRequest> inBoundRequests = helper.getObjectList("carryList", InBoundRequest.class);
			List<TaskReturnInBoundRequestResponse> errorList = new ArrayList<TaskReturnInBoundRequestResponse>();
			String errorMsg = "";

			for(int i=0;i<inBoundRequests.size();i++) {
				try {
					//qcInBoundTaskService.taskReturn(inBoundRequests.get(i));
				}
				catch (Exception e) {
					errorMsg = errorMsg + " " + e.getMessage();
					// TODO: handle exception
					TaskReturnInBoundRequestResponse taskReturnInBoundRequestResponse = new TaskReturnInBoundRequestResponse();
					taskReturnInBoundRequestResponse.setTaskId(inBoundRequests.get(i).getTaskId());
					taskReturnInBoundRequestResponse.setErrMsg(e.getMessage());

					errorList.add(taskReturnInBoundRequestResponse);
				}
			}

			if(errorList.isEmpty()) {
				String resultStr = getMcsValue(true,"操作成功","200",new ArrayList<TaskReturnInBoundRequestResponse>());
				out.write(resultStr.getBytes("UTF-8"));
				out.flush();
				out.close();

				FileLogHelper.WriteLog("mcstaskReturn", "EIS->MCS返回" + resultStr);
			}else {
				FileLogHelper.WriteLog("mcstaskReturnError", "入库异常，错误信息：\n" + errorMsg);
				String resultStr = getMcsValue(true,errorMsg,"100",errorList);
				out.write(resultStr.getBytes("UTF-8"));
				out.flush();
				out.close();

				FileLogHelper.WriteLog("mcstaskReturn", "EIS->MCS返回" + resultStr);
			}
		} catch (Exception e) {

			FileLogHelper.WriteLog("mcstaskReturnError", "mcs返回异常" + e.toString());
			String resultStr = getMcsValue(false,e.toString(),"100",new ArrayList<TaskReturnInBoundRequestResponse>());
			out.write(resultStr.getBytes("UTF-8"));
			out.flush();
			out.close();

			FileLogHelper.WriteLog("mcstaskReturn", "EIS->MCS返回" + resultStr);
		}
	}*/

	/*@ApiOperation(value = "提升机故障回告", notes = "提升机故障回告")
	@PostMapping("/breakdown")
	public void breakdown(@RequestBody String json, HttpServletResponse response) throws Exception {

		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		OutputStream out = response.getOutputStream();
		//PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		try {
			FileLogHelper.WriteLog("mcsbreakdown", "MCS->EIS传输" + json);

		} catch (Exception e) {
			FileLogHelper.WriteLog("mcsbreakdownError", "错误信息：\n" + e.toString());
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("ret", false);
			jsonObject.put("msg", e.toString());
			jsonObject.put("code", "200");
			jsonObject.put("data", "");
			out.write(jsonObject.toString().getBytes("UTF-8"));
			out.flush();
			out.close();
		}
	}*/

	private <T> String getQcMcsValue(Boolean success,String msg,String code, List<T> data) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("ret", success);
		jsonObject.put("msg", msg);
		jsonObject.put("code", code);
		jsonObject.put("data", data);
		return jsonObject.toString();
	}
	
	private <T> String getLhMcsValue(Boolean success,String msg,String code, List<T> data) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success", success);
		jsonObject.put("message", msg);
		jsonObject.put("code", code);
		jsonObject.put("data", data);
		return jsonObject.toString();
	}

	/*@ApiOperation(value = "MCS跨层任务回告", notes = "MCS跨层任务回告")
	@PostMapping("/CrossLayerReport")
	public WcsPublicResponseDto<SxCarAcrossTask> crossLayerReport(@RequestBody SxCarAcrossTask sxCarAcrossTask)
			throws Exception {
		WcsPublicResponseDto<SxCarAcrossTask> instance;
		try {
			FileLogHelper.WriteLog("CrossLayerReport", "MCS->EIS MCS跨层任务回告" + JSON.toJSONString(sxCarAcrossTask));
			mcsInterfaceService.crossLayerReport(sxCarAcrossTask);
			instance = WcsPublicResponseDto.newInstance(true, "操作成功", null);
		} catch (Exception e) {
			instance = WcsPublicResponseDto.newInstance(false, e.getMessage(), sxCarAcrossTask);
		}
		FileLogHelper.WriteLog("CrossLayerReport", "MCS->EIS MCS跨层任务回告,EIS返回:" + JSON.toJSONString(instance));
		return instance;
	}*/
}
