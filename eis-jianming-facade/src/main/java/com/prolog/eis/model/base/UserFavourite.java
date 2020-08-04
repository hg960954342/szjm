package com.prolog.eis.model.base;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

@Table("USERFAVOURITE")
public class UserFavourite extends BaseModel{

	@Id
	@AutoKey(sequence = "USERFAVOURITE_SEQ",type = AutoKey.TYPE_IDENTITY)
	@ApiModelProperty("id")
	private int id ;
	
	@ApiModelProperty("用户Id")
	private int userId;
	
	@ApiModelProperty("角色一级权限Id")
	private int firstPrivilegeId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getFirstPrivilegeId() {
		return firstPrivilegeId;
	}

	public void setFirstPrivilegeId(int firstPrivilegeId) {
		this.firstPrivilegeId = firstPrivilegeId;
	}

	public UserFavourite() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserFavourite(int id, int userId, int firstPrivilegeId) {
		super();
		this.id = id;
		this.userId = userId;
		this.firstPrivilegeId = firstPrivilegeId;
	}

	@Override
	public String toString() {
		return "UserFavourite [id=" + id + ", userId=" + userId + ", firstPrivilegeId=" + firstPrivilegeId + "]";
	}
	
}
