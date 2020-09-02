package com.prolog.eis.model.wms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

@Table("outbound_task")
public class OutboundTask {

    @Id
    @ApiModelProperty("主键")
    @AutoKey(type = AutoKey.TYPE_IDENTITY)
    private int id;

    @Column("bill_no")
    @ApiModelProperty("入库单号")
    @JsonProperty("BILLNO")
    private String billNo;

    @Column("wms_push")
    @ApiModelProperty("是否wms下发，0不是，1是")
    private int wmsPush;

    @Column("reback")
    @ApiModelProperty("是否回传，0不回传，1回传")
    private int reBack;

    @Column("empty_container")
    @ApiModelProperty("0任务托  1空托")
    private int emptyContainer;

    @Column("task_type")
    @ApiModelProperty("任务托暂未定   空托的情况 0空托垛入库  1空托碟")
    @JsonProperty("TYPE")
    private int taskType;

    @Column("task_state")
    @ApiModelProperty("任务状态")
    private int taskState;

    @Column("sfreq")
    @ApiModelProperty("站点要求 0 无   1有")
    @JsonProperty("SFREQUIREMENT")
    private int sfReq;

    private float qty;

    @Column("pick_code")
    @ApiModelProperty("拣选站")
    @JsonProperty("PICKCODE")
    private String pickCode;

    @Column("owner_id")
    @ApiModelProperty("wms业主")
    @JsonProperty("CONSIGNOR")
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

    public int getEmptyContainer() {
        return emptyContainer;
    }

    public void setEmptyContainer(int emptyContainer) {
        this.emptyContainer = emptyContainer;
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

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
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
