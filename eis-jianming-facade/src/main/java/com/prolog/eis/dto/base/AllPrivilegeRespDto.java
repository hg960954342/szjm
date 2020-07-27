package com.prolog.eis.dto.base;

import java.util.List;

public class AllPrivilegeRespDto {

	private int id;
	
	private String privilegeKey;
	
	private String privilegeName;
	
	private String  sortIndex;
	
	private List<SecondPrivilegeDto> secondPrivilege;

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

	public List<SecondPrivilegeDto> getSecondPrivilege() {
		return secondPrivilege;
	}

	public void setSecondPrivilege(List<SecondPrivilegeDto> secondPrivilege) {
		this.secondPrivilege = secondPrivilege;
	}

	public AllPrivilegeRespDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AllPrivilegeRespDto(int id, String privilegeKey, String privilegeName, String sortIndex,
			List<SecondPrivilegeDto> secondPrivilege) {
		super();
		this.id = id;
		this.privilegeKey = privilegeKey;
		this.privilegeName = privilegeName;
		this.sortIndex = sortIndex;
		this.secondPrivilege = secondPrivilege;
	}

	@Override
	public String toString() {
		return "AllPrivilegeRespDto [id=" + id + ", privilegeKey=" + privilegeKey + ", privilegeName=" + privilegeName
				+ ", sortIndex=" + sortIndex + ", secondPrivilege=" + secondPrivilege + "]";
	}

}
