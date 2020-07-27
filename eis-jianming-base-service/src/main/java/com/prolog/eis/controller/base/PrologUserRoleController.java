package com.prolog.eis.controller.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prolog.eis.dto.base.AllPrivilegeRespDto;
import com.prolog.eis.model.base.BaseTreeModel;
import com.prolog.eis.model.base.UserRole;
import com.prolog.eis.service.base.UserRoleService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="权限服务")
@RequestMapping("/api/v1/base/role")
public class PrologUserRoleController {

	@Autowired
	private UserRoleService userRoleService;
	
	@ApiOperation(value="角色权限查询",notes="查询该角色下的所有权限")
	@PostMapping("/all")
	public RestMessage<List<AllPrivilegeRespDto>> queryAllRole(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int roleId = helper.getInt("roleId");
		List<AllPrivilegeRespDto> allPrivileges = userRoleService.findUserRole(roleId);
		return RestMessage.newInstance(true, "查询成功", allPrivileges);
	}
	
	
	@ApiOperation(value="新增用户角色",notes="新增用户角色")
	@PostMapping("/save")
	public RestMessage<UserRole> saveUserRole(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		String roleName = helper.getString("roleName");
		userRoleService.saveUserRole(roleName);
		return RestMessage.newInstance(true, "保存成功", null);
	}
	
	@ApiOperation(value="修改用户角色",notes="修改用户角色")
	@PostMapping("/update")
	public RestMessage<UserRole> updateUserRole(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int id = helper.getInt("id");
		String roleName = helper.getString("roleName");
		userRoleService.updateUserRole(id,roleName);
		return RestMessage.newInstance(true, "修改成功", null);
	}
	
	@ApiOperation(value="删除用户角色",notes="删除用户角色")
	@PostMapping("/delete")
	public RestMessage<UserRole> deleteUserRole(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int id = helper.getInt("id");
 		userRoleService.deleteUserRole(id);
		return RestMessage.newInstance(true, "删除成功", null);
	}
	
	@ApiOperation(value="查询所有角色",notes="查询所有角色")
	@PostMapping("/query")
	public RestMessage<List<UserRole>> queryUserRole() throws Exception{
		List<UserRole> userRoles = userRoleService.findAllUserRole();
		return RestMessage.newInstance(true, "查询成功", userRoles);
	}
	
	@ApiOperation(value="修改角色排序",notes="修改角色排序")
	@PostMapping("/sortIndex")
	public RestMessage<BaseTreeModel> updateSoreIndex(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
 		@SuppressWarnings("unchecked")
		List<Integer> ids = helper.getObject("ids", List.class);
		userRoleService.updateSortIndex(ids);
		return RestMessage.newInstance(true, "查询成功", null);
	}
}
