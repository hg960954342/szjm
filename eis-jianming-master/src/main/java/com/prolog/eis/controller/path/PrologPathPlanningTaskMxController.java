package com.prolog.eis.controller.path;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@Api(tags = "路径规划任务汇总")
@RequestMapping("/api/v1/sxk/pathplanningtaskmx")
public class PrologPathPlanningTaskMxController {

	/*@Autowired
	private SxPathPlanningTaskMxMapper sxPathPlanningTaskMxMapper;
	@Autowired
	private QcSxPathPlanningTaskHzMapper qcSxPathPlanningTaskHzMapper;
	
	@ApiOperation(value = "路径规划任务明细查询", notes = "路径规划任务明细查询")
    @PostMapping("/findTaskMxByHzId")
    public RestMessage<List<SxPathPlanningTaskMx>> findConfigMxByHzId(@RequestBody String json) throws Exception {
    	PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
    	int taskHzId = helper.getInt("taskHzId");
    	List<SxPathPlanningTaskMx> sxPathPlanningConfigMxs = sxPathPlanningTaskMxMapper.findByMap(MapUtils.put("taskHzId", taskHzId).getMap(), SxPathPlanningTaskMx.class);
        return RestMessage.newInstance(true, "查询成功", sxPathPlanningConfigMxs);
    }
	
	@ApiOperation(value = "查询所有路径Hz", notes = "查询所有路径Hz")
	@PostMapping("/findAllTaskHz")
    public RestMessage<List<SxPathPlanningTaskHz>> findHzList() {
		List<SxPathPlanningTaskMx> sxPathPlanningConfigMxs = sxPathPlanningTaskMxMapper.findByMap(null,SxPathPlanningTaskMx.class);
		List<QcSxPathPlanningTaskHz> sxPathPlanningTaskHzList = qcSxPathPlanningTaskHzMapper.findAll();
		Map<Integer, List<SxPathPlanningTaskMx>> mxMap = sxPathPlanningConfigMxs.stream().collect(Collectors.groupingBy(SxPathPlanningTaskMx::getTaskHzId));
		sxPathPlanningTaskHzList.stream().forEach(p->{
			if (mxMap.containsKey(p.getId())){
				p.setSxPathPlanningTaskMx(mxMap.get(p.getId()));
			}
		});
		//根据時間排序
		List<SxPathPlanningTaskHz> hzs = sxPathPlanningTaskHzList.stream().sorted(Comparator.comparing(SxPathPlanningTaskHz::getCreateTime)).collect(Collectors.toList());
		return RestMessage.newInstance(true, "查询成功", hzs);
    }*/
}
