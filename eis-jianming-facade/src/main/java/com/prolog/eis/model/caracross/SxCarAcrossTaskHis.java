package com.prolog.eis.model.caracross;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 小车跨层GCS/MCS任务历史表(SxCarAcrossTask)实体类
 *
 * @author panteng
 * @since 2020-04-13 14:44:52
 */
@Table("sx_car_across_task_his")
public class SxCarAcrossTaskHis implements Serializable {
    private static final long serialVersionUID = -16073936008177236L;
    @Id
    @Column("id")
    @ApiModelProperty("主键")
    private String id;
    
    @Column("across_id")
    @ApiModelProperty("跨层任务id")
    private Integer acrossId;
    
    @Column("task_id")
    @ApiModelProperty("任务id")
    private String taskId;
    
    @Column("floor")
    @ApiModelProperty("层")
    private Integer floor;
    
    @Column("task_type")
    @ApiModelProperty("GCS(1,进入提升机接驳口，2进入提升机，3出提升机） MCS(1,提升机到远程 2 提升机到目标层,3.提升机解锁）")
    private Integer taskType;
    
    @Column("systype")
    @ApiModelProperty("MCS/GCS")
    private String systype;
    
    @Column("hoistId")
    @ApiModelProperty("提升机编号")
    private String hoistid;
    
    @Column("send_err_msg")
    @ApiModelProperty("发送返回错误消息")
    private String sendErrMsg;
    
    @Column("send_status")
    @ApiModelProperty("发送状态（ 1成功 2失败）")
    private Integer sendStatus;

    @Column("task_status")
    @ApiModelProperty("任务状态（ 1已发送 2开始 3完成）")
    private Integer taskStatus;
    
    @Column("send_count")
    @ApiModelProperty("发送次数")
    private Integer sendCount;
    
    @Column("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @Column("start_time")
    @ApiModelProperty("任务开始时间")
    private Date startTime;

    @Column("finish_time")
    @ApiModelProperty("任务完成时间")
    private Date finishTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAcrossId() {
        return acrossId;
    }

    public void setAcrossId(Integer acrossId) {
        this.acrossId = acrossId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public String getSystype() {
        return systype;
    }

    public void setSystype(String systype) {
        this.systype = systype;
    }

    public String getHoistid() {
        return hoistid;
    }

    public void setHoistid(String hoistid) {
        this.hoistid = hoistid;
    }

    public String getSendErrMsg() {
        return sendErrMsg;
    }

    public void setSendErrMsg(String sendErrMsg) {
        this.sendErrMsg = sendErrMsg;
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

}