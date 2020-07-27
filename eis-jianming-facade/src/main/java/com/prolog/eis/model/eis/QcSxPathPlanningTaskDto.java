package com.prolog.eis.model.eis;

public class QcSxPathPlanningTaskDto {

	/**
	 * mxID
	 */
	private int id;		
	
	/**
	 * 序号
	 */
	private int sortIndex;		
	
	/**
	 * 任务汇总ID
	 */
	private int taskHzId;		
	
	/**
	 * 节点类型1：起点，2：过程点，3：终点
	 */
	private int nodeType;	
	
	/**
	 * 运输系统1：MCS，2：GCS
	 */
	private int transportationEquipment;
	
	/**
	 * 节点
	 */
	private String  node;
	
	/**
	 * 线体方向
	 */
	private String lineDirection;
	
	/**
	 * x坐标
	 */
	private int x;
	
	/**
	 * y坐标
	 */
	private int y;
	
	/**
	 * 层
	 */
	private int layer;
	
	/**
	 * 任务状态0：未完成，1：待开始，2：执行中，3：已完成
	 */
	private int isComplete;
	
	/**
	 * 任务ID
	 */
	private String taskId;
	
	/**
	 * 容器号
	 */
	private String containerNo;
	
	/**
	 * 路径任务类型
	 */
	private int pathType;
	
	/**
	 * 任务紧急程度
	 */
	private int emerge;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}

	public int getTaskHzId() {
		return taskHzId;
	}

	public void setTaskHzId(int taskHzId) {
		this.taskHzId = taskHzId;
	}

	public int getNodeType() {
		return nodeType;
	}

	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}

	public int getTransportationEquipment() {
		return transportationEquipment;
	}

	public void setTransportationEquipment(int transportationEquipment) {
		this.transportationEquipment = transportationEquipment;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getLineDirection() {
		return lineDirection;
	}

	public void setLineDirection(String lineDirection) {
		this.lineDirection = lineDirection;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(int isComplete) {
		this.isComplete = isComplete;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public int getPathType() {
		return pathType;
	}

	public void setPathType(int pathType) {
		this.pathType = pathType;
	}

	public int getEmerge() {
		return emerge;
	}

	public void setEmerge(int emerge) {
		this.emerge = emerge;
	}
}
