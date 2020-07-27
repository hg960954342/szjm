package com.prolog.eis.model.base;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("ROLEFIRSTPRIVILEGE")
public class RoleFirstPrivilege extends BaseModel{

	@Id
	@AutoKey(sequence = "ROLEFIRSTPRIVILEGE_SEQ",type = AutoKey.TYPE_IDENTITY)
	@ApiModelProperty("id")
	private int id ;
	
	@ApiModelProperty("角色id")
	private int roleId;
	
	@ApiModelProperty("角色一级权限id")
	private int firstPrivilegeId;

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

	public int getFirstPrivilegeId() {
		return firstPrivilegeId;
	}

	public void setFirstPrivilegeId(int firstPrivilegeId) {
		this.firstPrivilegeId = firstPrivilegeId;
	}

	public RoleFirstPrivilege(int id, int roleId, int firstPrivilegeId) {
		super();
		this.id = id;
		this.roleId = roleId;
		this.firstPrivilegeId = firstPrivilegeId;
	}

	public RoleFirstPrivilege() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "RoleFirstPrivilege [id=" + id + ", roleId=" + roleId + ", firstPrivilegeId=" + firstPrivilegeId + "]";
	}
	
}
