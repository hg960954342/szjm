package com.prolog.eis.dto.base;

import java.util.Date;

/**
 * 查询盘点计划请求Dto
 * @author Dss
 * @date 2019年1月30日 上午9:07:51
 */
public class QueryStockPlanRespDto {

	/**
	 * Id
	 */
	private int id;
	
	/**
	 * 业主ID
	 */
	private int ownerId;
	
	/**
	 * 业主名称
	 */
	private String ownerName;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 开始时间
	 */
	private Date startTime;
	
	/**
	 * 结束时间
	 */
	private Date endTime;
	
	/**
	 * 盘点状态 1:创建中 2:待盘点,3:盘点中,4:已完成
	 */
	private int checkState;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * wms盘点单号
	 */
	private String wmsPdNo;

	public QueryStockPlanRespDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QueryStockPlanRespDto(int id, int ownerId, String ownerName, String remark, Date startTime, Date endTime,
			int checkState, Date createTime, String wmsPdNo) {
		super();
		this.id = id;
		this.ownerId = ownerId;
		this.ownerName = ownerName;
		this.remark = remark;
		this.startTime = startTime;
		this.endTime = endTime;
		this.checkState = checkState;
		this.createTime = createTime;
		this.wmsPdNo = wmsPdNo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getCheckState() {
		return checkState;
	}

	public void setCheckState(int checkState) {
		this.checkState = checkState;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getWmsPdNo() {
		return wmsPdNo;
	}

	public void setWmsPdNo(String wmsPdNo) {
		this.wmsPdNo = wmsPdNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + checkState;
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + id;
		result = prime * result + ownerId;
		result = prime * result + ((ownerName == null) ? 0 : ownerName.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((wmsPdNo == null) ? 0 : wmsPdNo.hashCode());
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
		QueryStockPlanRespDto other = (QueryStockPlanRespDto) obj;
		if (checkState != other.checkState)
			return false;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (id != other.id)
			return false;
		if (ownerId != other.ownerId)
			return false;
		if (ownerName == null) {
			if (other.ownerName != null)
				return false;
		} else if (!ownerName.equals(other.ownerName))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (wmsPdNo == null) {
			if (other.wmsPdNo != null)
				return false;
		} else if (!wmsPdNo.equals(other.wmsPdNo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QueryStockPlanRespDto [id=" + id + ", ownerId=" + ownerId + ", ownerName=" + ownerName + ", remark="
				+ remark + ", startTime=" + startTime + ", endTime=" + endTime + ", checkState=" + checkState
				+ ", createTime=" + createTime + ", wmsPdNo=" + wmsPdNo + "]";
	}

}
