package com.prolog.eis.model.wms;

import com.alibaba.fastjson.annotation.JSONField;
import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

@Table("outbound_task_detail")
public class OutboundTaskDetailDto {

    @Id
    @ApiModelProperty("主键")
    @AutoKey(type = AutoKey.TYPE_IDENTITY)
    private int id;

    @Column("bill_no")
    @ApiModelProperty("入库单号")
    @JSONField(name="BILLNO")
    private String billNo;

    @Column("seqno")
    @ApiModelProperty("明细行号")
    @JSONField(name="SEQNO")
    private String seqNo;

    @Column("ctreq")
    @ApiModelProperty("是否指定托盘 0不指定 1指定")
    @JSONField(name="CTREQ")
    private int ctReq;

    @Column("container_code")
    @ApiModelProperty("母托盘编号")
    @JSONField(name="CONTAINERCODE")
    private String containerCode;

    @Column("owner_id")
    @ApiModelProperty("wms业主")
    @JSONField(name="CONSIGNOR")
    private String ownerId;

    @Column("item_id")
    @ApiModelProperty("wms商品id")
    @JSONField(name="ITEMID")
    private String itemId;

    @Column("item_name")
    @ApiModelProperty("商品名称")
    @JSONField(name="SPMCH")
    private String itemName;

    @Column("standard")
    @ApiModelProperty("规格")
    @JSONField(name="STANDARD")
    private BigDecimal standard;

    @Column("lot_id")
    @ApiModelProperty("wms内码")
    @JSONField(name="LOTID")
    private String lotId;

    @Column("lot")
    @ApiModelProperty("wms批号")
    @JSONField(name="LOT")
    private String lot;

    @Column("qty")
    @ApiModelProperty("数量（重量）")
    @JSONField(name="QTY")
    private BigDecimal qty;

    @Column("finish_qty")
    @ApiModelProperty("完成数量（重量）")
    private BigDecimal finishQty;

    @Column("pick_code")
    @ApiModelProperty("拣选站  指定拣选站  暂时移库出库用到")
    @JSONField(name="PICKCODE")
    private String pickCode;

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

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public int getCtReq() {
        return ctReq;
    }

    public void setCtReq(int ctReq) {
        this.ctReq = ctReq;
    }

    public String getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getFinishQty() {
        return finishQty;
    }

    public void setFinishQty(BigDecimal finishQty) {
        this.finishQty = finishQty;
    }

    public String getPickCode() {
        return pickCode;
    }

    public void setPickCode(String pickCode) {
        this.pickCode = pickCode;
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

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public BigDecimal getStandard() {
        return standard;
    }

    public void setStandard(BigDecimal standard) {
        this.standard = standard;
    }
}
