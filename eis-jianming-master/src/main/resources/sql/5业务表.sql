DROP TABLE IF EXISTS `container_task`;
CREATE TABLE `container_task` (
  `id` int NOT NULL AUTO_INCREMENT,
  `container_code` varchar(255) NOT NULL COMMENT '托盘号',
  `task_type` int DEFAULT NULL COMMENT 'eis内部暂定任务类型  1订单出库  2移库出库  3 盘点出库 4空托出库 5托盘入库,6空托补给',
  `source` varchar(50) NOT NULL COMMENT '当前位置',
  `source_type` int NOT NULL COMMENT '当前托盘区域 1托盘库内 2agv区域',
  `target` varchar(50) NOT NULL COMMENT '目的地 可能是agv区域坐标，也可能是入库提升机',
  `target_type` int NOT NULL COMMENT '1 agv区域 2输送线',
  `task_state` int NOT NULL COMMENT '1 到位 2已发送给下游设备 3下游设备回告开始 4离开原存储位 后面可以扩充',
  `task_code` varchar(50) NULL COMMENT '任务号',
  `item_id` varchar(255) DEFAULT NULL COMMENT 'wms商品id',
  `lot_id` varchar(255) DEFAULT NULL COMMENT 'wms批号',
  `owner_id` varchar(255) DEFAULT NULL COMMENT 'wms业主',
  `qty` decimal DEFAULT NULL COMMENT '数量（重量）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `send_time` datetime DEFAULT NULL COMMENT '发送给设备时间',
  `start_time` datetime DEFAULT NULL COMMENT '设备开始时间',
  `move_time` datetime DEFAULT NULL COMMENT '离开原存储位时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='托盘任务表';
DROP TABLE IF EXISTS `container_task_detail`;
CREATE TABLE `container_task_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `container_code` varchar(255) NOT NULL COMMENT '托盘号',
  `bill_no` varchar(255) NOT NULL COMMENT '出库单号',
  `seqno` varchar(255) NOT NULL COMMENT '明细行号',
  `item_id` varchar(255) DEFAULT NULL COMMENT 'wms商品id',
  `lot_id` varchar(255) DEFAULT NULL COMMENT 'wms批号',
  `owner_id` varchar(255) DEFAULT NULL COMMENT 'wms业主',
  `qty` decimal DEFAULT NULL COMMENT '数量（重量）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='托盘任务表';
DROP TABLE IF EXISTS `repeat_report`;
CREATE TABLE `repeat_report` (
  `id` int NOT NULL AUTO_INCREMENT,
  `report_data` varchar(255) NOT NULL COMMENT '回告数据 json格式',
  `report_type` int NOT NULL COMMENT '回告类型 1订单出库  2移库出库  3盘点出库 4空托出库 5订单入库',
  `report_url` varchar(255) NOT NULL COMMENT '回告地址',
  `message` varchar(255) NOT NULL COMMENT '返回信息',
  `report_count` int NOT NULL COMMENT '回告次数',
  `report_state` int DEFAULT 0 COMMENT '回告状态 0 未回告 1已回告 2取消回告',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='回告重发表';
