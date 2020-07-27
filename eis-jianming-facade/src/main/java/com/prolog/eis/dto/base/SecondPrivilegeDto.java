package com.prolog.eis.dto.base;

public class SecondPrivilegeDto {

	private int id;
	
	private int firstPrivilegeId;
	
	private String privilegeKey;
	
	private String privilegeName;
	
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

	public SecondPrivilegeDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SecondPrivilegeDto(int id, int firstPrivilegeId, String privilegeKey, String privilegeName, int sortIndex) {
		super();
		this.id = id;
		this.firstPrivilegeId = firstPrivilegeId;
		this.privilegeKey = privilegeKey;
		this.privilegeName = privilegeName;
		this.sortIndex = sortIndex;
	}
	
}
