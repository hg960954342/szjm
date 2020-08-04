package com.prolog.eis.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "GCS接口")
@RequestMapping("/api/v1/master/gcs/interface")
public class PrologGCSController {

	/*@Autowired
	private GcsInterfaceService gcsInterfaceService;

	@ApiOperation(value = "任务回告指令", notes = "任务回告指令")
	@PostMapping("/GcsOrderReport")
	public void gcsRequest(@RequestBody String json, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		OutputStream out = response.getOutputStream();
		try {
			FileLogHelper.WriteLog("GCS任务回告", "GCS->EIS请求" + json);
			
			List<GcsOrderReportReqDto> gcsOrderReportReqDtos = PrologApiJsonHelper.getArrayList(json, GcsOrderReportReqDto.class);

			String errorMsg = "";
			List<GCSErrorDto> errorList = new ArrayList<GCSErrorDto>();
			for (GcsOrderReportReqDto gcsOrderReportReqDto : gcsOrderReportReqDtos) {
				try {
					gcsInterfaceService.gcsTaskReport(gcsOrderReportReqDto);	
				}catch (Exception e) {
					// TODO: handle exception
					errorMsg = errorMsg + " " + e.getMessage();
					
					GCSErrorDto gcsErrorDto = new GCSErrorDto();
					gcsErrorDto.setTaskId(gcsOrderReportReqDto.getBillCode());
					gcsErrorDto.setErrMsg(e.getMessage());
					
					errorList.add(gcsErrorDto);
				}
			}
			
			if(errorList.isEmpty()) {
				String resultStr = PrologApiJsonHelper.getGcsValue(new ArrayList<TaskReturnInBoundRequestResponse>());
				out.write(resultStr.getBytes("UTF-8"));
				out.flush();
				out.close();
				
				FileLogHelper.WriteLog("GCS任务回告", "GCS->EIS返回" + resultStr);
			}else {
				FileLogHelper.WriteLog("GCS任务回告Error", "错误信息：\n" + errorMsg);
				String resultStr = PrologApiJsonHelper.getGcsValue(errorList);
				out.write(resultStr.getBytes("UTF-8"));
				out.flush();
				out.close();

				FileLogHelper.WriteLog("GCS任务回告", "EIS->MCS返回" + resultStr);
			}
		}catch (Exception e) {
			FileLogHelper.WriteLog("GCS任务回告Error", e.getMessage());
			
			// TODO: handle exception
			String retultErr = PrologApiJsonHelper.setOutGcsErr(e.getMessage());
			out.write(retultErr.getBytes("UTF-8"));
			out.flush();
			out.close();
			
			FileLogHelper.WriteLog("GCS任务回告", "GCS->EIS返回" + retultErr);
		}
	}

	@ApiOperation(value = "GCS故障数据", notes = "GCS故障数据")
	@PostMapping("/GcsAlarm")
	public void gcsAlarm(@RequestBody String json, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		OutputStream out = response.getOutputStream();

		try {
			FileLogHelper.WriteLog("GCS故障数据", "GCS->EIS请求" + json);
			
			List<GcsAlarmReqDto> gcsAlarmReqDtos = PrologApiJsonHelper.getArrayList(json,GcsAlarmReqDto.class);
			if(gcsAlarmReqDtos.size() > 0) {
				gcsInterfaceService.gcsAlarm(gcsAlarmReqDtos.get(0));
			}else {
				throw new Exception("请求参数集合不能为空！");
			}
			String resultStr = PrologApiJsonHelper.getGcsValue(gcsAlarmReqDtos);
			out.write(resultStr.getBytes("UTF-8"));
			out.flush();
			out.close();
			
			FileLogHelper.WriteLog("GCS故障数据", "GCS->EIS返回" + resultStr);
		} catch (Exception e) {
			String retultErr = PrologApiJsonHelper.setOutGcsErr(e.toString());
			out.write(retultErr.getBytes("UTF-8"));
			out.flush();
			out.close();
			
			FileLogHelper.WriteLog("GCS故障数据", "GCS->EIS返回" + retultErr);
		}
	}

	@ApiOperation(value = "GCS跨层任务回告", notes = "GCS跨层任务回告")
	@PostMapping("/CrossLayerReport")
	public WcsPublicResponseDto<SxCarAcrossTask> crossLayerReport(@RequestBody SxCarAcrossTask sxCarAcrossTask) throws Exception {
		WcsPublicResponseDto<SxCarAcrossTask> instance;
		try {
			FileLogHelper.WriteLog("CrossLayerReport", "GCS->EIS GCS跨层任务回告" + JSON.toJSONString(sxCarAcrossTask));

			gcsInterfaceService.crossLayerReport(sxCarAcrossTask);
			instance = WcsPublicResponseDto.newInstance(true, "操作成功", null);
		} catch (Exception e) {
			e.printStackTrace();
			instance = WcsPublicResponseDto.newInstance(false, e.getMessage(), sxCarAcrossTask);
		}
		FileLogHelper.WriteLog("CrossLayerReport", "GCS->EIS GCS跨层任务回告,EIS返回:" + JSON.toJSONString(instance));
		return instance;
	}*/
}
