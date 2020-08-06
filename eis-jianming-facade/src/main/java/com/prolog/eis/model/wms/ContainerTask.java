package com.prolog.eis.model.wms;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@Table("container_task")
public class ContainerTask {
    @Id
    @ApiModelProperty("主键")
    @AutoKey(type = AutoKey.TYPE_IDENTITY)
    private int id;

    @Column("container_code")
    @ApiModelProperty("托盘号")
    private String containerCode;

    @Column("task_type")
    @ApiModelProperty("任务类型")
    private int taskType;

    @Column("source")
    @ApiModelProperty("当前位置")
    private String source;

    @Column("source_type")
    @ApiModelProperty("当前托盘区域 1托盘库内 2agv区域")
    private int sourceType;

    @Column("target")
    @ApiModelProperty("目的地")
    private String target;

    @Column("target_type")
    @ApiModelProperty("目的地区域 1托盘库内 2agv区域")
    private int targetType;

    @Column("task_state")
    @ApiModelProperty("任务状态 1 到位 2已发送给下游设备 3下游设备回告开始 4离开原存储位 后面可以扩充")
    private int taskState;

    @Column("task_code")
    @ApiModelProperty("任务号")
    private String taskCode;

    @Column("item_id")
    @ApiModelProperty("wms商品id")
    private String itemId;

    @Column("lot_id")
    @ApiModelProperty("wms批号")
    private String lotId;

    @Column("owner_id")
    @ApiModelProperty("wms业主")
    private String ownerId;

    @Column("qty")
    @ApiModelProperty("数量（重量）")
    private double qty;

    @Column("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @Column("send_time")
    @ApiModelProperty("发送给设备时间")
    private Date sendTime;

    @Column("start_time")
    @ApiModelProperty("设备开始时间")
    private Date startTime;

    @Column("move_time")
    @ApiModelProperty("离开原存储位时间")
    private Date moveTime;

    @Column("end_time")
    @ApiModelProperty("结束时间")
    private Date endTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
    
	public String getContainerCode() {
		return containerCode;
	}

	public void setContainerCode(String containerCode) {
		this.containerCode = containerCode;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getSourceType() {
		return sourceType;
	}

	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public int getTargetType() {
		return targetType;
	}

	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}

	public int getTaskState() {
		return taskState;
	}

	public void setTaskState(int taskState) {
		this.taskState = taskState;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getLotId() {
		return lotId;
	}

	public void setLotId(String lotId) {
		this.lotId = lotId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getMoveTime() {
		return moveTime;
	}

	public void setMoveTime(Date moveTime) {
		this.moveTime = moveTime;
	}

	public Date getEndTime() {
		return endTime;
	}

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "ContainerTask{" +
                "id=" + id +
                ", containerCode=" + containerCode +
                ", taskType=" + taskType +
                ", source='" + source + '\'' +
                ", sourceType='" + sourceType + '\'' +
                ", target='" + target + '\'' +
                ", targetType='" + targetType + '\'' +
                ", taskState='" + taskState + '\'' +
                ", taskCode='" + taskCode + '\'' +
                ", itemId='" + itemId + '\'' +
                ", lotId='" + lotId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", qty=" + qty +
                ", createTime=" + createTime +
                ", sendTime=" + sendTime +
                ", startTime=" + startTime +
                ", moveTime=" + moveTime +
                ", endTime=" + endTime +
                '}';
    }
}
