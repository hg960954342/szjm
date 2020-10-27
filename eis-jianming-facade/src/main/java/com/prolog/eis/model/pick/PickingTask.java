package com.prolog.eis.model.pick;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author chengxudong
 * @description
 **/
@Table("picking_task")
public class PickingTask {

    @Id
    @Column("id")
    @ApiModelProperty("主键")
    private String id;

    @Column("bill_no")
    @ApiModelProperty("拣货单号")
    private String billNo;
    @Column("bar_code")
    @ApiModelProperty("条码")
    private String barCode;
    @Column("item_id")
    @ApiModelProperty("商品id")
    private String itemid;
    @Column("lot_no")
    @ApiModelProperty("批号")
    private String lotNo;

    @Column("qty")
    @ApiModelProperty("数量（重量）")
    private double qty;

    @Column("lot_id")
    @ApiModelProperty("lotid")
    private String lotId;
    @Column("item_name")
    @ApiModelProperty("item_name")
    private String itemName;
    @Column("is_over")
    @ApiModelProperty("is_over")
    private String isOver;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getIsOver() {
        return isOver;
    }

    public void setIsOver(String isOver) {
        this.isOver = isOver;
    }
}
