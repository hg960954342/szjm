package com.prolog.eis.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * eis内部暂定任务类型 0移库出库  1订单出库  3 盘点出库 4空托出库 5托盘入库,6空托补给
 */
@Getter
@AllArgsConstructor
public enum ContainerTaskTaskTypeEnum {

          MOVE_OUT_BOUND(0),
          ORDER_OUT_BOUND(1),
         ORDER_CHECK_OUT_BOUND(3),
         EMPTY_OUT_BOUND(4),
         ORDER_IN_BOUND(5),
         EMPTY_SUPPLY_BOUND(6);
          private Integer taskType;

}
