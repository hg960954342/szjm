package com.prolog.eis.service.base.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.prolog.eis.dao.base.BaseSortIndexMapper;
import com.prolog.eis.dao.base.FirstPrivilegeMapper;
import com.prolog.eis.dao.base.RoleFirstPrivilegeMapper;
import com.prolog.eis.dao.base.RoleSecondPrivilegeMapper;
import com.prolog.eis.dao.base.SecondPrivilegeMapper;
import com.prolog.eis.dao.base.UserFavouriteMapper;
import com.prolog.eis.model.base.FirstPrivilege;
import com.prolog.eis.model.base.RoleFirstPrivilege;
import com.prolog.eis.model.base.RoleSecondPrivilege;
import com.prolog.eis.model.base.SecondPrivilege;
import com.prolog.eis.model.base.UserFavourite;
import com.prolog.eis.service.base.FirstPrivilegeService;
import com.prolog.eis.util.PrologStringUtils;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Order;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;

@Service
public class FirstPrivilegeServiceImpl implements FirstPrivilegeService {

	@Autowired
	private FirstPrivilegeMapper firstPrivilegeMapper;
	@Autowired
	private SecondPrivilegeMapper secondPrivilegeMapper;
	@Autowired
	private RoleFirstPrivilegeMapper roleFirstPrivilegeMapper;
	@Autowired
	private RoleSecondPrivilegeMapper roleSecondPrivilegeMapper;
	@Autowired
	private UserFavouriteMapper userFavouriteMapper;
	@Autowired
	private BaseSortIndexMapper baseSortIndexMapper;

	@Override
	public List<FirstPrivilege> findFirstPrivilege() throws Exception {
		Criteria firstCrt = Criteria.forClass(FirstPrivilege.class);
		firstCrt.setOrder(Order.asc("sortIndex"));
		List<FirstPrivilege> firstPrivileges = firstPrivilegeMapper.findByCriteria(firstCrt);
		return firstPrivileges;
	}

	@Override
	@Transactional
	public void saveFirstPrivilege(FirstPrivilege firstPrivilege) throws Exception {
		if (StringUtils.isEmpty(firstPrivilege)) {
			throw new Exception("参数不能为空！");
		}
		if (StringUtils.isEmpty(firstPrivilege.getPrivilegeName())) {
			throw new Exception("权限描述不能为空！");
		}
		if (StringUtils.isEmpty(firstPrivilege.getPrivilegeKey())) {
			throw new Exception("权限key不能为空！");
		}
		if (StringUtils.isEmpty(firstPrivilege.getPrivilegeKey())) {
			throw new Exception("排序索引不能为空！");
		}
		firstPrivilegeMapper.save(firstPrivilege);
	}

	@Override
	@Transactional
	public void updateFirstPrivilege(FirstPrivilege firstPrivilege) throws Exception {
		if (StringUtils.isEmpty(firstPrivilege)) {
			throw new Exception("参数不能为空！");
		}
		if (StringUtils.isEmpty(firstPrivilege.getId())) {
			throw new Exception("一级权限Id不能为空！");
		}
		if (StringUtils.isEmpty(firstPrivilege.getPrivilegeName())) {
			throw new Exception("权限描述不能为空！");
		}
		if (StringUtils.isEmpty(firstPrivilege.getPrivilegeKey())) {
			throw new Exception("权限key不能为空！");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("privilegeName", firstPrivilege.getPrivilegeName());
		map.put("privilegeKey", firstPrivilege.getPrivilegeKey());
		firstPrivilegeMapper.updateMapById(firstPrivilege.getId(), map, FirstPrivilege.class);
	}

	@Override
	@Transactional
	public void deleteFirstPrivilege(int id) throws Exception {
		if (0 == id) {
			throw new Exception("角色一级权限不能为空！");
		}
		// 查询一级权限下所有的角色二级权限
		List<SecondPrivilege> secondPrivileges = secondPrivilegeMapper.findByMap(MapUtils.put("firstPrivilegeId", id).getMap(), SecondPrivilege.class);
		if(secondPrivileges.size()>0) {

		List<Integer> ids = new ArrayList<Integer>();
		for (SecondPrivilege secondPrivilege : secondPrivileges) {
			ids.add(secondPrivilege.getId());
		}
		Integer[] its = ids.toArray(new Integer[0]);
		String str = "";
		for (int i : its) {
			str += i + ",";
		}
		str = PrologStringUtils.getReplacStr(str);
		Criteria crt = Criteria.forClass(RoleSecondPrivilege.class);
		crt.setRestriction(Restrictions.and(Restrictions.in("secondPrivilegeId", str)));
		// 删除一级权限下所有角色二级权限
		roleSecondPrivilegeMapper.deleteByCriteria(crt);
		}

		// 删除一级权限下所有二级权限
		secondPrivilegeMapper.deleteByMap(MapUtils.put("firstPrivilegeId", id).getMap(), SecondPrivilege.class);

		// 删除一级权限下所有角色一级权限
		roleFirstPrivilegeMapper.deleteByMap(MapUtils.put("firstPrivilegeId", id).getMap(), RoleFirstPrivilege.class);

		// 删除一级权限下所有用户收藏夹
		userFavouriteMapper.deleteByMap(MapUtils.put("firstPrivilegeId", id).getMap(), UserFavourite.class);

		// 删除一级权限
		firstPrivilegeMapper.deleteById(id, FirstPrivilege.class);

	}

	@Override
	public void updateSortIndex(List<Integer> ids) {
		for(int i =0; i < ids.size(); i++) {
			baseSortIndexMapper.updateSortIndex("FirstPrivilege",i+1, ids.get(i));
		}		
	}

}
