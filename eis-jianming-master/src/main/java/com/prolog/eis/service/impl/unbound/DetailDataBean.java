package com.prolog.eis.service.impl.unbound;

/**
 * 出库实体对象
 */
public class DetailDataBean {



    private String ownerId;
    private String itemId;
    private String itemName;
    private String lotId;
    private String pickCode;
    private float qty;  //总量
    private float cqty; //正在出库的量
    private float finishQty;
    private String billNo;
    private float last; //需要出库的总量


    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public float getLast() {
        return qty-cqty-finishQty;
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

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public float getCqty() {
        return cqty;
    }

    public void setCqty(float cqty) {
        this.cqty = cqty;
    }

    public float getFinishQty() {
        return finishQty;
    }

    public void setFinishQty(float finishQty) {
        this.finishQty = finishQty;
    }
}
