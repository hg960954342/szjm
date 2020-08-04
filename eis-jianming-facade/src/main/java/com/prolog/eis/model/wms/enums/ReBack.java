package com.prolog.eis.model.wms.enums;


/**
 * 是否回告wms枚举 1回传 0否
 */
public enum ReBack {


     YES(1),
     NO(0);

     private Integer reback;

    public Integer getReback() {
        return reback;
    }

    ReBack(Integer reback) {
        this.reback = reback;
    }
}
