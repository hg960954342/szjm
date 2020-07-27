package com.prolog.eis.dto.eis.mcs;

public class McsRequestTaskDto {

	/**
	 * 母托盘编号
	 */
	private String stockId;
	/**
	 * 请求位置:原坐标
	 */
	private String source;
	/**
	 * 目的位置：目的坐标
	 */
	private String target;
	
	private boolean success;
	
	private String errorMessage;

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public McsRequestTaskDto(String stockId, String source, String target, boolean success, String errorMessage) {
		super();
		this.stockId = stockId;
		this.source = source;
		this.target = target;
		this.success = success;
		this.errorMessage = errorMessage;
	}

	public McsRequestTaskDto() {
		super();
		// TODO Auto-generated constructor stub
	}
}
