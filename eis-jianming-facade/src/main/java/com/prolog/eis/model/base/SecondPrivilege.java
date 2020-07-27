package com.prolog.eis.model.base;
import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("SECONDPRIVILEGE")
public class SecondPrivilege extends BaseModel{

	@Id
	@AutoKey(sequence = "SECONDPRIVILEGE_SEQ",type = AutoKey.TYPE_IDENTITY)
	@ApiModelProperty("id")
	private int id;
	
	@ApiModelProperty("一级权限id")
	private int firstPrivilegeId;
	
	@ApiModelProperty("权限key")
	private String privilegeKey;
	
	@ApiModelProperty("权限名称")
	private String privilegeName;
	
	@ApiModelProperty("排序索引")
	private int sortIndex;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFirstPrivilegeId() {
		return firstPrivilegeId;
	}

	public void setFirstPrivilegeId(int firstPrivilegeId) {
		this.firstPrivilegeId = firstPrivilegeId;
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

	public int getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}

	public SecondPrivilege() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SecondPrivilege(int id, int firstPrivilegeId, String privilegeKey, String privilegeName, int sortIndex) {
		super();
		this.id = id;
		this.firstPrivilegeId = firstPrivilegeId;
		this.privilegeKey = privilegeKey;
		this.privilegeName = privilegeName;
		this.sortIndex = sortIndex;
	}

	@Override
	public String toString() {
		return "SecondPrivilege [id=" + id + ", firstPrivilegeId=" + firstPrivilegeId + ", privilegeKey=" + privilegeKey
				+ ", privilegeName=" + privilegeName + ", sortIndex=" + sortIndex + "]";
	}
	
}
