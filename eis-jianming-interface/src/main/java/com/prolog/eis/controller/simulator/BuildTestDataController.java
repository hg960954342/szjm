package com.prolog.eis.controller.simulator;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@Api(tags = "数据生成")
@RequestMapping("/api/v1/master/buildTestData")
public class BuildTestDataController {
	
	/*@Autowired
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
	}*/
}
