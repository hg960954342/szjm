package com.prolog.eis.model.eis;


import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Date;


@Table("inbound_task")
public class InboundTask {

    @Column("ID")
    @ApiModelProperty("主键")
    private Integer id;


    @Column("bill_no")
    @ApiModelProperty("入库单号")
    private String 	billNo ;



    @Column("wms_push")
    @ApiModelProperty("是否wms下发，0不是，1是")
    private Integer 	wmsPush ;


    @Column("reback")
    @ApiModelProperty("是否回传，0不回传，1回传")
    private Integer 	reBack ;


    @Column("empty_container")
    @ApiModelProperty("0任务托  1空托")
    private Integer  emptyContainer ;

    @Column("task_type")
    @ApiModelProperty("任务托暂未定   空托的情况 0空托垛入库  1空托碟")
    private Integer  taskType ;



    @Column("container_code")
    @ApiModelProperty("母托盘编号")
    private String  containeCode ;




    @Column("ceng")
    @ApiModelProperty("入库楼层")
    private String  ceng ;

    @Column("agv_loc")
    @ApiModelProperty("Agv搬运点")
    private String  agvLoc ;



    @Column("item_id")
    @ApiModelProperty("wms商品id")
    private String  itemId ;





    @Column("lot_id")
    @ApiModelProperty("wms批号")
    private String  lotId ;


    @Column("ownerid")
    @ApiModelProperty("wms业主")
    private String  ownerId ;




    @Column("qty")
    @ApiModelProperty("数量（重量）")
    private Double qty ;



    @Column("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime ;

    @Column("end_time")
    @ApiModelProperty("结束时间")
    private Date endTime ;


    public Integer getId() {
        return id;
    }



    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getWmsPush() {
        return wmsPush;
    }

    public void setWmsPush(Integer wmsPush) {
        this.wmsPush = wmsPush;
    }

    public Integer getReBack() {
        return reBack;
    }

    public void setReBack(Integer reBack) {
        this.reBack = reBack;
    }

    public Integer getEmptyContainer() {
        return emptyContainer;
    }

    public void setEmptyContainer(Integer emptyContainer) {
        this.emptyContainer = emptyContainer;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public String getContaineCode() {
        return containeCode;
    }

    public void setContaineCode(String containeCode) {
        this.containeCode = containeCode;
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

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
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
