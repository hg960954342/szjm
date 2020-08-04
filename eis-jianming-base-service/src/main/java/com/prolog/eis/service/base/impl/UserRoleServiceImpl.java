package com.prolog.eis.service.base.impl;

import com.prolog.eis.dao.base.*;
import com.prolog.eis.dto.base.AllPrivilegeRespDto;
import com.prolog.eis.dto.base.SecondPrivilegeDto;
import com.prolog.eis.model.base.*;
import com.prolog.eis.service.base.UserRoleService;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Order;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService{
	@Autowired
	private RoleFirstPrivilegeMapper roleFirstPrivilegeMapper;
	@Autowired
	private RoleSecondPrivilegeMapper roleSecondPrivilegeMapper;
	@Autowired
	private UserRoleMapper userRoleMapper;
	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	private BaseSortIndexMapper baseSortIndexMapper;
	
	
	@Override
	public List<AllPrivilegeRespDto> findUserRole(int roleId) throws Exception {
		if(0 == roleId) {
			throw new Exception("角色Id不能为空！");
		}
		List<FirstPrivilege> firstPrivileges = roleFirstPrivilegeMapper.findRoleFirstPrivilege(roleId);
		List<SecondPrivilege> secondPrivileges = roleSecondPrivilegeMapper.findRoleSecondPrivilege(roleId);
		
		List<AllPrivilegeRespDto> list = new ArrayList<AllPrivilegeRespDto>();
		for(FirstPrivilege firstPrivilege :firstPrivileges) {
			AllPrivilegeRespDto allPrivilege = new AllPrivilegeRespDto();
			allPrivilege.setId(firstPrivilege.getId());
			allPrivilege.setPrivilegeName(firstPrivilege.getPrivilegeName());
			allPrivilege.setPrivilegeKey(firstPrivilege.getPrivilegeKey());
			allPrivilege.setSortIndex(firstPrivilege.getSortIndex());
			List<SecondPrivilegeDto> listSecond = new ArrayList<>();
			for(SecondPrivilege secondPrivilege : secondPrivileges) {
				if(firstPrivilege.getId() == secondPrivilege.getFirstPrivilegeId()) {
					SecondPrivilegeDto secondPrivilegeDto = new SecondPrivilegeDto();
					secondPrivilegeDto.setFirstPrivilegeId(secondPrivilege.getFirstPrivilegeId());
					secondPrivilegeDto.setId(secondPrivilege.getId());
					secondPrivilegeDto.setPrivilegeKey(secondPrivilege.getPrivilegeKey());
					secondPrivilegeDto.setPrivilegeName(secondPrivilege.getPrivilegeName());
					secondPrivilegeDto.setSortIndex(secondPrivilege.getSortIndex());
					listSecond.add(secondPrivilegeDto);
				}
			}
			allPrivilege.setSecondPrivilege(listSecond);
			list.add(allPrivilege);
		}
		return list;
	}

	@Override
	@Transactional
	public void saveUserRole(String roleName) throws Exception {
		if(StringUtils.isEmpty(roleName)) {
			throw new Exception("角色名称不能为空！");
		}
		
		List<UserRole> roles = userRoleMapper.findByMap(MapUtils.put("roleName", roleName).getMap(), UserRole.class);
		if(roles.size() > 0){
			throw new Exception("角色名称重复");
		}
		
		UserRole userRole = new UserRole();
		userRole.setRoleName(roleName);
		Integer MaxSortIndex = userRoleMapper.findMaxSortIndex()+1;
		userRole.setSortIndex(MaxSortIndex);
		userRole.setIsDefault(0);
		
		userRoleMapper.save(userRole);
	}

	@Override
	@Transactional
	public void updateUserRole(int id, String roleName) throws Exception {
		if(StringUtils.isEmpty(id)) {
			throw new Exception("用户角色Id不能为空！");
		}
		if(StringUtils.isEmpty(roleName)) {
			throw new Exception("用户角色名称不能为空！");
		}
		int count = userRoleMapper.findOtherUserName(roleName, id);
		if(count > 0) {
			throw new Exception("用户角色名称重复！");
		}
		
		userRoleMapper.updateMapById(id, MapUtils.put("roleName", roleName).getMap(), UserRole.class);
	}

	@Override
	public List<UserRole> findAllUserRole() throws Exception {
		Criteria userRole = Criteria.forClass(UserRole.class);
		userRole.setOrder(Order.asc("sortIndex"));
		List<UserRole> userRoles = userRoleMapper.findByCriteria(userRole);
		return userRoles;
	}

	@Override
	public void deleteUserRole(int id) throws Exception {
		if (0 == id) {
			throw new Exception("角色Id不能为空！");
		}
		List<SysUser> listUser = sysUserMapper.findByMap(MapUtils.put("roleId", id).getMap(), SysUser.class);
		if (listUser.size() >0) {
			throw new Exception("该角色下存在用户，无法删除");
		}	

		// 根据角色编码删除所有角色二级权限
		roleSecondPrivilegeMapper.deleteByMap(MapUtils.put("roleId", id).getMap(), RoleSecondPrivilege.class);

		// 删除一级权限下所有角色一级权限
		roleFirstPrivilegeMapper.deleteByMap(MapUtils.put("roleId", id).getMap(), RoleFirstPrivilege.class);

		// 删除角色权限
		userRoleMapper.deleteById(id, UserRole.class);

	}

	@Override
	public void updateSortIndex(List<Integer> ids) {
		for(int i =0; i < ids.size(); i++) {
			baseSortIndexMapper.updateSortIndex("UserRole",i+1, ids.get(i));
		}
 		
	}

}
