package com.prolog.eis.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 拣选站锁类型枚举
 */
@Getter
@AllArgsConstructor
public enum PickStationLockEnum {
    //锁定
    LOCK(1),
    //非锁定
    NO_LOCK(0);

    private Integer lockTypeNumber;
}
