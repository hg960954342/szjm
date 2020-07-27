package com.prolog.eis.dto.eis;

public class TempPortZtTaskDto {

	private String containerCode;
	
	private int temJjunctionPortId;
	
	private String temJunctionPort;
	
	private int layer;
	
	private int x;
	
	private int y;
	
	private int junctionPortId;
	
	private String junctionPort;
	
	private String wmsPortNo;
	
	private int tagetLayer;
	
	private int targetX;
	
	private int targetY;

	public String getContainerCode() {
		return containerCode;
	}

	public void setContainerCode(String containerCode) {
		this.containerCode = containerCode;
	}

	public int getTemJjunctionPortId() {
		return temJjunctionPortId;
	}

	public void setTemJjunctionPortId(int temJjunctionPortId) {
		this.temJjunctionPortId = temJjunctionPortId;
	}

	public String getTemJunctionPort() {
		return temJunctionPort;
	}

	public void setTemJunctionPort(String temJunctionPort) {
		this.temJunctionPort = temJunctionPort;
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

	public int getJunctionPortId() {
		return junctionPortId;
	}

	public void setJunctionPortId(int junctionPortId) {
		this.junctionPortId = junctionPortId;
	}

	public String getJunctionPort() {
		return junctionPort;
	}

	public void setJunctionPort(String junctionPort) {
		this.junctionPort = junctionPort;
	}

	public String getWmsPortNo() {
		return wmsPortNo;
	}

	public void setWmsPortNo(String wmsPortNo) {
		this.wmsPortNo = wmsPortNo;
	}

	public int getTagetLayer() {
		return tagetLayer;
	}

	public void setTagetLayer(int tagetLayer) {
		this.tagetLayer = tagetLayer;
	}

	public int getTargetX() {
		return targetX;
	}

	public void setTargetX(int targetX) {
		this.targetX = targetX;
	}

	public int getTargetY() {
		return targetY;
	}

	public void setTargetY(int targetY) {
		this.targetY = targetY;
	}

	public TempPortZtTaskDto(String containerCode, int temJjunctionPortId, String temJunctionPort, int layer, int x,
			int y, int junctionPortId, String junctionPort, String wmsPortNo, int tagetLayer, int targetX,
			int targetY) {
		super();
		this.containerCode = containerCode;
		this.temJjunctionPortId = temJjunctionPortId;
		this.temJunctionPort = temJunctionPort;
		this.layer = layer;
		this.x = x;
		this.y = y;
		this.junctionPortId = junctionPortId;
		this.junctionPort = junctionPort;
		this.wmsPortNo = wmsPortNo;
		this.tagetLayer = tagetLayer;
		this.targetX = targetX;
		this.targetY = targetY;
	}

	public TempPortZtTaskDto() {
		super();
		// TODO Auto-generated constructor stub
	}
}
