package com.prolog.eis.model.wms;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;


@Table("container_task_detail")
public class ContainerTaskDetail {



    @Column("ID")
    @ApiModelProperty("主键")
    private Integer id;


    @Column("container_code")
    @ApiModelProperty("托盘编号")
    private String  containerCode ;


    @Column("bill_no")
    @ApiModelProperty("出库单号")
    private String 	billNo ;


    @Column("seqno")
    @ApiModelProperty("明细行号")
    private String 	seqNo ;


    @Column("item_id")
    @ApiModelProperty("wms商品id")
    private String 	itemId ;



    @Column("lot_id")
    @ApiModelProperty("wms批号")
    private String 	lotId ;


    @Column("owner_id")
    @ApiModelProperty("wms业主")
    private String 	ownerId ;

    @Column("qty")
    @ApiModelProperty("数量重量")
    private BigDecimal qty ;

    @Column("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime ;

    @Column("end_time")
    @ApiModelProperty("创建时间")
    private Date 	endTime ;


    public Integer getId() {
        return id;
    }


    public String getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
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

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
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
