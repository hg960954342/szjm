package com.prolog.eis.service.delet.impl;

import com.prolog.eis.dao.base.SysUserMapper;
import com.prolog.eis.dao.base.UserDeptMapper;
import com.prolog.eis.model.base.SysUser;
import com.prolog.eis.model.base.UserDept;
import com.prolog.eis.service.delet.DeleteService;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeleteServiceImpl implements DeleteService {

	@Autowired
	private UserDeptMapper userDeptMapper;
	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Override
	@Transactional
	public void DeleteUserDept(int id) throws Exception {
		if (0 == id) {
			throw new Exception("id不能为空！");
		}
		List<UserDept> userDeptNodes = userDeptMapper.findByMap(MapUtils.put("parentId", id).getMap(), UserDept.class);
		if (userDeptNodes.size() > 0) {
			throw new Exception("该节点下还有子节点无法删除！");
		}
		List<SysUser> users = sysUserMapper.findByMap(MapUtils.put("userDeptId", id).getMap(), SysUser.class);
		if (users.size() > 0) {
			throw new Exception("该部门下还有职员无法删除！");
		}
		userDeptMapper.deleteById(id, UserDept.class);
	}

	@Override
	@Transactional
	public void DeleteSysUser(int id) throws Exception {
		if (0 == id) {
			throw new Exception("id不能为空！");
		}
		sysUserMapper.deleteById(id, SysUser.class);
	}
}
