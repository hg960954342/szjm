package com.prolog.eis.model.wms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@Table("inbound_task_history")
public class InBoundTaskHistory {
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

    @Column("container_code")
    @ApiModelProperty("母托盘编号")
    @JsonProperty("CONTAINERCODE")
    private String containerCode;

    @Column("task_type")
    @ApiModelProperty("任务托暂未定   空托的情况 0空托垛入库  1空托碟")
    @JsonProperty("TASKTYPE")
    private int taskType;

    @Column("item_id")
    @ApiModelProperty("wms商品id")
    @JsonProperty("ITEMID")
    private String itemId;

    @Column("qty")
    @ApiModelProperty("数量（重量）")
    @JsonProperty("QTY")
    private float qty;

    @Column("lot_id")
    @ApiModelProperty("wms内码")
    @JsonProperty("LOTID")
    private String lotId;

    @Column("lot")
    @ApiModelProperty("wms批号")
    @JsonProperty("LOT")
    private String lot;

    @Column("ceng")
    @ApiModelProperty("入库楼层")
    @JsonProperty("CENG")
    private String ceng;

    @Column("agv_loc")
    @ApiModelProperty("Agv搬运点")
    @JsonProperty("AGVLOC")
    private String agvLoc;

    @Column("owner_id")
    @ApiModelProperty("wms业主")
    @JsonProperty("CONSIGNOR")
    private String ownerId;

    @Column("task_state")
    @ApiModelProperty("0 创建 1开始 3扫码入库 4完成")
    private int taskState;

    @Column("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @Column("start_time")
    @ApiModelProperty("开始时间")
    private Date startTime;

    @Column("ruku_time")
    @ApiModelProperty("扫码入库时间")
    private Date rukuTime;

    @Column("end_time")
    @ApiModelProperty("结束时间")
    private Date endTime;

    @Column("item_name")
    @ApiModelProperty("商品名称")
    @JsonProperty("SPMCH")
    private String itemName;


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

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

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public String getCeng() {
        return ceng;
    }

    public void setCeng(String ceng) {
        this.ceng = ceng;
    }

    public String getAgvLoc() {
        return agvLoc;
    }

    public void setAgvLoc(String agvLoc) {
        this.agvLoc = agvLoc;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getRukuTime() {
        return rukuTime;
    }

    public void setRukuTime(Date rukuTime) {
        this.rukuTime = rukuTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }
}
