CREATE TABLE `container_task` (
  `id` int NOT NULL AUTO_INCREMENT,
  `container_code` int NOT NULL COMMENT '托盘号',
  `task_type` int DEFAULT NULL COMMENT 'eis内部暂定任务类型  1订单出库  2移库出库  3 盘点出库 4空托出库',
  `source_type` varchar(50) NOT NULL COMMENT '当前托盘区域 1托盘库内 2agv区域',
  `source` varchar(50) NOT NULL COMMENT '当前位置',
  `target` varchar(50) NOT NULL COMMENT '目的地 可能是agv区域坐标，也可能是入库提升机',
  `target_type` varchar(50) NOT NULL COMMENT '1 agv区域 2输送线',
  `tast_state` varchar(50) NOT NULL COMMENT '1 到位 2移动中  后面可以扩充',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_update_time` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='托盘任务表';

CREATE TABLE `container_task_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `container_code` int NOT NULL COMMENT '托盘号',
  `bill_no` varchar(255) NOT NULL COMMENT '出库单号',
  `seqno` varchar(255) NOT NULL COMMENT '明细行号',
  `item_id` varchar(255) DEFAULT NULL COMMENT 'wms商品id',
  `lot_id` varchar(255) DEFAULT NULL COMMENT 'wms批号',
  `ownerid` varchar(255) DEFAULT NULL COMMENT 'wms业主',
  `qty` decimal DEFAULT NULL COMMENT '数量（重量）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='托盘任务表';