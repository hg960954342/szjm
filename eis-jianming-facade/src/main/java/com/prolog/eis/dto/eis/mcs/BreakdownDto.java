package com.prolog.eis.dto.eis.mcs;

public class BreakdownDto {

	/**
	 * 规则HHmmss+四位流水码(1-9999)
	 */
	private String taskId;
	/**
	 * 任务类型：1：入库:2：出库
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
	/**
	 * 入库重量
	 */
	private String weight;
	/**
	 * 任务优先级,0-99,0优先级最大
	 */
	private int priority;
	/**
	 * 错误类型，根据字典表匹配
	 */
	private int errorType;
	/**
	 * 消息内容
	 */
	private String msg;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
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
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getErrorType() {
		return errorType;
	}
	public void setErrorType(int errorType) {
		this.errorType = errorType;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public BreakdownDto(String taskId, int type, String stockId, String source, String target, String weight,
			int priority, int errorType, String msg) {
		super();
		this.taskId = taskId;
		this.type = type;
		this.stockId = stockId;
		this.source = source;
		this.target = target;
		this.weight = weight;
		this.priority = priority;
		this.errorType = errorType;
		this.msg = msg;
	}
	public BreakdownDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "BreakdownDto [taskId=" + taskId + ", type=" + type + ", stockId=" + stockId + ", source=" + source
				+ ", target=" + target + ", weight=" + weight + ", priority=" + priority + ", errorType=" + errorType
				+ ", msg=" + msg + "]";
	}
}
