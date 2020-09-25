package com.prolog.eis.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 出库的常量
 */
public interface OutBoundEnum {


    @Getter
    @AllArgsConstructor
    enum TaskType {
        MOVE_BOUND(0),//移库出库
        ORDER_BOUND(1),//订单出库
        ORDER_CHECK_OUT_BOUND(3); //盘点出库
        private Integer taskTypeNumber;
    }

    @Getter
    @AllArgsConstructor
    enum TargetType {
        AGV(1), //Agv区域
        SSX(2); //输送线区域

        private int number;


    }


}
