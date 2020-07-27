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
import com.prolog.eis.dao.base.RoleSecondPrivilegeMapper;
import com.prolog.eis.dao.base.SecondPrivilegeMapper;
import com.prolog.eis.dto.base.AllPrivilegeRespDto;
import com.prolog.eis.dto.base.SecondPrivilegeDto;
import com.prolog.eis.model.base.FirstPrivilege;
import com.prolog.eis.model.base.RoleSecondPrivilege;
import com.prolog.eis.model.base.SecondPrivilege;
import com.prolog.eis.service.base.SecondPrivilegeService;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Order;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;

@Service
public class SecondPrivilegeServiceImpl implements SecondPrivilegeService{

	@Autowired
	private SecondPrivilegeMapper secondPrivilegeMapper;
	@Autowired
	private FirstPrivilegeMapper firstPrivilegeMapper;
	@Autowired
	private RoleSecondPrivilegeMapper roleSecondPrivilegeMapper;
	@Autowired
	private BaseSortIndexMapper baseSortIndexMapper;

	@Override
	public List<SecondPrivilege> findSecondPrivilege(int firstPrivilegeId) throws Exception{
		if(StringUtils.isEmpty(firstPrivilegeId)) {
			throw new Exception("一级权限id不能为空");
		}
		Criteria secondCrt = Criteria.forClass(SecondPrivilege.class);
		secondCrt.setOrder(Order.asc("sortIndex"));
		secondCrt.setRestriction(Restrictions.eq("firstPrivilegeId", firstPrivilegeId));
		List<SecondPrivilege> secondPrivileges = secondPrivilegeMapper.findByCriteria(secondCrt);
		return secondPrivileges;
	}

	@Override
	public List<AllPrivilegeRespDto> allAllPrivilegeResp() throws Exception {
		//查询一级权限并排序
		Criteria firstCrt = Criteria.forClass(FirstPrivilege.class);
		firstCrt.setOrder(Order.asc("sortIndex"));
		List<FirstPrivilege> firstPrivileges = firstPrivilegeMapper.findByCriteria(firstCrt);
		//查询二级权限并排序
		Criteria secondCrt = Criteria.forClass(SecondPrivilege.class);
		secondCrt.setOrder(Order.asc("sortIndex"));
		List<SecondPrivilege> secondPrivileges = secondPrivilegeMapper.findByCriteria(secondCrt);
		
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
	public void saveSecondPrivilege(SecondPrivilege secondPrivilege) throws Exception {
		if(StringUtils.isEmpty(secondPrivilege)) {
			throw new Exception("参数不能为空！");
		}
		if(StringUtils.isEmpty(secondPrivilege.getFirstPrivilegeId())) {
			throw new Exception("一级权限Id不能为空！");
		}
		if(StringUtils.isEmpty(secondPrivilege.getPrivilegeKey())) {
			throw new Exception("权限key不能为空！");
		}
		if(StringUtils.isEmpty(secondPrivilege.getPrivilegeName())) {
			throw new Exception("权限名称不能为空！");
		}
		if(StringUtils.isEmpty(secondPrivilege.getPrivilegeKey())) {
			throw new Exception("排序索引不能为空！");
		}
		secondPrivilegeMapper.save(secondPrivilege);
	}

	@Override
	@Transactional
	public void updateSecondPrivilege(SecondPrivilege secondPrivilege) throws Exception {
		if(StringUtils.isEmpty(secondPrivilege)) {
			throw new Exception("参数不能为空！");
		}
		if(StringUtils.isEmpty(secondPrivilege.getId())) {
			throw new Exception("一级权限Id不能为空！");
		}
		if(StringUtils.isEmpty(secondPrivilege.getPrivilegeKey())) {
			throw new Exception("权限key不能为空！");
		}
		if(StringUtils.isEmpty(secondPrivilege.getPrivilegeName())) {
			throw new Exception("权限名称不能为空！");
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("privilegeKey",secondPrivilege.getPrivilegeKey());
		map.put("privilegeName", secondPrivilege.getPrivilegeName());
		secondPrivilegeMapper.updateMapById(secondPrivilege.getId(), map, SecondPrivilege.class);
	}

	@Override
	public void deleteSecondPrivilege(int id) throws Exception {
		if (0 == id) {
			throw new Exception("角色二级权限不能为空！");
		}
		//删除二级权限下所有角色二级权限
		roleSecondPrivilegeMapper.deleteByMap(MapUtils.put("secondPrivilegeId", id).getMap(), RoleSecondPrivilege.class);
		
		//删除二级权限
		secondPrivilegeMapper.deleteById(id, SecondPrivilege.class);
	}

	@Override
	public void updateSortIndex(List<Integer> ids) {
		for(int i =0; i < ids.size(); i++) {
			baseSortIndexMapper.updateSortIndex("SecondPrivilege",i+1, ids.get(i));
		}			
	}

}
