package com.prolog.eis.controller.baseinfo;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "Wms 叫料解包区资料")
@RequestMapping("/api/v1/sxk/stationsinfo")
public class PrologStationsInfoController {

	/*@Autowired
	private StationsInfoService stationsInfoService;
	
	@ApiOperation(value = "获取叫料解包区信息", notes = "获取叫料解包区信息")
	@PostMapping("/getstationsinfo")
	public RestMessage<List<StationsInfo>> getStationsInfo(@RequestBody String json) throws Exception {
		
		List<StationsInfo> list = stationsInfoService.getStationsInfos();

		return RestMessage.newInstance(true, "保存成功", list);
	}
	
	@ApiOperation(value = "添加叫料解包区", notes = "添加叫料解包区")
	@PostMapping("/addstationsinfo")
	public RestMessage<String> addStationsInfo(@RequestBody String json) throws Exception {
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);

		String wmsStationNo = helper.getString("wmsStationNo");
		String remark = helper.getString("remark");
		
		stationsInfoService.addStationsInfo(wmsStationNo,remark);
		
		return RestMessage.newInstance(true, "保存成功", null);
	}
	
	@ApiOperation(value = "编辑叫料解包区", notes = "添加叫料解包区")
	@PostMapping("/editstationsinfo")
	public RestMessage<String> editStationsInfo(@RequestBody String json) throws Exception {
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);

		int id = helper.getInt("id");
		String wmsStationNo = helper.getString("wmsStationNo");
		String remark = helper.getString("remark");

		stationsInfoService.editStationsInfo(id, wmsStationNo,remark);
		
		return RestMessage.newInstance(true, "保存成功", null);
	}
	
	@ApiOperation(value = "删除叫料解包区", notes = "删除叫料解包区")
	@PostMapping("/deletestationsinfo")
	public RestMessage<String> deleteStationsinfo(@RequestBody String json) throws Exception {
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);

		int id = helper.getInt("id");
		stationsInfoService.deleteStationsInfo(id);
		
		return RestMessage.newInstance(true, "保存成功", null);
	}
	
	@ApiOperation(value = "获取料箱库叫料解包区", notes = "获取料箱库叫料解包区")
	@PostMapping("/getlxkstations")
	public RestMessage<List<StationsInfo>> getLxkStations(@RequestBody String json) throws Exception {
		
		List<StationsInfo> lxkList = stationsInfoService.getLxkStations();
		
		return RestMessage.newInstance(true, "保存成功", lxkList);
	}
	
	@ApiOperation(value = "获取四向库叫料解包区", notes = "获取四向库叫料解包区")
	@PostMapping("/getsxkstations")
	public RestMessage<List<StationsInfo>> getSxkStations(@RequestBody String json) throws Exception {
		
		List<StationsInfo> sxkList = stationsInfoService.getSxkStations();
		
		return RestMessage.newInstance(true, "保存成功", sxkList);
	}*/
}
