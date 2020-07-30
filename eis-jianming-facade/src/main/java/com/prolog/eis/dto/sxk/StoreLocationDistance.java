package com.prolog.eis.dto.sxk;

public class StoreLocationDistance {
	private int storeLocationId;

	private int distance;
	
	private int flag;		//标识  1:靠index为1的，2:靠index为最大的

	public int getStoreLocationId() {
		return storeLocationId;
	}

	public void setStoreLocationId(int storeLocationId) {
		this.storeLocationId = storeLocationId;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}
