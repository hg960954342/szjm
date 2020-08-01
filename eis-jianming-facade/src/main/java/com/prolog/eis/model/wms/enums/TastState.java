package com.prolog.eis.model.wms.enums;


/**
 * 托盘状态 INPLACE 到位状态
 *          MOVING 移动中
 *          READY 未开始
 */
public enum TastState {



    INPLACE("1"),
    MOVING("2"),
    READY("0");

    public String getTastState() {
        return tastState;
    }

    private String tastState;

    TastState(String tastState) {
        this.tastState = tastState;
    }
}
