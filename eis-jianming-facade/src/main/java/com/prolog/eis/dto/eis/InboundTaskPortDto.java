package com.prolog.eis.dto.eis;

public class InboundTaskPortDto {

	private String wmsPortNo;
	
	private Integer taskId;
	
	private Integer taskType;
	
	private String portNo;
	
	private int finished;

	public String getWmsPortNo() {
		return wmsPortNo;
	}

	public void setWmsPortNo(String wmsPortNo) {
		this.wmsPortNo = wmsPortNo;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

	public String getPortNo() {
		return portNo;
	}

	public void setPortNo(String portNo) {
		this.portNo = portNo;
	}

	public int getFinished() {
		return finished;
	}

	public void setFinished(int finished) {
		this.finished = finished;
	}

	public InboundTaskPortDto(String wmsPortNo, Integer taskId, Integer taskType, String portNo, int finished) {
		super();
		this.wmsPortNo = wmsPortNo;
		this.taskId = taskId;
		this.taskType = taskType;
		this.portNo = portNo;
		this.finished = finished;
	}

	public InboundTaskPortDto() {
		super();
		// TODO Auto-generated constructor stub
	}
}
