package com.prolog.eis.dto.sxk;

public class AllInStoreLocationLayersDto {

	private int layer;
	private int propertyCount;
	private int containerCount;
	private int ckTaskCount;
	private int rkTaskCount;
	private int hoisterTaskCount;
	public int getLayer() {
		return layer;
	}
	public void setLayer(int layer) {
		this.layer = layer;
	}
	public int getPropertyCount() {
		return propertyCount;
	}
	public void setPropertyCount(int propertyCount) {
		this.propertyCount = propertyCount;
	}
	public int getCkTaskCount() {
		return ckTaskCount;
	}
	public void setCkTaskCount(int ckTaskCount) {
		this.ckTaskCount = ckTaskCount;
	}
	public int getRkTaskCount() {
		return rkTaskCount;
	}
	public void setRkTaskCount(int rkTaskCount) {
		this.rkTaskCount = rkTaskCount;
	}
	public int getContainerCount() {
		return containerCount;
	}
	public void setContainerCount(int containerCount) {
		this.containerCount = containerCount;
	}
	public int getHoisterTaskCount() {
		return hoisterTaskCount;
	}
	public void setHoisterTaskCount(int hoisterTaskCount) {
		this.hoisterTaskCount = hoisterTaskCount;
	}
}
