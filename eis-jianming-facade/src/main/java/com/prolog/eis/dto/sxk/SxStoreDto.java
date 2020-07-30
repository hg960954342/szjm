package com.prolog.eis.dto.sxk;

public class SxStoreDto {

private int taskCount;
	
	private int layer;
	
	private String belongArea;
	
	/**
	 * 小车数
	 */
	private int carNoCount;
	
	/**
	 * 平均值
	 */
	private double avgCount;
	
	/**
	 * 最大任务数
	 */
	private int maxCarTask;

	public SxStoreDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SxStoreDto(int taskCount, int layer, String belongArea, int carNoCount, double avgCount, int maxCarTask) {
		super();
		this.taskCount = taskCount;
		this.layer = layer;
		this.belongArea = belongArea;
		this.carNoCount = carNoCount;
		this.avgCount = avgCount;
		this.maxCarTask = maxCarTask;
	}

	public int getTaskCount() {
		return taskCount;
	}

	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public String getBelongArea() {
		return belongArea;
	}

	public void setBelongArea(String belongArea) {
		this.belongArea = belongArea;
	}

	public int getCarNoCount() {
		return carNoCount;
	}

	public void setCarNoCount(int carNoCount) {
		this.carNoCount = carNoCount;
	}

	public double getAvgCount() {
		return avgCount;
	}

	public void setAvgCount(double avgCount) {
		this.avgCount = avgCount;
	}

	public int getMaxCarTask() {
		return maxCarTask;
	}

	public void setMaxCarTask(int maxCarTask) {
		this.maxCarTask = maxCarTask;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(avgCount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((belongArea == null) ? 0 : belongArea.hashCode());
		result = prime * result + carNoCount;
		result = prime * result + layer;
		result = prime * result + maxCarTask;
		result = prime * result + taskCount;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SxStoreDto other = (SxStoreDto) obj;
		if (Double.doubleToLongBits(avgCount) != Double.doubleToLongBits(other.avgCount))
			return false;
		if (belongArea == null) {
			if (other.belongArea != null)
				return false;
		} else if (!belongArea.equals(other.belongArea))
			return false;
		if (carNoCount != other.carNoCount)
			return false;
		if (layer != other.layer)
			return false;
		if (maxCarTask != other.maxCarTask)
			return false;
		if (taskCount != other.taskCount)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SxStoreDto [taskCount=" + taskCount + ", layer=" + layer + ", belongArea=" + belongArea
				+ ", carNoCount=" + carNoCount + ", avgCount=" + avgCount + ", maxCarTask=" + maxCarTask + "]";
	}
}
