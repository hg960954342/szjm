package com.prolog.eis.controller.base;

import com.prolog.eis.dto.base.BasePagerDto;
import com.prolog.eis.dto.base.DataEntity;
import com.prolog.eis.dto.base.DataEntityCollectionDto;
import com.prolog.eis.model.base.RoleFirstPrivilege;
import com.prolog.eis.service.base.BasePagerService;
import com.prolog.eis.service.base.RoleFirstPrivilegeService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "权限服务")
@RequestMapping("/api/v1/base/roleFirstPrivilege")
public class RoleFirstPrivilegeController {

	@Autowired
	private RoleFirstPrivilegeService roleFirstPrivilegeService;
	@Autowired
	private BasePagerService basePagerService;


	@ApiOperation(value = "保存一级角色权限", notes = "保存一级角色权限")
	@PostMapping("/save")
	public RestMessage<RoleFirstPrivilege> saveRoleFirstPrivilege(@RequestBody String json) throws Exception {
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int roleId = helper.getInt("roleId");
		int firstPrivilegeId = helper.getInt("firstPrivilegeId");
		roleFirstPrivilegeService.saveRoleFirstPrivilege(roleId, firstPrivilegeId);
		return RestMessage.newInstance(true, "保存成功", null);
	}

	@ApiOperation(value = "删除一级权限", notes = "删除一级权限")
	@PostMapping("/delete")
	public RestMessage<RoleFirstPrivilege> deleteRoleFirstPrivilege(@RequestBody String json) throws Exception {
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int roleId = helper.getInt("roleId");
		int firstPrivilegeId = helper.getInt("firstPrivilegeId");
		roleFirstPrivilegeService.deleteRoleFirstPrivilege(roleId, firstPrivilegeId);
		return RestMessage.newInstance(true, "删除成功", null);
		 
	}
	
	@ApiOperation(value = "加载分页", notes = "加载分页")
	@PostMapping("/loadPager")
	public RestMessage<DataEntityCollectionDto> loadPager(@RequestBody String json) throws Exception {
		String columns = "loginName,userName,deptFullPath,sex,roleName";
		String tableName = "SysUserView";
 		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		Map<String, Object> reStr = helper.getPagerDto(columns,tableName,"condition", BasePagerDto.class);
		//获得拼装后的JSON对象
 		int pageSize = (int) reStr.get("pageSize");
 		int startPage = (int) reStr.get("startPage");
 		String condition = reStr.get("conditions").toString();
 		String orders =  reStr.get("orders").toString();
    		List<DataEntity> rows = basePagerService.getPagers(columns, tableName, condition, orders, startPage,pageSize);
  		DataEntityCollectionDto collectionDto = new DataEntityCollectionDto();
  		collectionDto.setRows(rows);
  		return RestMessage.newInstance(true, "查询成功", collectionDto);
	}
	
	@ApiOperation(value = "第一次加载分页", notes = "第一次加载分页")
	@PostMapping("/firstLoadPager")
	public RestMessage<DataEntityCollectionDto> firstLoadPager(@RequestBody String json) throws Exception {
		String columns = "loginName,userName,deptFullPath,sex,roleName";
		String tableName = "SysUserView";
 		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		Map<String, Object> reStr = helper.getPagerDto(columns,tableName,"condition", BasePagerDto.class);
		//获得拼装后的JSON对象
 		int pageSize = (int) reStr.get("pageSize");
 		int startPage = (int) reStr.get("startPage");
 		String condition = reStr.get("conditions").toString();
 		String orders =  reStr.get("orders").toString();
 		//第一次加载需要获得总数量
 		int totalCount = basePagerService.getTotalCount(tableName,condition);
  		List<DataEntity> rows = basePagerService.getPagers(columns, tableName, condition, orders, startPage,pageSize);
  		DataEntityCollectionDto collectionDto = new DataEntityCollectionDto();
  		collectionDto.setRows(rows);
  		collectionDto.setTotalCount(totalCount);
 		return RestMessage.newInstance(true, "查询成功", collectionDto);
 	}
	
 	

}
