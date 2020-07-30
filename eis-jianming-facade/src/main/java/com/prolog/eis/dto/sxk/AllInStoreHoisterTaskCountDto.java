package com.prolog.eis.dto.sxk;

public class AllInStoreHoisterTaskCountDto {

	private int layer;

	private int taskCount;

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getTaskCount() {
		return taskCount;
	}

	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}

	public AllInStoreHoisterTaskCountDto(int layer, int taskCount) {
		super();
		this.layer = layer;
		this.taskCount = taskCount;
	}

	public AllInStoreHoisterTaskCountDto() {
		super();
		// TODO Auto-generated constructor stub
	}
}
