package com.prolog.eis.dto.base;

import java.util.Date;
import java.util.List;

public class UserLoginRespDto {
	
	private int id;
	
	private int userDeptId;
	
	private String loginName;
	
	private String userName;
	
	private String userPassword;
	
	private int roleId;
	
	private int sex;
	
	private String mobile;
	
	private Date lastLoginTime;
	
	private Date createTime;
	
	//一级权限Id
	private List<Integer> userFavourite;
	
	//用户下所有权限
	private List<AllPrivilegeRespDto> allPrivilege;

	public UserLoginRespDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserLoginRespDto(int id, int userDeptId, String loginName, String userName, String userPassword, int roleId,
			int sex, String mobile, Date lastLoginTime, Date createTime) {
		super();
		this.id = id;
		this.userDeptId = userDeptId;
		this.loginName = loginName;
		this.userName = userName;
		this.userPassword = userPassword;
		this.roleId = roleId;
		this.sex = sex;
		this.mobile = mobile;
		this.lastLoginTime = lastLoginTime;
		this.createTime = createTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserDeptId() {
		return userDeptId;
	}

	public void setUserDeptId(int userDeptId) {
		this.userDeptId = userDeptId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public List<Integer> getUserFavourite() {
		return userFavourite;
	}

	public void setUserFavourite(List<Integer> userFavourite) {
		this.userFavourite = userFavourite;
	}

	public List<AllPrivilegeRespDto> getAllPrivilege() {
		return allPrivilege;
	}

	public void setAllPrivilege(List<AllPrivilegeRespDto> allPrivilege) {
		this.allPrivilege = allPrivilege;
	}
}
