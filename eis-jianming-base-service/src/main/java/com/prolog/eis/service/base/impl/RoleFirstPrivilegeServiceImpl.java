package com.prolog.eis.service.base.impl;

import com.prolog.eis.dao.base.RoleFirstPrivilegeMapper;
import com.prolog.eis.dao.base.RoleSecondPrivilegeMapper;
import com.prolog.eis.dao.base.SecondPrivilegeMapper;
import com.prolog.eis.model.base.RoleFirstPrivilege;
import com.prolog.eis.model.base.RoleSecondPrivilege;
import com.prolog.eis.model.base.SecondPrivilege;
import com.prolog.eis.service.base.RoleFirstPrivilegeService;
import com.prolog.eis.util.PrologStringUtils;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleFirstPrivilegeServiceImpl implements RoleFirstPrivilegeService {

	@Autowired
	private RoleFirstPrivilegeMapper roleFirstPrivilegeMapper;
	@Autowired
	private RoleSecondPrivilegeMapper roleSecondPrivilegeMapper;
	@Autowired
	private SecondPrivilegeMapper secondPrivilegeMapper;

	@Override
	@Transactional
	public void saveRoleFirstPrivilege(int roleId, int firstPrivilegeId) throws Exception {
		if (0 == roleId) {
			throw new Exception("角色Id不能为空！");
		}
		if (0 == firstPrivilegeId) {
			throw new Exception("角色一级权限不能为空！");
		}
		RoleFirstPrivilege roleFirstPrivilege = new RoleFirstPrivilege();
		roleFirstPrivilege.setFirstPrivilegeId(firstPrivilegeId);
		roleFirstPrivilege.setRoleId(roleId);
		roleFirstPrivilegeMapper.save(roleFirstPrivilege);
		
		// 查询一级权限下所有的角色二级权限
		List<SecondPrivilege> secondPrivileges = secondPrivilegeMapper.findByMap(MapUtils.put("firstPrivilegeId", firstPrivilegeId).getMap(), SecondPrivilege.class);
		if(secondPrivileges.size() > 0) {
			List<RoleSecondPrivilege> roleSecond = new ArrayList<RoleSecondPrivilege>();
			
			for(SecondPrivilege second:secondPrivileges) {
				RoleSecondPrivilege roleSecondPrivilege = new RoleSecondPrivilege();
				roleSecondPrivilege.setRoleId(roleId);
				roleSecondPrivilege.setSecondPrivilegeId(second.getId());
				
				roleSecond.add(roleSecondPrivilege);
			}
			
			roleSecondPrivilegeMapper.saveBatch(roleSecond);
		}
	}

	@Override
	@Transactional
	public void deleteRoleFirstPrivilege(int roleId, int firstPrivilegeId) throws Exception {
		if (0 == roleId) {
			throw new Exception("角色Id不能为空！");
		}
		if (0 == firstPrivilegeId) {
			throw new Exception("角色一级权限不能为空！");
		}
		// 查询一级权限下所有的二级权限
		List<SecondPrivilege> secondPrivileges = secondPrivilegeMapper
				.findByMap(MapUtils.put("firstPrivilegeId", firstPrivilegeId).getMap(), SecondPrivilege.class);
		if (secondPrivileges.size() > 0) {
			List<Integer> ids = new ArrayList<Integer>();

			for (SecondPrivilege secondPrivilege : secondPrivileges) {
				ids.add(secondPrivilege.getId());
			}
			Criteria crt = Criteria.forClass(RoleSecondPrivilege.class);
			Integer[] its = ids.toArray(new Integer[0]);
			String str = "";
			for (int i : its) {
				str += i + ",";
			}
			str = PrologStringUtils.getReplacStr(str);
			crt.setRestriction(
					Restrictions.and(Restrictions.in("secondPrivilegeId", str), Restrictions.eq("roleId", roleId)));
			// 删除该角色下的所有二级权限
			roleSecondPrivilegeMapper.deleteByCriteria(crt);
		}
		// 删除该角色下的一级权限
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleId", roleId);
		map.put("firstPrivilegeId", firstPrivilegeId);
		roleFirstPrivilegeMapper.deleteByMap(map, RoleFirstPrivilege.class);

	}

}
