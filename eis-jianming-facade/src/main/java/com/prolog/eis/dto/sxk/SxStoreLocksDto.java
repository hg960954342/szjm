package com.prolog.eis.dto.sxk;

public class SxStoreLocksDto {

	/**
	 * 货位升位锁
	 */
	private Integer ascentLockStat;
	
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
	
	/**
	 * 容器编号
	 */
	private String containerNo;

	public SxStoreLocksDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SxStoreLocksDto(Integer ascentLockStat, Integer ascentGroupLockState, Integer isLock, int deptNum,
			int storeId, int layer, int x, int y, String containerNo) {
		super();
		this.ascentLockStat = ascentLockStat;
		this.ascentGroupLockState = ascentGroupLockState;
		this.isLock = isLock;
		this.deptNum = deptNum;
		this.storeId = storeId;
		this.layer = layer;
		this.x = x;
		this.y = y;
		this.containerNo = containerNo;
	}

	public Integer getAscentLockStat() {
		return ascentLockStat;
	}

	public void setAscentLockStat(Integer ascentLockStat) {
		this.ascentLockStat = ascentLockStat;
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

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ascentGroupLockState == null) ? 0 : ascentGroupLockState.hashCode());
		result = prime * result + ((ascentLockStat == null) ? 0 : ascentLockStat.hashCode());
		result = prime * result + ((containerNo == null) ? 0 : containerNo.hashCode());
		result = prime * result + deptNum;
		result = prime * result + ((isLock == null) ? 0 : isLock.hashCode());
		result = prime * result + layer;
		result = prime * result + storeId;
		result = prime * result + x;
		result = prime * result + y;
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
		SxStoreLocksDto other = (SxStoreLocksDto) obj;
		if (ascentGroupLockState == null) {
			if (other.ascentGroupLockState != null)
				return false;
		} else if (!ascentGroupLockState.equals(other.ascentGroupLockState))
			return false;
		if (ascentLockStat == null) {
			if (other.ascentLockStat != null)
				return false;
		} else if (!ascentLockStat.equals(other.ascentLockStat))
			return false;
		if (containerNo == null) {
			if (other.containerNo != null)
				return false;
		} else if (!containerNo.equals(other.containerNo))
			return false;
		if (deptNum != other.deptNum)
			return false;
		if (isLock == null) {
			if (other.isLock != null)
				return false;
		} else if (!isLock.equals(other.isLock))
			return false;
		if (layer != other.layer)
			return false;
		if (storeId != other.storeId)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SxStoreLocksDto [ascentLockStat=" + ascentLockStat + ", ascentGroupLockState=" + ascentGroupLockState
				+ ", isLock=" + isLock + ", deptNum=" + deptNum + ", storeId=" + storeId + ", layer=" + layer + ", x="
				+ x + ", y=" + y + ", containerNo=" + containerNo + "]";
	}
}
