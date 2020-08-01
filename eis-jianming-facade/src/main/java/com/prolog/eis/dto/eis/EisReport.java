package com.prolog.eis.dto.eis;

/**
 * 回告wms 明细 实体类
 * 订单、商品、批号、分摊数量、托盘、点位。
 */
public class EisReport {

    //容器
    private String containerCode;

    //订单id
    private String billNo;
    //商品
    private String itemId;
    //批号
    private String lotId;
    //分摊数量
    private double qty;

    //点位
    private String agvLoc;

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

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public String getAgvLoc() {
        return agvLoc;
    }

    public void setAgvLoc(String agvLoc) {
        this.agvLoc = agvLoc;
    }
}
