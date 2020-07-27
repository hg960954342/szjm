package com.prolog.eis.dto.gcs;

public class GcsOrderReportReqDto {
	
	/**
	 * 32位uuid
	 */
	private String id; 
	
	/**
	 * 到位回告时间
	 */
	private String billDate;
	
	/**
	 * EIS生成的任务单号
	 */
	private String billCode;
	
	/**
	 * 执行母托盘编号
	 */
	private String containerCode;
	
	/**
	 * 系统类型：1:GCS  2:MCS
	 */
	private int sysType;
	
	/**
	 * 目的位置
	 */
	private String locCodeTo;
	
	/**
	 * 当前位置
	 */
	private String position;
	
	/**
	 * 作业状态:1:开始  2:结束
	 */
	private int workStatus;
	
	/**
	 * 小车编号
	 */
	private String rgvId;
	
	/**
	 * 任务类型：1:入库 2:出库 3:搬运 4:跨层
	 */
	private int taskType;

	public GcsOrderReportReqDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GcsOrderReportReqDto(String id, String billDate, String billCode, String containerCode, int sysType,
			String locCodeTo, String position, int workStatus, String rgvId, int taskType) {
		super();
		this.id = id;
		this.billDate = billDate;
		this.billCode = billCode;
		this.containerCode = containerCode;
		this.sysType = sysType;
		this.locCodeTo = locCodeTo;
		this.position = position;
		this.workStatus = workStatus;
		this.rgvId = rgvId;
		this.taskType = taskType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getContainerCode() {
		return containerCode;
	}

	public void setContainerCode(String containerCode) {
		this.containerCode = containerCode;
	}

	public int getSysType() {
		return sysType;
	}

	public void setSysType(int sysType) {
		this.sysType = sysType;
	}

	public String getLocCodeTo() {
		return locCodeTo;
	}

	public void setLocCodeTo(String locCodeTo) {
		this.locCodeTo = locCodeTo;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(int workStatus) {
		this.workStatus = workStatus;
	}

	public String getRgvId() {
		return rgvId;
	}

	public void setRgvId(String rgvId) {
		this.rgvId = rgvId;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((billCode == null) ? 0 : billCode.hashCode());
		result = prime * result + ((billDate == null) ? 0 : billDate.hashCode());
		result = prime * result + ((containerCode == null) ? 0 : containerCode.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((locCodeTo == null) ? 0 : locCodeTo.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + ((rgvId == null) ? 0 : rgvId.hashCode());
		result = prime * result + sysType;
		result = prime * result + taskType;
		result = prime * result + workStatus;
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
		GcsOrderReportReqDto other = (GcsOrderReportReqDto) obj;
		if (billCode == null) {
			if (other.billCode != null)
				return false;
		} else if (!billCode.equals(other.billCode))
			return false;
		if (billDate == null) {
			if (other.billDate != null)
				return false;
		} else if (!billDate.equals(other.billDate))
			return false;
		if (containerCode == null) {
			if (other.containerCode != null)
				return false;
		} else if (!containerCode.equals(other.containerCode))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (locCodeTo == null) {
			if (other.locCodeTo != null)
				return false;
		} else if (!locCodeTo.equals(other.locCodeTo))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (rgvId == null) {
			if (other.rgvId != null)
				return false;
		} else if (!rgvId.equals(other.rgvId))
			return false;
		if (sysType != other.sysType)
			return false;
		if (taskType != other.taskType)
			return false;
		if (workStatus != other.workStatus)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GcsOrderReportReqDto [id=" + id + ", billDate=" + billDate + ", billCode=" + billCode
				+ ", containerCode=" + containerCode + ", sysType=" + sysType + ", locCodeTo=" + locCodeTo
				+ ", position=" + position + ", workStatus=" + workStatus + ", rgvId=" + rgvId + ", taskType="
				+ taskType + "]";
	}
}
