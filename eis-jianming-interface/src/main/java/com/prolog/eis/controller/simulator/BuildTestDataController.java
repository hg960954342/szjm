package com.prolog.eis.controller.simulator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prolog.eis.model.middle.WmsRawTrkInterface;
import com.prolog.eis.service.simulator.BuildTestDataService;
import com.prolog.framework.common.message.RestMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "数据生成")
@RequestMapping("/api/v1/master/buildTestData")
public class BuildTestDataController {
	
	@Autowired
	private BuildTestDataService buildTestDataService;
	
	@ApiOperation(value = "出入库任务数据生成", notes = "出入库任务数据生成")
	@PostMapping("/wmsRawTrkInterface")
	public RestMessage<Long> wmsRawTrkInterface(@RequestBody List<WmsRawTrkInterface> list) throws Exception {
		try {
			long t = buildTestDataService.wmsRawTrkInterface(list);
			return RestMessage.newInstance(true, "保存成功", t);
		} catch (Exception e) {
			return RestMessage.newInstance(false, "保存失败:" + e.getMessage(), null);
		}
	}
	
	@ApiOperation(value = "出入库任务数据生成", notes = "出入库任务数据生成")
	@PostMapping("/getwmsrawtrkinterfaces")
	public RestMessage<List<WmsRawTrkInterface>> getWmsRawTrkInterfaces() throws Exception {
		try {
			List<WmsRawTrkInterface> list = buildTestDataService.getWmsRawTrkInterface();
			
			return RestMessage.newInstance(true, "保存成功", list);
		} catch (Exception e) {
			return RestMessage.newInstance(false, "保存失败:" + e.getMessage(), null);
		}
	}
}
