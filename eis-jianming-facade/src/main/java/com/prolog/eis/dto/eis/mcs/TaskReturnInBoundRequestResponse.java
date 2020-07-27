package com.prolog.eis.dto.eis.mcs;

public class TaskReturnInBoundRequestResponse {

	private String taskId;
	
	private String errMsg;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public TaskReturnInBoundRequestResponse(String taskId, String errMsg) {
		super();
		this.taskId = taskId;
		this.errMsg = errMsg;
	}

	public TaskReturnInBoundRequestResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
}
