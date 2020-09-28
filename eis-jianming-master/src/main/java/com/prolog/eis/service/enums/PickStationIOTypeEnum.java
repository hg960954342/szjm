package com.prolog.eis.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 拣选站 IO类型 1 入库 2 出库 3(1+2)
 */
@Getter
@AllArgsConstructor
public enum PickStationIOTypeEnum {
    /**
     * 1入库
     */
    IN(1),
    /**
     * 2.出库
     */
     OUT(2),
    /**
     * 3.入库+出库
     */
     IN_OUT(3);


    private Integer ioType;


}
