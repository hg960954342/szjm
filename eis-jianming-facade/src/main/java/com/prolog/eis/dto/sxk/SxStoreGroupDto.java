package com.prolog.eis.dto.sxk;

public class SxStoreGroupDto {

	/**
	 * 货位组升位锁
	 */
	private int ascentLockState;
	
	/**
	 * 移库数
	 */
	private int deptNum;

	/**
	 * 层
	 */
	private int layer;
	
	/**
	 * 货位组位置索引(从上到下、从左到右)
	 */
	private int locationIndex;		
	
	/**
	 * 货位组ID
	 */
	private int storeLocationGroupId;
	
	/**
	 * 相邻货位ID1
	 */
	private Integer storeLocationId1;		
	
	/**
	 * 相邻货位ID2
	 */
	private Integer storeLocationId2;

	/**
	 * X
	 */
	private int x;		
	
	/**
	 * Y
	 */
	private int y;		
	
	/**
	 * 容器编号
	 */
	private String containerNo;
	
	/**
	 * 任务属性1（路由）
	 */
	private String taskProperty1;
	
	/**
	 * 任务属性2（时效）
	 */
	private String taskProperty2;
	
	/**
	 * 库存状态
	 */
	private int storeState;
	
	private int sotreId;
	
	public SxStoreGroupDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SxStoreGroupDto(int ascentLockState, int deptNum, int layer, int locationIndex, int storeLocationGroupId,
			Integer storeLocationId1, Integer storeLocationId2, int x, int y, String containerNo, String taskProperty1,
			String taskProperty2, int storeState, int sotreId) {
		super();
		this.ascentLockState = ascentLockState;
		this.deptNum = deptNum;
		this.layer = layer;
		this.locationIndex = locationIndex;
		this.storeLocationGroupId = storeLocationGroupId;
		this.storeLocationId1 = storeLocationId1;
		this.storeLocationId2 = storeLocationId2;
		this.x = x;
		this.y = y;
		this.containerNo = containerNo;
		this.taskProperty1 = taskProperty1;
		this.taskProperty2 = taskProperty2;
		this.storeState = storeState;
		this.sotreId = sotreId;
	}

	public int getAscentLockState() {
		return ascentLockState;
	}

	public void setAscentLockState(int ascentLockState) {
		this.ascentLockState = ascentLockState;
	}

	public int getDeptNum() {
		return deptNum;
	}

	public void setDeptNum(int deptNum) {
		this.deptNum = deptNum;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getLocationIndex() {
		return locationIndex;
	}

	public void setLocationIndex(int locationIndex) {
		this.locationIndex = locationIndex;
	}

	public int getStoreLocationGroupId() {
		return storeLocationGroupId;
	}

	public void setStoreLocationGroupId(int storeLocationGroupId) {
		this.storeLocationGroupId = storeLocationGroupId;
	}

	public Integer getStoreLocationId1() {
		return storeLocationId1;
	}

	public void setStoreLocationId1(Integer storeLocationId1) {
		this.storeLocationId1 = storeLocationId1;
	}

	public Integer getStoreLocationId2() {
		return storeLocationId2;
	}

	public void setStoreLocationId2(Integer storeLocationId2) {
		this.storeLocationId2 = storeLocationId2;
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

	public String getTaskProperty1() {
		return taskProperty1;
	}

	public void setTaskProperty1(String taskProperty1) {
		this.taskProperty1 = taskProperty1;
	}

	public String getTaskProperty2() {
		return taskProperty2;
	}

	public void setTaskProperty2(String taskProperty2) {
		this.taskProperty2 = taskProperty2;
	}

	public int getStoreState() {
		return storeState;
	}

	public void setStoreState(int storeState) {
		this.storeState = storeState;
	}

	public int getSotreId() {
		return sotreId;
	}

	public void setSotreId(int sotreId) {
		this.sotreId = sotreId;
	}

	@Override
	public String toString() {
		return "SxStoreGroupDto [ascentLockState=" + ascentLockState + ", deptNum=" + deptNum + ", layer=" + layer
				+ ", locationIndex=" + locationIndex + ", storeLocationGroupId=" + storeLocationGroupId
				+ ", storeLocationId1=" + storeLocationId1 + ", storeLocationId2=" + storeLocationId2 + ", x=" + x
				+ ", y=" + y + ", containerNo=" + containerNo + ", taskProperty1=" + taskProperty1 + ", taskProperty2="
				+ taskProperty2 + ", storeState=" + storeState + ", sotreId=" + sotreId + "]";
	}
}
