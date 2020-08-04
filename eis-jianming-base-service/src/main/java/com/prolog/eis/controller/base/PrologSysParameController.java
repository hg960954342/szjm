package com.prolog.eis.controller.base;

import com.prolog.eis.model.base.SysParame;
import com.prolog.eis.service.base.SysParameService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "系统参数配置")
@RequestMapping("/api/v1/base/sysparameconfig")
public class PrologSysParameController {

	@Autowired
	private SysParameService sysParameService;
	
	@ApiOperation(value = "获取系统参数", notes = "获取系统参数")
	@PostMapping("/getsysparames")
	public RestMessage<List<SysParame>> getSysParames(@RequestBody String json) throws Exception {
		
		List<SysParame> list = sysParameService.getSysParames();
		
		return RestMessage.newInstance(true, "查询成功", list);
	}
	
	@ApiOperation(value = "设置系统参数", notes = "设置系统参数")
	@PostMapping("/setsysparamevalue")
	public RestMessage<String> setSysParameValue(@RequestBody String json) throws Exception {
		
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		String key = helper.getString("key");
		String value = helper.getString("value");
		
		sysParameService.setSysParameValue(key, value);
		
		return RestMessage.newInstance(true, "查询成功", null);
	}
}