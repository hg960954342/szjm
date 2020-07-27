package com.prolog.eis.service.base.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prolog.eis.dao.base.RoleFirstPrivilegeMapper;
import com.prolog.eis.dao.base.RoleSecondPrivilegeMapper;
import com.prolog.eis.model.base.RoleFirstPrivilege;
import com.prolog.eis.model.base.RoleSecondPrivilege;
import com.prolog.eis.service.base.RoleSecondPrivilegeService;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;

@Service
public class RoleSecondPrivilegeServiceImpl implements RoleSecondPrivilegeService{
	
	@Autowired
	private RoleSecondPrivilegeMapper roleSecondPrivilegeMapper;
	@Autowired
	private RoleFirstPrivilegeMapper roleFirstPrivilegeMapper;

	@Override
	@Transactional
	public void saveRoleSecondPrivile(int roleId, int secondPrivilegeId,int firstPrivilegeId) throws Exception {
		if(0 == roleId) {
			throw new Exception("角色Id不能为空！");
		}
		if(0 == secondPrivilegeId) {
			throw new Exception("角色二级权限Id不能为空！");
		}
		if(0 == firstPrivilegeId) {
			throw new Exception("角色一级权限Id不能为空！");
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("firstPrivilegeId", firstPrivilegeId);
		map.put("roleId", roleId);
		List<RoleFirstPrivilege> firstPrivileges = roleFirstPrivilegeMapper.findByMap(map, RoleFirstPrivilege.class);
		//如果查询角色一级权限为空的话则保存
		if(0 == firstPrivileges.size()) {
			RoleFirstPrivilege roleFirstPrivilege = new RoleFirstPrivilege();
			roleFirstPrivilege.setFirstPrivilegeId(firstPrivilegeId);
			roleFirstPrivilege.setRoleId(roleId);
			roleFirstPrivilegeMapper.save(roleFirstPrivilege);
		}
		Criteria  crt = Criteria.forClass(RoleSecondPrivilege.class);
		crt.setRestriction(Restrictions.and(Restrictions.eq("roleId", roleId),Restrictions.eq("secondPrivilegeId", secondPrivilegeId)));
		List<RoleSecondPrivilege> secondPrivileges = roleSecondPrivilegeMapper.findByCriteria(crt);
		if(0 == secondPrivileges.size()) {
			RoleSecondPrivilege roleSecondPrivilege = new  RoleSecondPrivilege();
			roleSecondPrivilege.setRoleId(roleId);
			roleSecondPrivilege.setSecondPrivilegeId(secondPrivilegeId);
			roleSecondPrivilegeMapper.save(roleSecondPrivilege);
		}
	}

	@Override
	@Transactional
	public void deleteRoleSecondPrivilege(int roleId, int secondPrivilegeId) throws Exception {
		if(0 == roleId) {
			throw new Exception("角色Id不能为空！");
		}
		if(0 == secondPrivilegeId) {
			throw new Exception("角色二级权限Id不能为空！");
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("roleId", roleId);
		map.put("secondPrivilegeId", secondPrivilegeId);
		roleSecondPrivilegeMapper.deleteByMap(map, RoleSecondPrivilege.class);
	}

}
