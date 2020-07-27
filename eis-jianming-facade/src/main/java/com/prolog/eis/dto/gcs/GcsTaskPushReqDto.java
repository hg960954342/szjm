package com.prolog.eis.dto.gcs;

public class GcsTaskPushReqDto {

	/**
	 * 32位uuid
	 */
	private String id;

	/**
	 * 任务号
	 */
	private String taskId;
	
	/**
	 * 容器编号
	 */
	private String containerId;
	
	/**
	 * 楼层
	 */
	private int floor;
	
	/**
	 * 任务类型：1:入库 2:出库 3:搬运
	 */
	private int taskType;
	
	/**
	 * 任务优先级,0-99,0优先级最大
	 */
	private int priority;
	
	/**
	 * 源位置
	 */
	private String locIdFrom;
	
	/**
	 * 源区域
	 */
	private String fromArea;
	
	/**
	 * 目标位置
	 */
	private String locIdTo;
	
	/**
	 * 目标区域
	 */
	private String toArea;
	
	/**
	 * 创建时间
	 */
	private String createTime;

	public GcsTaskPushReqDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GcsTaskPushReqDto(String id, String taskId, String containerId, int floor, int taskType, int priority,
			String locIdFrom, String fromArea, String locIdTo, String toArea, String createTime) {
		super();
		this.id = id;
		this.taskId = taskId;
		this.containerId = containerId;
		this.floor = floor;
		this.taskType = taskType;
		this.priority = priority;
		this.locIdFrom = locIdFrom;
		this.fromArea = fromArea;
		this.locIdTo = locIdTo;
		this.toArea = toArea;
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getContainerId() {
		return containerId;
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getLocIdFrom() {
		return locIdFrom;
	}

	public void setLocIdFrom(String locIdFrom) {
		this.locIdFrom = locIdFrom;
	}

	public String getFromArea() {
		return fromArea;
	}

	public void setFromArea(String fromArea) {
		this.fromArea = fromArea;
	}

	public String getLocIdTo() {
		return locIdTo;
	}

	public void setLocIdTo(String locIdTo) {
		this.locIdTo = locIdTo;
	}

	public String getToArea() {
		return toArea;
	}

	public void setToArea(String toArea) {
		this.toArea = toArea;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((containerId == null) ? 0 : containerId.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + floor;
		result = prime * result + ((fromArea == null) ? 0 : fromArea.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((locIdFrom == null) ? 0 : locIdFrom.hashCode());
		result = prime * result + ((locIdTo == null) ? 0 : locIdTo.hashCode());
		result = prime * result + priority;
		result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
		result = prime * result + taskType;
		result = prime * result + ((toArea == null) ? 0 : toArea.hashCode());
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
		GcsTaskPushReqDto other = (GcsTaskPushReqDto) obj;
		if (containerId == null) {
			if (other.containerId != null)
				return false;
		} else if (!containerId.equals(other.containerId))
			return false;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (floor != other.floor)
			return false;
		if (fromArea == null) {
			if (other.fromArea != null)
				return false;
		} else if (!fromArea.equals(other.fromArea))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (locIdFrom == null) {
			if (other.locIdFrom != null)
				return false;
		} else if (!locIdFrom.equals(other.locIdFrom))
			return false;
		if (locIdTo == null) {
			if (other.locIdTo != null)
				return false;
		} else if (!locIdTo.equals(other.locIdTo))
			return false;
		if (priority != other.priority)
			return false;
		if (taskId == null) {
			if (other.taskId != null)
				return false;
		} else if (!taskId.equals(other.taskId))
			return false;
		if (taskType != other.taskType)
			return false;
		if (toArea == null) {
			if (other.toArea != null)
				return false;
		} else if (!toArea.equals(other.toArea))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GcsTaskPushReqDto [id=" + id + ", taskId=" + taskId + ", containerId=" + containerId + ", floor="
				+ floor + ", taskType=" + taskType + ", priority=" + priority + ", locIdFrom=" + locIdFrom
				+ ", fromArea=" + fromArea + ", locIdTo=" + locIdTo + ", toArea=" + toArea + ", createTime="
				+ createTime + "]";
	}
}
