package com.prolog.eis.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 出入库taskType类型枚举
 */

@Getter
@AllArgsConstructor
public enum PortInfoTaskTypeEnum {

    // 1任务托
    TASK(1),
    //2包材
    PACKING_MATERIA(2),
    //3 空托剁或者空托叠
    EMPTY_BUTTRESS(3),
    //4空托盘
    EMPTY_TRAY(4),
    //5质检
    QUALITY_TESTING(5);

    private Integer taskType;


}
