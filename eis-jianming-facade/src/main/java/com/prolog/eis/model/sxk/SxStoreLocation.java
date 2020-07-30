package com.prolog.eis.model.sxk;

import java.util.Date;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Ignore;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("SX_STORE_LOCATION")
public class SxStoreLocation {

	@Id
    @ApiModelProperty("货位ID")
    private int id;        //货位ID

    @Column("store_no")
    @ApiModelProperty("货位编号")
    private String storeNo;

    @Column("STORE_LOCATION_GROUP_ID")
    @ApiModelProperty("货位组ID")
    private int storeLocationGroupId;        //货位组ID

    @Column("LAYER")
    @ApiModelProperty("层")
    private int layer;        //层

    @Column("X")
    @ApiModelProperty("X")
    private int x;        //X

    @Column("Y")
    @ApiModelProperty("Y")
    private int y;        //Y

    @Column("STORE_LOCATION_ID1")
    @ApiModelProperty("相邻货位ID1")
    private Integer storeLocationId1;        //相邻货位ID1

    @Column("STORE_LOCATION_ID2")
    @ApiModelProperty("相邻货位ID2")
    private Integer storeLocationId2;        //相邻货位ID2


    @Column("ASCENT_LOCK_STATE")
    @ApiModelProperty("货位组升位锁")
    private int ascentLockState;        //货位组升位锁

    @Column("LOCATION_INDEX")
    @ApiModelProperty("货位组位置索引(从上到下、从左到右)")
    private int locationIndex;        //货位组位置索引(从上到下、从左到右)

    @Column("dept_num")
    @ApiModelProperty("移库数")
    private int deptNum;

    @Column("depth")
    @ApiModelProperty("深度")
    private int depth;

    @Column("CREATE_TIME")
    @ApiModelProperty("创建时间")
    private Date createTime;        //创建时间
    
    @Column("vertical_location_group_id")
    @ApiModelProperty("垂直货位Id")
    private Integer verticalLocationGroupId;
    
    @Column("actual_weight")
    @ApiModelProperty("实际重量")
    private Double actualWeight;		//实际重量

    @Column("limit_weight")
    @ApiModelProperty("限重")
    private Double limitWeight;		//限重
    
    @Column("is_inBound_location")
    @ApiModelProperty("是否为入库货位")
    private int isInBoundLocation;		//是否为入库货位(0.否、1、是)
    
    @Column("wms_store_no")
    @ApiModelProperty("Wms货位编号")
    private String wmsStoreNo;

    @Column("task_lock")
    @ApiModelProperty("任务锁")
    private int taskLock;

	@Ignore
	@ApiModelProperty("方向编号 02 03")
	private String directionCoding;
    
    public SxStoreLocation() {
        super();
        // TODO Auto-generated constructor stub
    }

	public String getDirectionCoding() {
		return directionCoding;
	}

