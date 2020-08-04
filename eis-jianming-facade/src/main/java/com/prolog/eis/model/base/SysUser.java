package com.prolog.eis.model.base;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@Table(value = "SYSUSER")
public class SysUser extends BaseModel{

	@Id
	@AutoKey(sequence = "SYSUSER_SEQ",type = AutoKey.TYPE_IDENTITY)
	@ApiModelProperty("用户id")
	private int id;
	
	@ApiModelProperty("用户部门id")
	private int userDeptId;
	
	@ApiModelProperty("账户")
	private String loginName;
	
	@ApiModelProperty("姓名")
	private String userName;
	
	@ApiModelProperty("密码")
	private String userPassword;
	
	@ApiModelProperty("角色id")
	private int roleId;
	
	@ApiModelProperty("性别 0:无 1:男 2:女")
	private int sex;
	
	@ApiModelProperty("联系电话")
	private String mobile;
	
	@ApiModelProperty("最后一次登录时间")
	private Date lastLoginTime;
	
	@ApiModelProperty("工号")
	@Column("WORK_NO")
	private String workNo;
	
	@ApiModelProperty("创建时间")
	private Date createTime;

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

	
	public String getWorkNo() {
		return workNo;
	}

	public void setWorkNo(String workNo) {
		this.workNo = workNo;
	}

	@Override
	public String toString() {
		return "SysUser [id=" + id + ", userDeptId=" + userDeptId + ", loginName=" + loginName + ", userName="
				+ userName + ", userPassword=" + userPassword + ", roleId=" + roleId + ", sex=" + sex + ", mobile="
				+ mobile + ", lastLoginTime=" + lastLoginTime + ", workNo=" + workNo + ", createTime=" + createTime
				+ "]";
	}



}
