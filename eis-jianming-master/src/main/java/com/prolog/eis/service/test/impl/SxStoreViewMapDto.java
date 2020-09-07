package com.prolog.eis.service.test.impl;

public class SxStoreViewMapDto {
    //库存状态(10：入库中、 20：已上架、 30：出库中、31:待出库、40：移位中)
    private String storeState20_0; //20_0: 已经上架
    private String storeState20_1; //20_1: 已经上架被锁定
    private String storeState10_0; //10：入库中
    private String storeState10_1; //10：入库中 被锁定
    private String storeState30_0;//30:出库中
    private String storeState30_1;//30:出库中被锁定
    private String storeState50_0; //其他状态
    private String storeState50_1; //其他状态被锁定

    public String getStoreState20_0() {
        return storeState20_0;
    }

    public void setStoreState20_0(String storeState20_0) {
        this.storeState20_0 = storeState20_0;
    }

    public String getStoreState20_1() {
        return storeState20_1;
    }

    public void setStoreState20_1(String storeState20_1) {
        this.storeState20_1 = storeState20_1;
    }

    public String getStoreState10_0() {
        return storeState10_0;
    }

    public void setStoreState10_0(String storeState10_0) {
        this.storeState10_0 = storeState10_0;
    }

    public String getStoreState10_1() {
        return storeState10_1;
    }

    public void setStoreState10_1(String storeState10_1) {
        this.storeState10_1 = storeState10_1;
    }

    public String getStoreState30_0() {
        return storeState30_0;
    }

    public void setStoreState30_0(String storeState30_0) {
        this.storeState30_0 = storeState30_0;
    }

    public String getStoreState30_1() {
        return storeState30_1;
    }

    public void setStoreState30_1(String storeState30_1) {
        this.storeState30_1 = storeState30_1;
    }

    public String getStoreState50_0() {
        return storeState50_0;
    }

    public void setStoreState50_0(String storeState50_0) {
        this.storeState50_0 = storeState50_0;
    }

    public String getStoreState50_1() {
        return storeState50_1;
    }

    public void setStoreState50_1(String storeState50_1) {
        this.storeState50_1 = storeState50_1;
    }
}
