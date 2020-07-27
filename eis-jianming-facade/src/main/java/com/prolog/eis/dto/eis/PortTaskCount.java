package com.prolog.eis.dto.eis;

public class PortTaskCount {

	private String portNo;
	
	private int taskCount;

	public String getPortNo() {
		return portNo;
	}

	public void setPortNo(String portNo) {
		this.portNo = portNo;
	}

	public int getTaskCount() {
		return taskCount;
	}

	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}

	public PortTaskCount(String portNo, int taskCount) {
		super();
		this.portNo = portNo;
		this.taskCount = taskCount;
	}

	public PortTaskCount() {
		super();
		// TODO Auto-generated constructor stub
	}
}
