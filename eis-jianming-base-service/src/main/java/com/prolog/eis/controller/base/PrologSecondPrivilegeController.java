package com.prolog.eis.controller.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prolog.eis.dto.base.AllPrivilegeRespDto;
import com.prolog.eis.model.base.BaseTreeModel;
import com.prolog.eis.model.base.FirstPrivilege;
import com.prolog.eis.model.base.SecondPrivilege;
import com.prolog.eis.service.base.SecondPrivilegeService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="权限服务")
@RequestMapping("/api/v1/base/secondPrivilege")
public class PrologSecondPrivilegeController {

	@Autowired
	private SecondPrivilegeService secondPrivilegeService;
	
	@ApiOperation(value="二级权限查询",notes="二级权限查询")
	@PostMapping("/query")
	public RestMessage<List<SecondPrivilege>> querySecondPrivilege(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int firstPrivilegeId = helper.getInt("firstPrivilegeId");
		List<SecondPrivilege> secondPrivileges = secondPrivilegeService.findSecondPrivilege(firstPrivilegeId);
		return RestMessage.newInstance(true, "查询成功", secondPrivileges);
	}
	
	@ApiOperation(value="二级权限保存",notes="二级权限保存")
	@PostMapping("/save")
	public RestMessage<SecondPrivilege> saveFirstPrivilege(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		SecondPrivilege secondPrivilege = helper.getObject("secondPrivilege",SecondPrivilege.class);
		secondPrivilegeService.saveSecondPrivilege(secondPrivilege);
		return RestMessage.newInstance(true, "保存成功", null);
	}
	
	@ApiOperation(value="二级权限修改",notes="二级权限修改")
	@PostMapping("/update")
	public RestMessage<FirstPrivilege> updateFirstPrivilege(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		SecondPrivilege secondPrivilege = helper.getObject("secondPrivilege",SecondPrivilege.class);		
		secondPrivilegeService.updateSecondPrivilege(secondPrivilege);
		return RestMessage.newInstance(true, "修改成功", null);
	}
	
	@ApiOperation(value="二级权限删除",notes="二级权限删除")
	@PostMapping("/delete")
	public RestMessage<FirstPrivilege> deleteSecondPrivilege(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int id = helper.getInt("id");		
		secondPrivilegeService.deleteSecondPrivilege(id);
		return RestMessage.newInstance(true, "删除成功", null);
	}
	
	@ApiOperation(value="所有权限",notes="所有权限")
	@PostMapping("/allPrivilege")
	public RestMessage<List<AllPrivilegeRespDto>> allPrivilege() throws Exception{
		List<AllPrivilegeRespDto> allPrivilegeRespDto = secondPrivilegeService.allAllPrivilegeResp();
		return RestMessage.newInstance(true, "查询成功", allPrivilegeRespDto);
	}
	
	@ApiOperation(value="修改二级权限排序",notes="修改二级权限排序")
	@PostMapping("/sortIndex")
	public RestMessage<BaseTreeModel> updateSoreIndex(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
 		@SuppressWarnings("unchecked")
		List<Integer> ids = helper.getObject("ids", List.class);
		secondPrivilegeService.updateSortIndex(ids);
		return RestMessage.newInstance(true, "查询成功", null);
	}
}
