package com.prolog.eis.dto.eis.mcs;

public class McsRequestTaskDto {

	/**
	 * mcs任务类型
	 */
	private int type;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

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

	public McsRequestTaskDto(int type, String stockId, String source, String target, boolean success,
			String errorMessage) {
		super();
		this.type = type;
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
