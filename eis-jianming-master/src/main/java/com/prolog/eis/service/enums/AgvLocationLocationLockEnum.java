package com.prolog.eis.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AgvLocationLocationLockEnum {
    /**
     * 锁定
     */
    LOCK(1),
    /**
     * 不锁定
     */
    NO_LOCK(0);

    private Integer lockTypeNumber;


}
