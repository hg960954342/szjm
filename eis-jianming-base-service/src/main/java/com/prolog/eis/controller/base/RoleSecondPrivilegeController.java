package com.prolog.eis.controller.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prolog.eis.model.base.RoleFirstPrivilege;
import com.prolog.eis.service.base.RoleSecondPrivilegeService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="权限服务")
@RequestMapping("/api/v1/base/roleSecondPrivilege")
public class RoleSecondPrivilegeController {
	
	@Autowired
	private RoleSecondPrivilegeService roleSecondPrivilegeService;
	
	@ApiOperation(value="保存二级角色权限",notes="保存二级角色权限")
	@PostMapping("/save")
	public RestMessage<RoleFirstPrivilege> saveSecondPrivilege(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int roleId = helper.getInt("roleId");
		int firstPrivilegeId = helper.getInt("firstPrivilegeId");
		int secondPrivilegeId = helper.getInt("secondPrivilegeId");
		roleSecondPrivilegeService.saveRoleSecondPrivile(roleId, secondPrivilegeId, firstPrivilegeId);
		return RestMessage.newInstance(true, "保存成功", null);
	}
	
	@ApiOperation(value="删除二级角色权限",notes="删除二级角色权限")
	@PostMapping("/delete")
	public RestMessage<RoleFirstPrivilege> deleteSecondPrivilege(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int roleId = helper.getInt("roleId");
		int secondPrivilegeId = helper.getInt("secondPrivilegeId");
		roleSecondPrivilegeService.deleteRoleSecondPrivilege(roleId, secondPrivilegeId);
		return RestMessage.newInstance(true, "删除成功", null);
	}

}
