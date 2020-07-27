package com.prolog.eis.dto.eis;

import com.prolog.eis.model.eis.ThroughTask;
import com.prolog.eis.model.eis.WmsInboundTask;

public class InStoreValidateDto {

	private boolean success;
	
	/**
	 * 1 空托任務  2任務托  3 借道任務
	 */
	private int resultType;
	
	private String msg;
	
	private boolean detection;
	
	private WmsInboundTask wmsInboundTask;
	
	private ThroughTask throughTask;

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

	public WmsInboundTask getWmsInboundTask() {
		return wmsInboundTask;
	}

	public void setWmsInboundTask(WmsInboundTask wmsInboundTask) {
		this.wmsInboundTask = wmsInboundTask;
	}

	public ThroughTask getThroughTask() {
		return throughTask;
	}

	public void setThroughTask(ThroughTask throughTask) {
		this.throughTask = throughTask;
	}
}
