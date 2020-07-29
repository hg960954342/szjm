package com.prolog.eis.dto.rcs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RcsRequestResultDto {

	@JsonProperty
	private String code;
	@JsonProperty
	private String date;
	@JsonProperty
	private String message;
	@JsonProperty
	private String reqCode;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getReqCode() {
		return reqCode;
	}
	public void setReqCode(String reqCode) {
		this.reqCode = reqCode;
	}
}
