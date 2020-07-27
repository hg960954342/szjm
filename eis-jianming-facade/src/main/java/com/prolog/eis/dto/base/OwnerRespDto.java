package com.prolog.eis.dto.base;

/**
 * 业主返回Dto
 * @author Dss
 * @date 2018年10月18日 下午3:43:17
 */
public class OwnerRespDto {
	
	private int id;
	
	private String ownerNo;
	
	private String ownerName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOwnerNo() {
		return ownerNo;
	}

	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
}
