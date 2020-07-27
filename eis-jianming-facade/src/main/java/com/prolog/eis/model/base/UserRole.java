package com.prolog.eis.model.base;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("USERROLE")
public class UserRole extends BaseModel{

	@Id
	@AutoKey(sequence = "USERROLE_SEQ",type = AutoKey.TYPE_IDENTITY)
	@ApiModelProperty("id")
	private int id;
	
	@ApiModelProperty("角色名称")
	private String roleName;
	
	@ApiModelProperty("是否默认角色")
	private int isDefault;
	
	@ApiModelProperty("排序索引")
	private int sortIndex;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}

	public int getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}

	public UserRole() {
		super();
	}

	public UserRole(int id, String roleName, int isDefault, int sortIndex) {
		super();
		this.id = id;
		this.roleName = roleName;
		this.isDefault = isDefault;
		this.sortIndex = sortIndex;
	}

	@Override
	public String toString() {
		return "UserRole [id=" + id + ", roleName=" + roleName + ", isDefault=" + isDefault + ", sortIndex=" + sortIndex
				+ "]";
	}
	
}
