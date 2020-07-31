package com.prolog.eis.dto.eis;

import com.prolog.eis.model.wms.InboundTask;

public class InStoreValidateDto {
private boolean success;
	
	/**
	 * 1 空托任務  2任務托  3 借道任務
	 */
	private int resultType;
	
	private String msg;
	
	private boolean detection;
	
	private InboundTask inboundTask;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getResultType() {
		return resultType;
	}

	public void setResultType(int resultType) {
		this.resultType = resultType;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isDetection() {
		return detection;
	}

	public void setDetection(boolean detection) {
		this.detection = detection;
	}

	public InboundTask getInboundTask() {
		return inboundTask;
	}

	public void setInboundTask(InboundTask inboundTask) {
		this.inboundTask = inboundTask;
	}
}
