package com.prolog.eis.dto.sxk;

public class SxStoreLock {

	/**
	 * 货位升位锁
	 */
	private Integer ascentLockState;
	
	/**
	 * 货位组升位锁
	 */
	private Integer ascentGroupLockState;
	
	/**
	 * 货位组是否锁定
	 */
	private Integer isLock;
	
	/**
	 * 移库数
	 */
	private int deptNum;
	
	/**
	 * 库存Id
	 */
	private int storeId;
	
	/**
	 * 货位Id
	 */
	private int locationId;
	
	/**
	 * 库存状态(10：入库中、 20：已上架、 30：出库中、31:待出库、40：移位中)
	 */
	private int storeState;
	/**
	 * 层
	 */
	private int layer;
	
	/**
	 * x
	 */
	private  int x;
	
	/**
	 * y
	 */
	private int y;

	public Integer getAscentLockState() {
		return ascentLockState;
	}

	public void setAscentLockState(Integer ascentLockState) {
		this.ascentLockState = ascentLockState;
	}

	public Integer getAscentGroupLockState() {
		return ascentGroupLockState;
	}

	public void setAscentGroupLockState(Integer ascentGroupLockState) {
		this.ascentGroupLockState = ascentGroupLockState;
	}

	public Integer getIsLock() {
		return isLock;
	}

	public void setIsLock(Integer isLock) {
		this.isLock = isLock;
	}

	public int getDeptNum() {
		return deptNum;
	}

	public void setDeptNum(int deptNum) {
		this.deptNum = deptNum;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public int getStoreState() {
		return storeState;
	}

	public void setStoreState(int storeState) {
		this.storeState = storeState;
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
}
