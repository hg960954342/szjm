package com.prolog.eis.model.gcs;

import java.util.Date;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("gcs_task_history")
public class GcsTaskHistory {

	@Id
	@ApiModelProperty("主键uuid")
	private String id;

	@Column("stock_id")
	@ApiModelProperty("托盘编号")
	private String stockId;

	@Column("task_id")
	@ApiModelProperty("任务ID")
	private String taskId;

	@Column("layer")
	@ApiModelProperty("层")
	private int layer;

	@Column("task_type")
	@ApiModelProperty("任务类型：1:入库 2:出库 3:搬运")
	private int taskType;

	@Column("priority")
	@ApiModelProperty("优先级")
	private int priority;

	@Column("loc_id_from")
	@ApiModelProperty("源位置")
	private String locIdFrom;

	@Column("loc_id_to")
	@ApiModelProperty("目标位置")
	private String locIdTo;

	@Column("task_state")
	@ApiModelProperty("任务状态(1.完成、2.失败)")
	private int taskState;

	@Column("send_count")
	@ApiModelProperty("发送次数")
	private int sendCount;

	@Column("err_msg")
	@ApiModelProperty("错误消息")
	private String errMsg;

	@Column("create_time")
	@ApiModelProperty("创建时间")
	private Date createTime;

	public GcsTaskHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GcsTaskHistory(String id, String stockId, String taskId, int layer, int taskType, int priority, String locIdFrom,
			String locIdTo, int taskState, int sendCount, String errMsg, Date createTime) {
		super();
		this.id = id;
		this.stockId = stockId;
		this.taskId = taskId;
		this.layer = layer;
		this.taskType = taskType;
		this.priority = priority;
		this.locIdFrom = locIdFrom;
		this.locIdTo = locIdTo;
		this.taskState = taskState;
		this.sendCount = sendCount;
		this.errMsg = errMsg;
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
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

	public String getLocIdTo() {
		return locIdTo;
	}

	public void setLocIdTo(String locIdTo) {
		this.locIdTo = locIdTo;
	}

	public int getTaskState() {
		return taskState;
	}

	public void setTaskState(int taskState) {
		this.taskState = taskState;
	}

	public int getSendCount() {
		return sendCount;
	}

	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((errMsg == null) ? 0 : errMsg.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + layer;
		result = prime * result + ((locIdFrom == null) ? 0 : locIdFrom.hashCode());
		result = prime * result + ((locIdTo == null) ? 0 : locIdTo.hashCode());
		result = prime * result + priority;
		result = prime * result + sendCount;
		result = prime * result + ((stockId == null) ? 0 : stockId.hashCode());
		result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
		result = prime * result + taskState;
		result = prime * result + taskType;
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
		GcsTaskHistory other = (GcsTaskHistory) obj;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (errMsg == null) {
			if (other.errMsg != null)
				return false;
		} else if (!errMsg.equals(other.errMsg))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (layer != other.layer)
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
		if (sendCount != other.sendCount)
			return false;
		if (stockId == null) {
			if (other.stockId != null)
				return false;
		} else if (!stockId.equals(other.stockId))
			return false;
		if (taskId == null) {
			if (other.taskId != null)
				return false;
		} else if (!taskId.equals(other.taskId))
			return false;
		if (taskState != other.taskState)
			return false;
		if (taskType != other.taskType)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GcsTask [id=" + id + ", stockId=" + stockId + ", taskId=" + taskId + ", layer=" + layer + ", taskType="
				+ taskType + ", priority=" + priority + ", locIdFrom=" + locIdFrom + ", locIdTo=" + locIdTo
				+ ", taskState=" + taskState + ", sendCount=" + sendCount + ", errMsg=" + errMsg + ", createTime="
				+ createTime + "]";
	}
}
