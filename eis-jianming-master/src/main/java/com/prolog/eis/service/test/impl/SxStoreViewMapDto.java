package com.prolog.eis.service.test.impl;

public class SxStoreViewMapDto {
    //库存状态(10：入库中、 20：已上架、 30：出库中、31:待出库、40：移位中)
    private String storeState20; //20: 已经上架
    private String storeState10; //10：入库中
    private String storeState30;//30:出库中
    private String storeState50; //其他状态

    public String getStoreState20() {
        return storeState20;
    }

    public void setStoreState20(String storeState20) {
        this.storeState20 = storeState20;
    }

    public String getStoreState10() {
        return storeState10;
    }

    public void setStoreState10(String storeState10) {
        this.storeState10 = storeState10;
    }

    public String getStoreState30() {
        return storeState30;
    }

    public void setStoreState30(String storeState30) {
        this.storeState30 = storeState30;
    }

    public String getStoreState50() {
        return storeState50;
    }

    public void setStoreState50(String storeState50) {
        this.storeState50 = storeState50;
    }
}
