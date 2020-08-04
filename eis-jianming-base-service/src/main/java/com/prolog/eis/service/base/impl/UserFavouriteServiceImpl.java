package com.prolog.eis.service.base.impl;

import com.prolog.eis.dao.base.UserFavouriteMapper;
import com.prolog.eis.model.base.UserFavourite;
import com.prolog.eis.service.base.UserFavouriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserFavouriteServiceImpl implements UserFavouriteService{

	@Autowired
	private UserFavouriteMapper userFavouriteMapper;
	
	@Override
	@Transactional
	public void addUserFavourite(UserFavourite userFavourite) throws Exception {
		if(StringUtils.isEmpty(userFavourite)) {
			throw new Exception("参数不能为空！");
		}
		if(0 == userFavourite.getFirstPrivilegeId()) {
			throw new Exception("一级权限Id不能为空！");
		}
		if(0 == userFavourite.getUserId()) {
			throw new Exception("用户Id不能为空！");
		}
		userFavouriteMapper.save(userFavourite);
	}

	@Override
	@Transactional
	public void removeUserFavourite(int userId, int firstPrivilegeId) throws Exception {
		if(0 == firstPrivilegeId) {
			throw new Exception("一级权限Id不能为空！");
		}
		if(0 == userId) {
			throw new Exception("用户Id不能为空！");
		}
		Map<String, Object> map  = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("firstPrivilegeId", firstPrivilegeId);
		userFavouriteMapper.deleteByMap(map, UserFavourite.class);
	}

	@Override
	public List<Integer> queryUserFavourite(int userId) throws Exception {
		if(0 == userId) {
			throw new Exception("用户Id不能为空！");
		}
		List<Integer> userFavourites = userFavouriteMapper.findFirstPrivilege(userId);
		return userFavourites;
	}

}
