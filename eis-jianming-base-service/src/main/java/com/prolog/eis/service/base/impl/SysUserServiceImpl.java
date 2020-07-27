package com.prolog.eis.service.base.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.prolog.eis.dao.base.SysUserMapper;
import com.prolog.eis.dao.base.UserPictureMapper;
import com.prolog.eis.dto.base.AllPrivilegeRespDto;
import com.prolog.eis.dto.base.SysUserRespDto;
import com.prolog.eis.dto.base.UserLoginRespDto;
import com.prolog.eis.dto.base.UserPwdReqDto;
import com.prolog.eis.model.base.SysUser;
import com.prolog.eis.model.base.UserPicture;
import com.prolog.eis.service.base.SecondPrivilegeService;
import com.prolog.eis.service.base.SysUserService;
import com.prolog.eis.service.base.UserFavouriteService;
import com.prolog.eis.service.base.UserRoleService;
import com.prolog.eis.util.PrologDateUtils;
import com.prolog.eis.util.PrologMd5Util;
import com.prolog.framework.utils.MapUtils;

@Service
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	private UserPictureMapper userPictureMapper;
	@Autowired
	private UserFavouriteService userFavouriteService;
	@Autowired
	private UserRoleService userRoleService;
	@Autowired
	private SecondPrivilegeService secondPrivilegeService;

	@Override
	@Transactional
	public UserLoginRespDto login(String userName, String pwd) throws Exception {
		if(StringUtils.isEmpty(userName)) {
			throw new Exception("用户姓名不能为空！");
		}
		if(StringUtils.isEmpty(pwd)) {
			throw new Exception("密码不能为空！");
		}
		pwd = PrologMd5Util.md5(pwd);
		List<SysUser> user = sysUserMapper.findUser(userName);
		if(user.size()>1){
			throw new Exception("请联系管理员，存在多个用户！");
		}
		if(0 == user.size()) {
			throw new Exception("账户或密码不正确!");
		}
		if(1 == user.size()){
			if(user.get(0).getUserPassword().equals(pwd)) {
				SysUser currUser = user.get(0) ;
				Date lastLoginTime = PrologDateUtils.parseObject(new Date());
				sysUserMapper.updateMapById(currUser.getId(), MapUtils.put("lastLoginTime", lastLoginTime).getMap(),SysUser.class);
			}else {
				throw new Exception("账户或密码不正确!");
			}
		}
		UserLoginRespDto userLoginResp = new UserLoginRespDto();
		BeanUtils.copyProperties(user.get(0),userLoginResp);
		List<Integer> userFavourites = userFavouriteService.queryUserFavourite(user.get(0).getId());
		userLoginResp.setUserFavourite(userFavourites);
		List<AllPrivilegeRespDto> allPrivileges = null;
		if("admin".equals(userName)) {
			allPrivileges = secondPrivilegeService.allAllPrivilegeResp();
		}else {
			allPrivileges = userRoleService.findUserRole(user.get(0).getRoleId());
		}
		userLoginResp.setAllPrivilege(allPrivileges);
		return userLoginResp;
	}

	@Override
	@Transactional
	public void userPictureUpload(int userId ,byte[] pictureBytes,String fileExtend) throws Exception {
		if(StringUtils.isEmpty(userId)) {
			throw new Exception("用户姓名不能为空！");
		}
		List<UserPicture> userPictureList = userPictureMapper.findByMap(MapUtils.put("userId", userId).getMap(), UserPicture.class);
		if(userPictureList.size() == 0) {
			UserPicture entity = new UserPicture();
			entity.setUserId(userId);
			entity.setPictureBytes(pictureBytes);
			entity.setFileExtend(fileExtend);
			userPictureMapper.save(entity);
		}else {
			UserPicture userPicture = userPictureList.get(0);
			
			userPicture.setPictureBytes(pictureBytes);
			userPicture.setFileExtend(fileExtend);
			userPictureMapper.update(userPicture);
		}
	}

	@Override
	public UserPicture userPictureDownload(int userId) throws Exception {
		if(0 == userId) {
			throw new Exception("用户id不能为空");
		}
		List<UserPicture> userPictures = userPictureMapper.findByMap(MapUtils.put("userId", userId).getMap(), UserPicture.class);
		if(userPictures.size()>0) {
			return userPictures.get(0);
		}else{
			return null;
		}
		
	}

	@Override
	public void updatePassWord(UserPwdReqDto userRequest) throws Exception {
		if(StringUtils.isEmpty(userRequest.getOldPwd())) {
			throw new Exception("原始密码不能为空！");
		}
		if(StringUtils.isEmpty(userRequest.getOldPwd())) {
			throw new Exception("新密码不能为空！");
		}
		if(userRequest.getOldPwd().equals(userRequest.getNewPwd())) {
			throw new Exception("新密码和原始密码一致，请重新输入！");
		}
		String pwd = PrologMd5Util.md5(userRequest.getNewPwd());
		sysUserMapper.updateMapById(userRequest.getUserId(), MapUtils.put("userPassWord", pwd).getMap(), SysUser.class);
	}

	@Override
	public List<SysUserRespDto> findSysUser(String name ,String fullPath) throws Exception {
		//List<SysUser> sysUsers = sysUserMapper.findByMap(MapUtils.put("", "").getMap(), SysUser.class);
		name = "%"+name+"%";
		fullPath = fullPath+"%";
		List<SysUserRespDto> sysUsers = sysUserMapper.queryUser(name, fullPath);
		return sysUsers;
	}

	@Override
	public void saveSysUser(SysUser user) throws Exception {
		if(StringUtils.isEmpty(user.getUserName())) {
			throw new Exception("用户姓名不能为空！");
		}
		if(StringUtils.isEmpty(user.getRoleId())) {
			throw new Exception("用户角色不能为空！");
		}
		if(StringUtils.isEmpty(user.getLoginName())) {
			throw new Exception("账户不能为空！");
		}
		if(0 == user.getUserDeptId()) {
			throw new Exception("部门Id不能为空！");
		}
		String pwd = PrologMd5Util.md5("888888");
		user.setUserPassword(pwd);
		user.setCreateTime(new Date());
		
		List<SysUser> tem = sysUserMapper.findByMap(MapUtils.put("loginName", user.getLoginName()).getMap(), SysUser.class);
		if(tem.size() > 0)
		{
			throw new Exception("账户已存在！");
		}
		
		sysUserMapper.save(user);
	}

	@Override
	public void updateSysUser(SysUser user) throws Exception {
		if (0 == user.getId()) {
			throw new Exception("用户id不能为空！");
		}
		
		List<SysUser> tem = sysUserMapper.findOtherUserLoginName(user.getLoginName(), user.getId());
		if(tem.size() > 0)
		{
			throw new Exception("账户已存在！");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("loginName", user.getLoginName());
		map.put("userName", user.getUserName());
		map.put("sex", user.getSex());
		map.put("mobile", user.getMobile());
		map.put("userDeptId", user.getUserDeptId());
		map.put("roleId", user.getRoleId());
		map.put("workNo", user.getWorkNo());
		sysUserMapper.updateMapById(user.getId(), map, SysUser.class);
	}

	@Override
	public SysUser querySysUser(int id) throws Exception {
		if(0 == id) {
			throw new Exception("用户Id不能为空");
		}
		SysUser sysUser= sysUserMapper.findById(id, SysUser.class);
		return sysUser;
	}

	@Override
	public SysUser findUserNoPwd(int id) throws Exception {
		if (0 == id) {
			throw new Exception("用户Id不能为空");
		}
		SysUser user = sysUserMapper.findUserNoPwd(id);
		return user;
	}

	@Override
	public List<SysUserRespDto> queryUserNoPwd(String name, String fullPath) throws Exception {
		name = "'%"+name+"%'";
		fullPath = "'%"+fullPath+"%'";
 		List<SysUserRespDto> respDtoList = sysUserMapper.queryUserNoPwd(name, fullPath);
 		return respDtoList;
	}
}
