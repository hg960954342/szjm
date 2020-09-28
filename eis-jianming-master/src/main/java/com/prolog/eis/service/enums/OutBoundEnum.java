package com.prolog.eis.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 出库的常量
 */
public interface OutBoundEnum {

    /**
     * 出库类型枚举
     */
    @Getter
    @AllArgsConstructor
    enum TaskType {
        /**
         * //移库出库
         */
        MOVE_BOUND(0),
        /**
         * //订单出库
         */
        ORDER_BOUND(1),
        /**
         * //盘点出库
         */
        ORDER_CHECK_OUT_BOUND(3);
        private Integer taskTypeNumber;
    }

    /**
     * 出库目标类型枚举
     */
    @Getter
    @AllArgsConstructor
    enum TargetType {
        /**
         *  //Agv区域
         */
        AGV(1),
        /**
         * //输送线区域
         */
        SSX(2);

        private int number;


    }


}
