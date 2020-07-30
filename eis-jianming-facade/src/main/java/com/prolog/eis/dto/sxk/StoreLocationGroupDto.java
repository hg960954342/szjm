package com.prolog.eis.dto.sxk;

import java.util.List;

import com.prolog.eis.model.store.SxStoreLocation;

public class StoreLocationGroupDto {

	private int id;
	private String groupNo;		//货位组编号
	private int locationNum;	//货位数量
	private int layer;		//层
	private int entrance;		//出入口类型
	private int inOutNum;		//出入口数量
	private int x;		//x
	private int y;		//y
	private int reservedLocation; //预留货位
	private int belongArea; //所属区域
	private int isLock;

	private List<SxStoreLocation> storeLocations;	//货位集合

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public int getLocationNum() {
		return locationNum;
	}

	public void setLocationNum(int locationNum) {
		this.locationNum = locationNum;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getEntrance() {
		return entrance;
	}

	public void setEntrance(int entrance) {
		this.entrance = entrance;
	}

	public int getInOutNum() {
		return inOutNum;
	}

	public void setInOutNum(int inOutNum) {
		this.inOutNum = inOutNum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getReservedLocation() {
		return reservedLocation;
	}

	public void setReservedLocation(int reservedLocation) {
		this.reservedLocation = reservedLocation;
	}

	public int getBelongArea() {
		return belongArea;
	}

	public void setBelongArea(int belongArea) {
		this.belongArea = belongArea;
	}

	public List<SxStoreLocation> getStoreLocations() {
		return storeLocations;
	}

	public void setStoreLocations(List<SxStoreLocation> storeLocations) {
		this.storeLocations = storeLocations;
	}

	public int getIsLock() {
		return isLock;
	}

	public void setIsLock(int isLock) {
		this.isLock = isLock;
	}
}
