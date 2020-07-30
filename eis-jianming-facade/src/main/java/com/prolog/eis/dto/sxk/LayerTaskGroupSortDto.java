package com.prolog.eis.dto.sxk;

public class LayerTaskGroupSortDto {

	/**
	 * 层
	 */
	private int layer;
	
	/**
	 * 层任务数
	 */
	private int layerTaskCount;
	
	/**
	 * 小车数
	 */
	private int carCount;
	
	/**
	 * 小车平均任务数
	 */
	private double avgTaskCount;

	public LayerTaskGroupSortDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LayerTaskGroupSortDto(int layer, int layerTaskCount, int carCount, double avgTaskCount) {
		super();
		this.layer = layer;
		this.layerTaskCount = layerTaskCount;
		this.carCount = carCount;
		this.avgTaskCount = avgTaskCount;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getLayerTaskCount() {
		return layerTaskCount;
	}

	public void setLayerTaskCount(int layerTaskCount) {
		this.layerTaskCount = layerTaskCount;
	}

	public int getCarCount() {
		return carCount;
	}

	public void setCarCount(int carCount) {
		this.carCount = carCount;
	}

	public double getAvgTaskCount() {
		return avgTaskCount;
	}

	public void setAvgTaskCount(double avgTaskCount) {
		this.avgTaskCount = avgTaskCount;
	}
}
