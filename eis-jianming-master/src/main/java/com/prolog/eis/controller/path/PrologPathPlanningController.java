package com.prolog.eis.controller.path;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prolog.eis.model.path.SxPathPlanningConfigHz;
import com.prolog.eis.model.path.SxPathPlanningConfigMx;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "路径规划配置")
@RequestMapping("/api/v1/sxk/pathplanning")
public class PrologPathPlanningController {

	/*@Autowired
	private SxPathPlanningConifgHzService sxPathPlanningConifgHzService;
	
    @ApiOperation(value = "路径规划查询", notes = "路径规划查询")
    @PostMapping("/findAllConfig")
    public RestMessage<List<SxPathPlanningConfigDto>> findAllConfig() throws Exception {
		
    	List<SxPathPlanningConfigDto> sxPathPlanningConfigDtos = sxPathPlanningConifgHzService.findAllConfig();
        return RestMessage.newInstance(true, "查询成功", sxPathPlanningConfigDtos);
    }
    
    @ApiOperation(value = "路径规划明细查询", notes = "路径规划明细查询")
    @PostMapping("/findConfigMxByHzId")
    public RestMessage<List<SxPathPlanningConfigMx>> findConfigMxByHzId(@RequestBody String json) throws Exception {
    	PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
    	int configHzId = helper.getInt("configHzId");
    	List<SxPathPlanningConfigMx> sxPathPlanningConfigMxs = sxPathPlanningConifgHzService.findConfigMxByHzId(configHzId);
        return RestMessage.newInstance(true, "查询成功", sxPathPlanningConfigMxs);
    }
    
    @ApiOperation(value = "路径规划汇总添加", notes = "路径规划汇总添加")
    @PostMapping("/addConfigHz")
    public RestMessage<List<SxPathPlanningConfigDto>> addConfigHz(@RequestBody String json) throws Exception {
    	PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
    	SxPathPlanningConfigHz sxPathPlanningConfigHz = helper.getObject("sxPathPlanningConfigHz",SxPathPlanningConfigHz.class);
    	sxPathPlanningConifgHzService.addConifgHz(sxPathPlanningConfigHz);
        return RestMessage.newInstance(true, "添加成功", null);
    }
    
    @ApiOperation(value = "路径规划汇总删除", notes = "路径规划汇总删除")
    @PostMapping("/deleteConfigHz")
    public RestMessage<List<SxPathPlanningConfigDto>> deleteConfigHz(@RequestBody String json) throws Exception {
    	PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
    	int configHzId = helper.getInt("configHzId");
    	sxPathPlanningConifgHzService.deleteConfigHz(configHzId);
        return RestMessage.newInstance(true, "删除成功", null);
    }
    
    @ApiOperation(value = "路径规划明细添加", notes = "路径规划明细添加")
    @PostMapping("/addConfigMx")
    public RestMessage<SxPathPlanningConfigMx> addConfigMx(@RequestBody String json) throws Exception {
    	PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
    	SxPathPlanningConfigMx sxPathPlanningConfigMx = helper.getObject("node",SxPathPlanningConfigMx.class);
//    	List<SxPathPlanningConfigMx> sxPathPlanningConfigMxs = helper.getObjectList("", SxPathPlanningConfigMx.class);
    	SxPathPlanningConfigMx addConfigMx = sxPathPlanningConifgHzService.addConfigMx(sxPathPlanningConfigMx);
        return RestMessage.newInstance(true, "添加成功", addConfigMx);
    }
    
    @ApiOperation(value = "路径规划汇总修改", notes = "路径规划汇总修改")
    @PostMapping("/updateConfigHz")
    public RestMessage<SxPathPlanningConfigHz> updateConfigHz(@RequestBody String json) throws Exception {
    	PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
    	SxPathPlanningConfigHz configHz = helper.getObject("sxPathPlanningConfigHz",SxPathPlanningConfigHz.class);
    	sxPathPlanningConifgHzService.updateConfigHz(configHz);
        return RestMessage.newInstance(true, "添加成功", null);
    }
    
    @ApiOperation(value = "路径规划明细修改", notes = "路径规划明细修改")
    @PostMapping("/updateConfigMx")
    public RestMessage<SxPathPlanningConfigMx> updateConfigMx(@RequestBody String json) throws Exception {
    	PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
    	SxPathPlanningConfigMx configMx = helper.getObject("node",SxPathPlanningConfigMx.class);
    	SxPathPlanningConfigMx addConfigMx =  sxPathPlanningConifgHzService.updateConfigMx(configMx);
        return RestMessage.newInstance(true, "添加成功", addConfigMx);
    }
    
    @ApiOperation(value = "路径规划明细排序修改", notes = "路径规划明细排序修改")
    @PostMapping("/updateConfigMxSort")
    public RestMessage<SxPathPlanningConfigMx> updateConfigMxSort(@RequestBody String json) throws Exception {
    	PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
    	List<Integer> ids = helper.getIntList("ids");
    	sxPathPlanningConifgHzService.updateConfigMxSort(ids);
        return RestMessage.newInstance(true, "添加成功", null);
    }
  
    
    @ApiOperation(value = "路径规划明细删除", notes = "路径规划明细删除")
    @PostMapping("/deleteConfigMxById")
    public RestMessage<List<SxPathPlanningConfigMx>> deleteConfigMxById(@RequestBody String json) throws Exception {
    	PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
    	int id = helper.getInt("id");
    	sxPathPlanningConifgHzService.deleteMxByMxId(id);
        return RestMessage.newInstance(true, "删除成功", null);
    }*/
}
