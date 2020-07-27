package com.prolog.eis.model.base;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("FIRSTPRIVILEGE")
public class FirstPrivilege extends BaseModel{

	@Id
	@AutoKey(sequence = "FirstPrivilege_SEQ",type = AutoKey.TYPE_IDENTITY)
	@ApiModelProperty("id")
	private int id;
	
	@ApiModelProperty("权限Key")
	private String privilegeKey;
	
	@ApiModelProperty("权限描述")
	private String privilegeName;
	
	@ApiModelProperty("排序索引")
	private String  sortIndex;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPrivilegeKey() {
		return privilegeKey;
	}

	public void setPrivilegeKey(String privilegeKey) {
		this.privilegeKey = privilegeKey;
	}


	public String getPrivilegeName() {
		return privilegeName;
	}

	public void setPrivilegeName(String privilegeName) {
		this.privilegeName = privilegeName;
	}

	public String getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(String sortIndex) {
		this.sortIndex = sortIndex;
	}

	public FirstPrivilege() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FirstPrivilege(int id, String privilegeKey, String privilegeName, String sortIndex) {
		super();
		this.id = id;
		this.privilegeKey = privilegeKey;
		this.privilegeName = privilegeName;
		this.sortIndex = sortIndex;
	}

	@Override
	public String toString() {
		return "FirstPrivilege [id=" + id + ", privilegeKey=" + privilegeKey + ", privilegeName=" + privilegeName
				+ ", sortIndex=" + sortIndex + "]";
	}

}
