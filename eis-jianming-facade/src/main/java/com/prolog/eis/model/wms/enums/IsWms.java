package com.prolog.eis.model.wms.enums;


/**
 * 是否是wms下发 1是 0否
 */
public enum IsWms {


     YES(1),
     NO(0);

     private Integer reback;

    public Integer getReback() {
        return reback;
    }

    IsWms(Integer reback) {
        this.reback = reback;
    }
}
