package com.prolog.eis.controller;

import com.prolog.eis.dto.eis.mcs.InBoundRequest;
import com.prolog.eis.dto.eis.mcs.McsRequestTaskDto;
import com.prolog.eis.dto.eis.mcs.TaskReturnInBoundRequestResponse;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.service.MCSLineService;
import com.prolog.eis.service.mcs.impl.McsInterfaceServiceSend;
import com.prolog.eis.service.mcs.impl.McsTaskWithOutPathAsycDto;
import com.prolog.eis.service.store.QcInBoundTaskService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Api(tags = "MCS接口")
@RequestMapping("/wcs/task")
public class PrologJmMCSController {

	@Autowired
	private QcInBoundTaskService qcInBoundTaskService;
	@Autowired
	private McsInterfaceServiceSend mcsInterfaceServiceSend;
	@Autowired
	private MCSLineService mcsLineService;
	@Autowired
	com.prolog.eis.service.mcs.McsInterfaceService mcsInterfaceService;

	@ApiOperation(value = "提升机请求", notes = "提升机请求")
	@PostMapping("/mcsRequest")
	public void mcsRequest(@RequestBody String json, HttpServletResponse response) throws Exception {

		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		OutputStream out = response.getOutputStream();
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		try {
			List<InBoundRequest> inBoundRequests = helper.getObjectList("carryList", InBoundRequest.class);
			//根据原点，去除重复集合
			List<InBoundRequest> newInBoundRequests = dictinctInBoundRequest(inBoundRequests);
			
			String errorMsg = "";

			List<McsRequestTaskDto> sendList = new ArrayList<McsRequestTaskDto>(); 
			for(int i=0;i<newInBoundRequests.size();i++) {
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
						LogServices.logSys("MCS->EIS返回" + e.getMessage());
						// TODO: handle exception
						errorMsg = errorMsg + " " + e.getMessage();
					}
				}
			}

			String resultStr = getJmMcsValue(true,errorMsg,"200",new ArrayList<TaskReturnInBoundRequestResponse>());
			out.write(resultStr.getBytes("UTF-8"));
			out.flush();
			out.close();
			//int type, String containerNo, String address, String target, String weight, String priority,int state
			//给mcs发指令
			for (McsRequestTaskDto mcsRequestTaskDto : sendList) {
				mcsInterfaceServiceSend.sendMcsTaskWithOutPathAsyc(mcsRequestTaskDto.getType(),
						mcsRequestTaskDto.getStockId(), 
						mcsRequestTaskDto.getSource(),
						mcsRequestTaskDto.getTarget(),
						"0", "99",0);


			}
		}catch (Exception e) {
			// TODO: handle exception
 			String resultStr = getJmMcsValue(false,e.toString(),"100",new ArrayList<TaskReturnInBoundRequestResponse>());
			out.write(resultStr.getBytes("UTF-8"));
			out.flush();
			out.close();
 			LogServices.logSys("MCS->EIS返回" + resultStr);
		}
	}
	
	private List<InBoundRequest> dictinctInBoundRequest(List<InBoundRequest> list){
		
		HashMap<String,InBoundRequest> hashMap = new HashMap<String,InBoundRequest>();
		
		for (InBoundRequest inBoundRequest : list) {
			if(!hashMap.containsKey(inBoundRequest.getSource())) {
				hashMap.put(inBoundRequest.getSource(),inBoundRequest);
			}else {
				//判断后面的条码是否为noRead
				if(!"noRead".equals(inBoundRequest.getStockId())) {
					hashMap.put(inBoundRequest.getSource(),inBoundRequest);
				}
			}
		}
		
		return new ArrayList<InBoundRequest>(hashMap.values());
	}
	
	
	@ApiOperation(value = "提升机任务回告", notes = "提升机任务回告")
	@PostMapping("/callback")
	public void taskReturn(@RequestBody String json, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		OutputStream out = response.getOutputStream();
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);

		try {

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
				
				String resultStr = getJmMcsValue(true,"操作成功","200",new ArrayList<TaskReturnInBoundRequestResponse>());
				out.write(resultStr.getBytes("UTF-8"));
				out.flush();
				out.close();

 			}catch (Exception e) {
				// TODO: handle exception
				
 				String resultStr = getJmMcsValue(false,e.toString(),"100",new ArrayList<TaskReturnInBoundRequestResponse>());
				out.write(resultStr.getBytes("UTF-8"));
				out.flush();
				out.close();

 				LogServices.logSys("EIS->MCS返回" + resultStr);
			}
		} catch (Exception e) {

			String resultStr = getJmMcsValue(false,e.toString(),"100",new ArrayList<TaskReturnInBoundRequestResponse>());
			out.write(resultStr.getBytes("UTF-8"));
			out.flush();
			out.close();
			LogServices.logSys("EIS->MCS返回" + resultStr);
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

			String deviceNo = helper.getString("deviceNo");

			mcsLineService.splitOutBound(deviceNo);
			
			String resultStr = getJmMcsValue(true,"操作成功","200",null);
			out.write(resultStr.getBytes("UTF-8"));
			out.flush();
			out.close();
			
 		}catch (Exception e) {
			// TODO: handle exception
 			LogServices.logSys(e.getMessage());
			String resultStr = this.getJmMcsValue(true,"执行失败" + e.getMessage(),"100",null);
			
			out.write(resultStr.getBytes("UTF-8"));
			out.flush();
			out.close();
		}
	}
	
	@ApiOperation(value = "WCS-EIS叠盘机叠满请求入库", notes = "WCS-EIS叠盘机叠满请求入库")
	@PostMapping("/foldInBound")
	public void foldInBound(@RequestBody String json, HttpServletResponse response) throws Exception {

		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		OutputStream out = response.getOutputStream();
		try {
			PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);


			String deviceNo = helper.getString("deviceNo");
			String containerNo = helper.getString("containerNo");

			//生成空托盘入库任务
			qcInBoundTaskService.foldInBound(deviceNo, containerNo);
			
			String resultStr = this.getJmMcsValue(true,"请求成功","200",null);

			out.write(resultStr.getBytes("UTF-8"));
			out.flush();
			out.close();
			
 		}
		catch (Exception e) {
			// TODO: handle exception
			LogServices.logSys( e.getMessage());
			String resultStr = this.getJmMcsValue(true,"执行失败:" + e.getMessage(),"200",null);

			out.write(resultStr.getBytes("UTF-8"));
			out.flush();
			out.close();
			

		}
	}
	
	private <T> String getJmMcsValue(Boolean success,String msg,String code, List<T> data) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("ret", success);
		jsonObject.put("msg", msg);
		jsonObject.put("code", code);
		jsonObject.put("data", data);
		return jsonObject.toString();
	}
}
