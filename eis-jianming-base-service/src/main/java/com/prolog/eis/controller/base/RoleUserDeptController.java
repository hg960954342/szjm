package com.prolog.eis.controller.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prolog.eis.model.base.BaseTreeModel;
import com.prolog.eis.service.base.UserDeptService;
import com.prolog.eis.service.delet.DeleteService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="用户服务")
@RequestMapping("/api/v1/base/userDept")
public class RoleUserDeptController {
	
	@Autowired
	private UserDeptService userDeptService;
	@Autowired
	private DeleteService deleteService;
	
	@ApiOperation(value="查询部门树",notes="查询部门树")
	@PostMapping("/queryTree")
	public RestMessage<List<BaseTreeModel>> queryDeptTree(@RequestBody String json) throws Exception{
		List<BaseTreeModel> trees = userDeptService.queryDeptTreeNode();
		return RestMessage.newInstance(true, "查询成功", trees);
	}
	
	@ApiOperation(value="添加部门树",notes="添加部门树")
	@PostMapping("/saveTree")
	public RestMessage<BaseTreeModel> saveDeptTreeNode(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int parentId = helper.getInt("parentId");
		String objectName = helper.getString("objectName");
		BaseTreeModel trees = userDeptService.saveDeptTreeNode(parentId, objectName);
		return RestMessage.newInstance(true, "添加成功", trees);
	}
	
	@ApiOperation(value="修改部门树",notes="修改部门树")
	@PostMapping("/updateTree")
	public RestMessage<BaseTreeModel> updateDeptTreeNode(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int id = helper.getInt("id");
		String objectName = helper.getString("objectName");
		userDeptService.updateNode(id, objectName);
		return RestMessage.newInstance(true, "查询成功", null);
	}
	
	@ApiOperation(value="移动部门树",notes="移动部门树")
	@PostMapping("/moveTree")
	public RestMessage<BaseTreeModel> moveDeptTreeNode(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int targetId = helper.getInt("targetId");
		int parentId = helper.getInt("parentId");
		userDeptService.moveDeptNode(targetId, parentId);
		return RestMessage.newInstance(true, "查询成功", null);
	}
	
	@ApiOperation(value="删除部门树",notes="删除部门树")
	@PostMapping("/delTree")
	public RestMessage<BaseTreeModel> deleteDeptTreeNode(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int id = helper.getInt("id");
		deleteService.DeleteUserDept(id);
		return RestMessage.newInstance(true, "查询成功", null);
	}

	@ApiOperation(value="修改部门排序",notes="修改部门排序")
	@PostMapping("/sortIndex")
	public RestMessage<BaseTreeModel> updateSoreIndex(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
 		@SuppressWarnings("unchecked")
		List<Integer> ids = helper.getObject("ids", List.class);
		userDeptService.updateSoreIndex(ids);
		return RestMessage.newInstance(true, "查询成功", null);
	}

}
