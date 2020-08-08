package com.prolog.eis.dto.eis;

public class CkContainerTaskDto {

	private int x;
	
	private int y;
	
	private int layer;
	
	private String containerCode;
	
	private String target;
	
	private int targetType;
	
	private String taskCode;

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

	public String getContainerCode() {
		return containerCode;
	}

	public void setContainerCode(String containerCode) {
		this.containerCode = containerCode;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public int getTargetType() {
		return targetType;
	}

	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public CkContainerTaskDto(int x, int y, int layer, String containerCode, String target, int targetType,
			String taskCode) {
		super();
		this.x = x;
		this.y = y;
		this.layer = layer;
		this.containerCode = containerCode;
		this.target = target;
		this.targetType = targetType;
		this.taskCode = taskCode;
	}

	public CkContainerTaskDto() {
		super();
		// TODO Auto-generated constructor stub
	}
}
