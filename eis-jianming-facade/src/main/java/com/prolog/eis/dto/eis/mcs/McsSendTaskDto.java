package com.prolog.eis.dto.eis.mcs;

public class McsSendTaskDto {

	/**
	 * 规则HHmmss+四位流水码(1-9999)
	 */
	private String taskId;
	/**
	 * 任务类型：1：入库:2：出库 3 移位 4输送线前进
	 */
	private int type;
	
	/**
	 * 库编号
	 */
	private int bankId;
	/**
	 * 母托盘编号
	 */
	private String containerNo;
	/**
	 * 请求位置:原坐标
	 */
	private String address;
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
	private String priority;
	/**
	 * 任务优先级,0-99,0优先级最大
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
	public int getBankId() {
		return bankId;
	}
	public void setBankId(int bankId) {
		this.bankId = bankId;
	}
	public String getContainerNo() {
		return containerNo;
	}
	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