	public void setDirectionCoding(String directionCoding) {
		this.directionCoding = directionCoding;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public int getStoreLocationGroupId() {
		return storeLocationGroupId;
	}

	public void setStoreLocationGroupId(int storeLocationGroupId) {
		this.storeLocationGroupId = storeLocationGroupId;
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

	public int getAscentLockState() {
		return ascentLockState;
	}

	public void setAscentLockState(int ascentLockState) {
		this.ascentLockState = ascentLockState;
	}

	public int getLocationIndex() {
		return locationIndex;
	}

	public void setLocationIndex(int locationIndex) {
		this.locationIndex = locationIndex;
	}

	public int getDeptNum() {
		return deptNum;
	}

	public void setDeptNum(int deptNum) {
		this.deptNum = deptNum;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getVerticalLocationGroupId() {
		return verticalLocationGroupId;
	}

	public void setVerticalLocationGroupId(Integer verticalLocationGroupId) {
		this.verticalLocationGroupId = verticalLocationGroupId;
	}

	public Double getActualWeight() {
		return actualWeight;
	}

	public void setActualWeight(Double actualWeight) {
		this.actualWeight = actualWeight;
	}

	public Double getLimitWeight() {
		return limitWeight;
	}

	public void setLimitWeight(Double limitWeight) {
		this.limitWeight = limitWeight;
	}

	public int getIsInBoundLocation() {
		return isInBoundLocation;
	}

	public void setIsInBoundLocation(int isInBoundLocation) {
		this.isInBoundLocation = isInBoundLocation;
	}

	public String getWmsStoreNo() {
		return wmsStoreNo;
	}

	public void setWmsStoreNo(String wmsStoreNo) {
		this.wmsStoreNo = wmsStoreNo;
	}

	public int getTaskLock() {
		return taskLock;
	}

	public void setTaskLock(int taskLock) {
		this.taskLock = taskLock;
	}


	@Override
	public String toString() {
		return "SxStoreLocation [id=" + id + ", storeNo=" + storeNo + ", storeLocationGroupId=" + storeLocationGroupId
				+ ", layer=" + layer + ", x=" + x + ", y=" + y + ", storeLocationId1=" + storeLocationId1
				+ ", storeLocationId2=" + storeLocationId2 + ", ascentLockState=" + ascentLockState + ", locationIndex="
				+ locationIndex + ", deptNum=" + deptNum + ", depth=" + depth + ", createTime=" + createTime
				+ ", verticalLocationGroupId=" + verticalLocationGroupId + ", actualWeight=" + actualWeight
				+ ", limitWeight=" + limitWeight + ", isInBoundLocation=" + isInBoundLocation + ", wmsStoreNo="
				+ wmsStoreNo + ", taskLock=" + taskLock + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actualWeight == null) ? 0 : actualWeight.hashCode());
		result = prime * result + ascentLockState;
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + deptNum;
		result = prime * result + depth;
		result = prime * result + id;
		result = prime * result + isInBoundLocation;
		result = prime * result + layer;
		result = prime * result + ((limitWeight == null) ? 0 : limitWeight.hashCode());
		result = prime * result + locationIndex;
		result = prime * result + storeLocationGroupId;
		result = prime * result + ((storeLocationId1 == null) ? 0 : storeLocationId1.hashCode());
		result = prime * result + ((storeLocationId2 == null) ? 0 : storeLocationId2.hashCode());
		result = prime * result + ((storeNo == null) ? 0 : storeNo.hashCode());
		result = prime * result + taskLock;
		result = prime * result + ((verticalLocationGroupId == null) ? 0 : verticalLocationGroupId.hashCode());
		result = prime * result + ((wmsStoreNo == null) ? 0 : wmsStoreNo.hashCode());
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
		SxStoreLocation other = (SxStoreLocation) obj;
		if (actualWeight == null) {
			if (other.actualWeight != null)
				return false;
		} else if (!actualWeight.equals(other.actualWeight))
			return false;
		if (ascentLockState != other.ascentLockState)
			return false;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (deptNum != other.deptNum)
			return false;
		if (depth != other.depth)
			return false;
		if (id != other.id)
			return false;
		if (isInBoundLocation != other.isInBoundLocation)
			return false;
		if (layer != other.layer)
			return false;
		if (limitWeight == null) {
			if (other.limitWeight != null)
				return false;
		} else if (!limitWeight.equals(other.limitWeight))
			return false;
		if (locationIndex != other.locationIndex)
			return false;
		if (storeLocationGroupId != other.storeLocationGroupId)
			return false;
		if (storeLocationId1 == null) {
			if (other.storeLocationId1 != null)
				return false;
		} else if (!storeLocationId1.equals(other.storeLocationId1))
			return false;
		if (storeLocationId2 == null) {
			if (other.storeLocationId2 != null)
				return false;
		} else if (!storeLocationId2.equals(other.storeLocationId2))
			return false;
		if (storeNo == null) {
			if (other.storeNo != null)
				return false;
		} else if (!storeNo.equals(other.storeNo))
			return false;
		if (taskLock != other.taskLock)
			return false;
		if (verticalLocationGroupId == null) {
			if (other.verticalLocationGroupId != null)
				return false;
		} else if (!verticalLocationGroupId.equals(other.verticalLocationGroupId))
			return false;
		if (wmsStoreNo == null) {
			if (other.wmsStoreNo != null)
				return false;
		} else if (!wmsStoreNo.equals(other.wmsStoreNo))
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public SxStoreLocation(int id, String storeNo, int storeLocationGroupId, int layer, int x, int y,
			Integer storeLocationId1, Integer storeLocationId2, int ascentLockState, int locationIndex, int deptNum,
			int depth, Date createTime, Integer verticalLocationGroupId, Double actualWeight, Double limitWeight,
			int isInBoundLocation, String wmsStoreNo, int taskLock) {
		super();
		this.id = id;
		this.storeNo = storeNo;
		this.storeLocationGroupId = storeLocationGroupId;
		this.layer = layer;
		this.x = x;
		this.y = y;
		this.storeLocationId1 = storeLocationId1;
		this.storeLocationId2 = storeLocationId2;
		this.ascentLockState = ascentLockState;
		this.locationIndex = locationIndex;
		this.deptNum = deptNum;
		this.depth = depth;
		this.createTime = createTime;
		this.verticalLocationGroupId = verticalLocationGroupId;
		this.actualWeight = actualWeight;
		this.limitWeight = limitWeight;
		this.isInBoundLocation = isInBoundLocation;
		this.wmsStoreNo = wmsStoreNo;
		this.taskLock = taskLock;
	}
}
