package com.prolog.eis.dto.eis;

public class PortTemsInfoDto {

	private int id;
	
	private String wmsStationNo;
		
	private int portInfoId;
	
	private int portType;
	
	private int taskType;
	
	private String junctionPort;
	
	private int layer;

	private int x;
	
	private int y;
	
	private int portLock;
	
	private int taskLock;
	
	private String remarks;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWmsStationNo() {
		return wmsStationNo;
	}

	public void setWmsStationNo(String wmsStationNo) {
		this.wmsStationNo = wmsStationNo;
	}

	public int getPortInfoId() {
		return portInfoId;
	}

	public void setPortInfoId(int portInfoId) {
		this.portInfoId = portInfoId;
	}

	public int getPortType() {
		return portType;
	}

	public void setPortType(int portType) {
		this.portType = portType;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public String getJunctionPort() {
		return junctionPort;
	}

	public void setJunctionPort(String junctionPort) {
		this.junctionPort = junctionPort;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
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

	public int getPortLock() {
		return portLock;
	}

	public void setPortLock(int portLock) {
		this.portLock = portLock;
	}

	public int getTaskLock() {
		return taskLock;
	}

	public void setTaskLock(int taskLock) {
		this.taskLock = taskLock;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public PortTemsInfoDto(int id, String wmsStationNo, int portInfoId, int portType, int taskType, String junctionPort,
			int layer, int x, int y, int portLock, int taskLock, String remarks) {
		super();
		this.id = id;
		this.wmsStationNo = wmsStationNo;
		this.portInfoId = portInfoId;
		this.portType = portType;
		this.taskType = taskType;
		this.junctionPort = junctionPort;
		this.layer = layer;
		this.x = x;
		this.y = y;
		this.portLock = portLock;
		this.taskLock = taskLock;
		this.remarks = remarks;
	}

	public PortTemsInfoDto() {
		super();
		// TODO Auto-generated constructor stub
	}
}
