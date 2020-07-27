package com.prolog.eis.dto.base;

public class UserPwdReqDto {
	
	/**
	 * 用户Id
	 */
	private int userId;
	
	/**
	 * 旧密码
	 */
	private String oldPwd;
	
	/**
	 * 新密码
	 */
	private String newPwd;
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

}
