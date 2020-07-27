package com.prolog.eis.controller.base;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prolog.framework.common.message.RestMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(tags = "工具类，检查连接是否通畅")
@RequestMapping("/api/v1/base/checkConnect")
public class PrologCheckConnectController {

	@ApiOperation(value = "查询连接状态", notes = "查询连接状态")
	@ResponseBody
	@RequestMapping(value = "/check", method = RequestMethod.POST, produces = "text/plain")
	public RestMessage<Object> queryConnectState() throws Exception {
		return RestMessage.newInstance(true, "查询成功", null);
	}

}
