package com.prolog.eis.model.base;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

@Table("ROLESECONDPRIVILEGE")
public class RoleSecondPrivilege extends BaseModel{

	@Id
	@AutoKey(sequence = "ROLESECONDPRIVILEGE_SEQ",type = AutoKey.TYPE_IDENTITY)
	@ApiModelProperty("id")
	private int id;
	
	@ApiModelProperty("角色Id")
	private int roleId;
	
	@ApiModelProperty("角色二级权限")
	private int secondPrivilegeId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getSecondPrivilegeId() {
		return secondPrivilegeId;
	}

	public void setSecondPrivilegeId(int secondPrivilegeId) {
		this.secondPrivilegeId = secondPrivilegeId;
	}

	public RoleSecondPrivilege() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RoleSecondPrivilege(int id, int roleId, int secondPrivilegeId) {
		super();
		this.id = id;
		this.roleId = roleId;
		this.secondPrivilegeId = secondPrivilegeId;
	}

	@Override
	public String toString() {
		return "RoleSecondPrivilege [id=" + id + ", roleId=" + roleId + ", secondPrivilegeId=" + secondPrivilegeId
				+ "]";
	}
}
