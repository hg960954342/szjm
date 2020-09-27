package com.prolog.eis.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AgvLocationTaskLockEnum {
    /**
     * 锁定
     */
    LOCK(1),
    /**
     * 非锁定
     */
    NO_LOCK(0);

    private Integer lockTypeNumber;
}
