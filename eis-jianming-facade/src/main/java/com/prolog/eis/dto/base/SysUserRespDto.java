package com.prolog.eis.dto.base;

import java.util.Date;

public class SysUserRespDto {

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
	
	private String objectName;
	
	private String fullPath;
	
	private String roleName;

	private String workNo;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}


	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public String getWorkNo() {
		return workNo;
	}

	public void setWorkNo(String workNo) {
		this.workNo = workNo;
	}

	public SysUserRespDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "SysUserRespDto [id=" + id + ", userDeptId=" + userDeptId + ", loginName=" + loginName + ", userName="
				+ userName + ", userPassword=" + userPassword + ", roleId=" + roleId + ", sex=" + sex + ", mobile="
				+ mobile + ", lastLoginTime=" + lastLoginTime + ", createTime=" + createTime + ", objectName="
				+ objectName + ", fullPath=" + fullPath + ", roleName=" + roleName + ", workNo=" + workNo + "]";
	}


}
