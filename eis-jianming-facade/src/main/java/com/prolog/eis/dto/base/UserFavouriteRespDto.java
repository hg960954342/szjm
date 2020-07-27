package com.prolog.eis.dto.base;

public class UserFavouriteRespDto {

	/**
	 * 权限id
	 */
	private int firstPrivilegeId;

	public int getFirstPrivilegeId() {
		return firstPrivilegeId;
	}

	public void setFirstPrivilegeId(int firstPrivilegeId) {
		this.firstPrivilegeId = firstPrivilegeId;
	}

	public UserFavouriteRespDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserFavouriteRespDto(int firstPrivilegeId) {
		super();
		this.firstPrivilegeId = firstPrivilegeId;
	}
	
}
