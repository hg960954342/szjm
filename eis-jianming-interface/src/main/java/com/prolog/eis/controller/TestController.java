package com.prolog.eis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prolog.eis.service.middle.MiddleTableNoticeService;
import com.prolog.eis.service.middle.MiddleTableSyncService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "测试")
@RequestMapping("/api/v1/test")
public class TestController {
	@Autowired
	private MiddleTableSyncService middleTableSyncService;
	@Autowired
	private MiddleTableNoticeService middleTableNoticeService;
	
	@ApiOperation(value = "测试", notes = "测试")
	@PostMapping(value = "/test2")
	public RestMessage<Object> test2(@RequestBody String json) throws Exception {
		middleTableNoticeService.noticeWmsRawTrkInterfaceInbound();
		middleTableNoticeService.noticeWmsRawTrkInterfaceOutbound();
		return RestMessage.newInstance(true, "成功", null);
	}
}
