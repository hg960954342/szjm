package com.prolog.eis.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 拣选站taskType类型枚举 1 订单 2 移库 3 （1+2）4空拖任务 5 (1+4) 6(2+4) 7(1+2+4)
 */
@Getter
@AllArgsConstructor
public enum PickStationTaskTypeEnum {

     ORDER_TASK_TYPE(1),
     MOVE_TASK_TYPE(2),
     ORDER_AND_MOVE_TASK_TYPE(3),
     EMPTY_TASK_TYPE(4),
     ORDER_AND_EMPTY_TASK_TYPE(5),
     MOVE_AND_EMPTY_TASK_TYPE(6),
     ALL_TASK_TYPE(7);


    private Integer taskType;


}
