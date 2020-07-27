package com.prolog.eis.dto.eis.mcs;

public class InBoundRequestResponse {

	private String taskId;
	
	private String target;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public InBoundRequestResponse(String taskId, String target) {
		super();
		this.taskId = taskId;
		this.target = target;
	}

	public InBoundRequestResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "InBoundRequestResponse [taskId=" + taskId + ", target=" + target + "]";
	}
}
