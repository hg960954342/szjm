package com.prolog.eis.service.impl.unbound.entity;

import java.math.BigDecimal;

/**
 * 出库实体对象
 */
public class DetailDataBean {



    private String ownerId;
    private String itemId;
    private String itemName;
    private String lotId;
    private String lot;
    private BigDecimal standard; //规格 多少重量一袋
    private String pickCode;
    private BigDecimal qty;  //总量
    private BigDecimal cqty; //正在出库的量
    private BigDecimal finishQty;
    private String billNo;
    private BigDecimal last; //需要出库的总量


    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public BigDecimal getLast() {
        return last;
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

    public BigDecimal getCqty() {
        return cqty;
    }

    public void setCqty(BigDecimal cqty) {
        this.cqty = cqty;
    }

    public BigDecimal getFinishQty() {
        return finishQty;
    }

    public void setFinishQty(BigDecimal finishQty) {
        this.finishQty = finishQty;
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
