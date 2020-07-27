package com.prolog.eis.dto.eis.mcs;

public class InBoundRequest {

	/**
	 * 规则HHmmss+四位流水码(1-9999)
	 */
	private String taskId;
	/**
	 * 任务类型：1：入库:2：出库 3 跨层
	 */
	private int type;
	/**
	 * 母托盘编号
	 */
	private String stockId;
	/**
	 * 字托盘编号
	 */
	private String stockIdSub;
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
	 * 尺寸检测信息：1：正常，2：左超长，3：右超长；4：上超长；5：下超长，6：超高
	 */
	private int detection;
	/**
	 * 任务优先级,0-99,0优先级最大
	 */
	private int priority;
	/**
	 *【任务状态：1：任务开始；2：任务完成】
	 */
	private int status;
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
	public String getStockIdSub() {
		return stockIdSub;
	}
	public void setStockIdSub(String stockIdSub) {
		this.stockIdSub = stockIdSub;
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
	public int getDetection() {
		return detection;
	}
	public void setDetection(int detection) {
		this.detection = detection;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public InBoundRequest(String taskId, int type, String stockId, String stockIdSub, String source, String target,
			String weight, int detection, int priority, int status) {
		super();
		this.taskId = taskId;
		this.type = type;
		this.stockId = stockId;
		this.stockIdSub = stockIdSub;
		this.source = source;
		this.target = target;
		this.weight = weight;
		this.detection = detection;
		this.priority = priority;
		this.status = status;
	}
	public InBoundRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "InBoundRequest [taskId=" + taskId + ", type=" + type + ", stockId=" + stockId + ", stockIdSub="
				+ stockIdSub + ", source=" + source + ", target=" + target + ", weight=" + weight + ", detection="
				+ detection + ", priority=" + priority + ", status=" + status + "]";
	}
}
