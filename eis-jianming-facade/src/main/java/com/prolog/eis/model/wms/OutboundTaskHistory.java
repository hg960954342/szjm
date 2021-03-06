package com.prolog.eis.model.wms;

import com.alibaba.fastjson.annotation.JSONField;
import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@Table("outbound_task_history")
public class OutboundTaskHistory {

    @Id
    @ApiModelProperty("主键")
    @AutoKey(type = AutoKey.TYPE_IDENTITY)
    private int id;

    @Column("bill_no")
    @ApiModelProperty("入库单号")
    @JSONField(name="BILLNO")
    private String billNo;

    @Column("wms_push")
    @ApiModelProperty("是否wms下发，0不是，1是")
    private int wmsPush;

    @Column("reback")
    @ApiModelProperty("是否回传，0不回传，1回传")
    private int reBack;

    @Column("task_type")
    @ApiModelProperty("任务托暂未定   空托的情况 0空托垛入库  1空托碟")
    @JSONField(name="TYPE")
    private int taskType;

    @Column("task_state")
    @ApiModelProperty("任务状态")
    private int taskState;

    @Column("sfreq")
    @ApiModelProperty("站点要求 0 无   1有")
    @JSONField(name="SFREQUIREMENT")
    private int sfReq;

    @Column("pick_code")
    @ApiModelProperty("拣选站")
    @JSONField(name="PICKCODE")
    private String pickCode;

    @Column("owner_id")
    @ApiModelProperty("wms业主")
    @JSONField(name="CONSIGNOR")
    private String ownerId;

    @Column("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @Column("end_time")
    @ApiModelProperty("结束时间")
    private Date endTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public int getWmsPush() {
        return wmsPush;
    }

    public void setWmsPush(int wmsPush) {
        this.wmsPush = wmsPush;
    }

    public int getReBack() {
        return reBack;
    }

    public void setReBack(int reBack) {
        this.reBack = reBack;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public int getSfReq() {
        return sfReq;
    }

    public void setSfReq(int sfReq) {
        this.sfReq = sfReq;
    }

    public String getPickCode() {
        return pickCode;
    }

    public void setPickCode(String pickCode) {
        this.pickCode = pickCode;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
