package com.prolog.eis.dto.sxk;


import java.math.BigDecimal;

public class OutBoundSxStoreDto {

    private String itemId;
    private String lotId;
    private String ownerId;
    private BigDecimal zqty; //整的数量
    private BigDecimal lqty; //零散的数量
    private BigDecimal qty; //总的数量
    private boolean outOrNo; //是否要出库
    private String containerNo; //托盘号
    private String storeNo; //库内坐标

    private BigDecimal outQty; //出库的量

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

    public BigDecimal getZqty() {
        return zqty;
    }

    public void setZqty(BigDecimal zqty) {
        this.zqty = zqty;
    }

    public BigDecimal getLqty() {
        return lqty;
    }

    public void setLqty(BigDecimal lqty) {
        this.lqty = lqty;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public boolean isOutOrNo() {
        return outOrNo;
    }

    public void setOutOrNo(boolean outOrNo) {
        this.outOrNo = outOrNo;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public BigDecimal getOutQty() {
        return outQty;
    }

    public void setOutQty(BigDecimal outQty) {
        this.outQty = outQty;
    }

    @Override
    public String toString() {
        return "OutBoundSxStoreDto{" +
                "itemId='" + itemId + '\'' +
                ", lotId='" + lotId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", zqty=" + zqty +
                ", lqty=" + lqty +
                ", qty=" + qty +
                ", outOrNo=" + outOrNo +
                ", containerNo='" + containerNo + '\'' +
                ", storeNo='" + storeNo + '\'' +
                ", outQty=" + outQty +
                '}';
    }
}
