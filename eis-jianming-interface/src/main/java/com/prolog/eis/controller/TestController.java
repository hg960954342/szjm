package com.prolog.eis.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@Api(tags = "测试")
@RequestMapping("/api/v1/test")
public class TestController {
	/*@Autowired
	private MiddleTableSyncService middleTableSyncService;
	@Autowired
	private MiddleTableNoticeService middleTableNoticeService;
	
	@ApiOperation(value = "测试", notes = "测试")
	@PostMapping(value = "/test2")
	public RestMessage<Object> test2(@RequestBody String json) throws Exception {
		middleTableNoticeService.noticeWmsRawTrkInterfaceInbound();
		middleTableNoticeService.noticeWmsRawTrkInterfaceOutbound();
		return RestMessage.newInstance(true, "成功", null);
	}*/
}
