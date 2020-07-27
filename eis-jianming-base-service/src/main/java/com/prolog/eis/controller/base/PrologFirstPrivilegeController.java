package com.prolog.eis.controller.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prolog.eis.model.base.BaseTreeModel;
import com.prolog.eis.model.base.FirstPrivilege;
import com.prolog.eis.service.base.FirstPrivilegeService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="权限服务")
@RequestMapping("/api/v1/base/firstPrivilege")
public class PrologFirstPrivilegeController {

	@Autowired
	private FirstPrivilegeService firstPrivilegeService;
	
	@ApiOperation(value="一级权限查询",notes="一级权限查询")
	@PostMapping("/query")
	public RestMessage<List<FirstPrivilege>> queryFirstPrivilege() throws Exception{
		List<FirstPrivilege> firstPrivileges = firstPrivilegeService.findFirstPrivilege();
		return RestMessage.newInstance(true, "查询成功", firstPrivileges);
	}
	
	@ApiOperation(value="一级权限保存",notes="一级权限保存")
	@PostMapping("/save")
	public RestMessage<FirstPrivilege> saveFirstPrivilege(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		FirstPrivilege firstPrivilege = helper.getObject("firstPrivilege", FirstPrivilege.class);
		firstPrivilegeService.saveFirstPrivilege(firstPrivilege);
		return RestMessage.newInstance(true, "保存成功", null);
	}
	
	@ApiOperation(value="一级权限修改",notes="一级权限修改")
	@PostMapping("/update")
	public RestMessage<FirstPrivilege> updateFirstPrivilege(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		FirstPrivilege firstPrivilege = helper.getObject("firstPrivilege", FirstPrivilege.class);
		firstPrivilegeService.updateFirstPrivilege(firstPrivilege);
		return RestMessage.newInstance(true, "修改成功", null);
	}
	
	@ApiOperation(value="一级权限删除",notes="一级权限删除")
	@PostMapping("/delete")
	public RestMessage<FirstPrivilege> deleteFirstPrivilege(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int id = helper.getInt("id");
		firstPrivilegeService.deleteFirstPrivilege(id);
		return RestMessage.newInstance(true, "删除成功", null);
	}
	
	@ApiOperation(value="修改一级权限排序",notes="修改一级权限排序")
	@PostMapping("/sortIndex")
	public RestMessage<BaseTreeModel> updateSoreIndex(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
 		@SuppressWarnings("unchecked")
		List<Integer> ids = helper.getObject("ids", List.class);
		firstPrivilegeService.updateSortIndex(ids);
		return RestMessage.newInstance(true, "查询成功", null);
	}
}
