package com.prolog.eis.controller.manual;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "Wms 出入库资料资料")
@RequestMapping("/api/v1/sxk/task")
public class PrologTaskController {

	/*@Autowired
	private PrologTaskService prologTaskService;
	@Autowired
	private MCSLineService mCSLineService;

	@ApiOperation(value = "创建入库任务", notes = "创建入库任务")
	@PostMapping("/createInboundTask")
	public RestMessage<WmsInboundTask> createInboundTask(@RequestBody WmsInboundTask inboundTask) throws Exception {

		WmsInboundTask in = prologTaskService.createInboundTask(inboundTask);

		return RestMessage.newInstance(true, "保存成功", in);
	}

	@ApiOperation(value = "创建出库任务", notes = "创建出库任务")
	@PostMapping("/createOutboundTask")
	public RestMessage<WmsOutboundTask> createOutboundTask(@RequestBody WmsOutboundTask outboundTask) throws Exception {

		WmsOutboundTask out = prologTaskService.createOutboundTask(outboundTask);

		return RestMessage.newInstance(true, "保存成功", out);
	}

	@ApiOperation(value = "任务确认", notes = "任务确认")
	@PostMapping("/confirmTask")
	public RestMessage<String> confirmTask(@RequestBody String json) throws Exception {

		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);

		String commandNo = helper.getString("commandNo");
		int type = helper.getInt("type");

		prologTaskService.confirmTask(commandNo, type);

		return RestMessage.newInstance(true, "确认成功", "");
	}

	@ApiOperation(value = "任务取消", notes = "任务取消")
	@PostMapping("/cancelTask")
	public RestMessage<String> cancelTask(@RequestBody String json) throws Exception {

		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);

		String commandNo = helper.getString("commandNo");
		int type = helper.getInt("type");

		prologTaskService.cancelTask(commandNo, type);

		return RestMessage.newInstance(true, "确认成功", null);
	}

	@ApiOperation(value = "任务完成", notes = "出库，实物已经出来但是数据没完成，手动强制完成")
	@PostMapping("/finishLxkOutboundTask")
	public RestMessage<String> finishLxkOutboundTask(@RequestBody String json) throws Exception {

		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);

		String boxNo = helper.getString("boxNo");

		prologTaskService.finishLxkOutboundTask(boxNo);

		return RestMessage.newInstance(true, "成功", null);
	}
	
	@ApiOperation(value = "修改任务优先级", notes = "修改任务优先级")
	@PostMapping("/changetaskpriority")
	public RestMessage<String> changeTaskPriority(@RequestBody String json) throws Exception {

		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);

		int id = helper.getInt("id");
		int priority = helper.getInt("priority");

		prologTaskService.changeTaskPriority(id,priority);

		return RestMessage.newInstance(true, "成功", null);
	}
	
	@ApiOperation(value = "创建空拖出库任务", notes = "创建空拖出库任务")
	@PostMapping("/createemptyboxtask")
	public RestMessage<String> createEmptyBoxTask(@RequestBody String json) throws Exception {

		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);

		String entryCode = helper.getString("entryCode");
		int layer = helper.getInt("layer");

		mCSLineService.createEmptyBoxTask(entryCode,layer);

		return RestMessage.newInstance(true, "成功", null);
	}*/
}
