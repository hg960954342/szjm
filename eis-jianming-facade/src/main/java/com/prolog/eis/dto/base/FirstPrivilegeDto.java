package com.prolog.eis.dto.base;

import com.prolog.eis.model.base.FirstPrivilege;

import java.util.Map;

public class FirstPrivilegeDto extends FirstPrivilege{
	private Map<String,Object> data;

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public FirstPrivilegeDto(Map<String, Object> data) {
		super();
		this.data = data;
	}

	public FirstPrivilegeDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FirstPrivilegeDto(int id, String privilegeKey, String privilegeName, String sortIndex) {
		super(id, privilegeKey, privilegeName, sortIndex);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "FirstPrivilegeDto [data=" + data + "]";
	}
}
