package com.prolog.eis.controller.baseinfo;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "Wms port口资料")
@RequestMapping("/api/v1/sxk/stationport")
public class PrologStationPortController {

	/*@Autowired
	private StationPortService stationPortService;
	
	@ApiOperation(value = "获取叫料解包区信息", notes = "获取叫料解包区信息")
	@PostMapping("/getstationportdtos")
	public RestMessage<List<StationPortDto>> getStationPortDtos(@RequestBody String json) throws Exception {
		
		List<StationPortDto> list = stationPortService.getStationPortDtos();

		return RestMessage.newInstance(true, "保存成功", list);
	}
	
	@ApiOperation(value = "获取该区域其他出库口", notes = "获取该区域其他出库口")
	@PostMapping("/getotherportinfo")
	public RestMessage<List<PortInfo>> getOtherPortInfo(@RequestBody String json) throws Exception {
		
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);

		int stationId = helper.getInt("stationId");
		
		List<PortInfo> list = stationPortService.getOtherPortInfo(stationId);

		return RestMessage.newInstance(true, "保存成功", list);
	}
	
	@ApiOperation(value = "添加获取叫料解包区出入库口信息", notes = "添加获取叫料解包区出入库口信息")
	@PostMapping("/addstationsinfoport")
	public RestMessage<String> addStationsInfoPort(@RequestBody String json) throws Exception {
		
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);

		int stationId = helper.getInt("stationId");
		int portId = helper.getInt("portId");
		
		stationPortService.addStationPortDtos(stationId,portId);

		return RestMessage.newInstance(true, "保存成功", null);
	}
	
	@ApiOperation(value = "删除获取叫料解包区出入库口信息", notes = "删除获取叫料解包区出入库口信息")
	@PostMapping("/deletestationportdtos")
	public RestMessage<String> deleteStationPortDtos(@RequestBody String json) throws Exception {
		
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);

		int stationId = helper.getInt("stationId");
		int portId = helper.getInt("portId");
		
		stationPortService.deleteStationPortDtos(stationId,portId);

		return RestMessage.newInstance(true, "保存成功", null);
	}*/
}
