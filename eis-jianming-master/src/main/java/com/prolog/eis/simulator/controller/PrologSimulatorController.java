package com.prolog.eis.simulator.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * MCS接口
 * 
 * @author clarence_she
 *
 */
@RestController
@Api(tags = "MCS接口")
@RequestMapping("/api/v1/sxk/simulator")
public class PrologSimulatorController {

	/*@Autowired
	private TestService testService;
	@Autowired
	private SxStoreTaskFinishService sxStoreTaskFinishService;
	
	@ApiOperation(value = "理货台货位统计", notes = "理货台货位统计")
    @PostMapping("/getsimulatorDataInitData")
    public RestMessage<SimulatorDataDto> getSimulatorDataInitData() throws Exception {

		SimulatorDataDto simulatorDataDto = testService.getSimulatorDataInitData();
		
		return RestMessage.newInstance(true, "查询成功", simulatorDataDto);
    }
	
	@ApiOperation(value = "修复货位数据", notes = "修复货位数据")
    @PostMapping("/xiu")
    public RestMessage<String> xiu() throws Exception {

		sxStoreTaskFinishService.computeIsInBoundLocationTest();
		
		return RestMessage.newInstance(true, "查询成功", null);
    }
	
	@ApiOperation(value = "修复货位数据", notes = "修复货位数据")
    @PostMapping("/setwmshuoweino")
    public RestMessage<String> setWmsHuoWeiNo() throws Exception {

		testService.setWmsHuoWeiNo();
		
		return RestMessage.newInstance(true, "查询成功", null);
    }
	
	@ApiOperation(value = "导入货位", notes = "导入货位")
	@PostMapping ("/importLocation")
	public RestMessage<Object> importLocation(@RequestBody String json) throws Exception {
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		List<StoreLocationGroupDto> storeLocationGroupDtos = helper.getObjectList("storeLocationGroups",StoreLocationGroupDto.class);
		if(CollectionUtils.isEmpty(storeLocationGroupDtos)){
			return RestMessage.newInstance(false, "空数据", null);
		}
		//500个保存
		List<List<StoreLocationGroupDto>> partition = Lists.partition(storeLocationGroupDtos, 500);
		for (List<StoreLocationGroupDto> locationGroupDtos : partition) {
			testService.importStoreLocation(locationGroupDtos);
		}
		return RestMessage.newInstance(true, "导入成功", null);
	}*/
}
