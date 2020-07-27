package com.prolog.eis.dto.gcs;

public class GcsAlarmReqDto {
	
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
	 *故障类型：1:RGV故障 2:托盘不可用 3:其他 
	 */
	private int errorType;
	
	/**
	 * 消息内容
	 */
	private String msg;
	
	/**
	 * 创建时间
	 */
	private String createTime;

	public GcsAlarmReqDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GcsAlarmReqDto(String id, String taskId, String containerId, int errorType, String msg, String createTime) {
		super();
		this.id = id;
		this.taskId = taskId;
		this.containerId = containerId;
		this.errorType = errorType;
		this.msg = msg;
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

	public int getErrorType() {
		return errorType;
	}

	public void setErrorType(int errorType) {
		this.errorType = errorType;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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
		result = prime * result + errorType;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((msg == null) ? 0 : msg.hashCode());
		result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
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
		GcsAlarmReqDto other = (GcsAlarmReqDto) obj;
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
		if (errorType != other.errorType)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (msg == null) {
			if (other.msg != null)
				return false;
		} else if (!msg.equals(other.msg))
			return false;
		if (taskId == null) {
			if (other.taskId != null)
				return false;
		} else if (!taskId.equals(other.taskId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GcsAlarmReqDto [id=" + id + ", taskId=" + taskId + ", containerId=" + containerId + ", errorType="
				+ errorType + ", msg=" + msg + ", createTime=" + createTime + "]";
	}
}
