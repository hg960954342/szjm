package com.prolog.eis.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PickStationLockEnum {
    LOCK(1),
    NO_LOCK(0);

    private Integer lockTypeNumber;
}
