package com.prolog.eis.controller.path;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "路径规划方案配置")
@RequestMapping("/api/v1/sxk/path/project")
public class PrologPathProjectController {

	/*@Autowired
	private SxPathConfigProjectService sxPathConfigProjectService;
	
    @ApiOperation(value = "路径规划方案查询", notes = "路径规划方案查询")
    @PostMapping("/findAllProject")
    public RestMessage<List<SxPathConfigProject>> findAllProject() throws Exception {
    	List<SxPathConfigProject> sxPathPlanningConfigDtos = sxPathConfigProjectService.findAllProject();
        return RestMessage.newInstance(true, "查询成功", sxPathPlanningConfigDtos);
    }

    @ApiOperation(value = "路径规划方案添加", notes = "路径规划方案添加")
    @PostMapping("/addProject")
    public RestMessage<String> addProject(@RequestBody String json) throws Exception {
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        SxPathConfigProject sxPathConfigProject = helper.getObject("pathProject",SxPathConfigProject.class);
        sxPathConfigProjectService.addProject(sxPathConfigProject);
        return RestMessage.newInstance(true, "添加成功", null);
    }

    @ApiOperation(value = "根据方案查询路径规划汇总", notes = "根据方案查询路径规划汇总")
    @PostMapping("/findHzByProjectId")
    public RestMessage<List<SxPathPlanningConfigDto>> findHzByProjectId(@RequestBody String json) throws Exception {
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        int projectId = helper.getInt("projectId");
        List<SxPathPlanningConfigDto> result = sxPathConfigProjectService.findHzByProjectId(projectId);
        return RestMessage.newInstance(true, "删除成功", result);
    }

    @ApiOperation(value = "方案绑定汇总", notes = "方案绑定汇总")
    @PostMapping("/projectBindingHzs")
    public RestMessage<String> projectBindingHzs(@RequestBody String json) throws Exception {
    	PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        int projectId = helper.getInt("projectId");
        List<Integer> configHzIds = helper.getIntList("configHzIds");
        if(CollectionUtils.isEmpty(configHzIds)){
            return RestMessage.newInstance(false, "绑定失败,绑定路径汇总id为空", null);
        }
        sxPathConfigProjectService.projectBindingHzs(projectId,configHzIds);
        return RestMessage.newInstance(true, "绑定成功", null);
    }

    @ApiOperation(value = "方案切换", notes = "方案切换")
    @PostMapping("/projectSwitch")
    public RestMessage<String> projectSwitch(@RequestBody String json) throws Exception {
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        int projectId = helper.getInt("projectId");
        return sxPathConfigProjectService.projectSwitch(projectId);
    }

    @ApiOperation(value = "根据方案ID查询", notes = "根据方案ID查询")
    @PostMapping("/findProjectById")
    public RestMessage<Map<String,Object>> findProjectById(@RequestBody String json) throws Exception {
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        int projectId = helper.getInt("projectId");
        return sxPathConfigProjectService.findProjectById(projectId);
    }

    @ApiOperation(value = "路径规划方案删除", notes = "路径规划方案删除")
    @PostMapping("/deleteProjectById")
    public RestMessage<String> deleteProjectById(@RequestBody String json) throws Exception {
    	PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        int projectId = helper.getInt("projectId");
        return sxPathConfigProjectService.deleteProjectById(projectId);
    }*/
}
