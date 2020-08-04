DROP TABLE if exists `inbound_task`;
CREATE TABLE `inbound_task` (
  `id` int NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(255) NOT NULL COMMENT '入库单号',
  `wms_push` int NOT NULL DEFAULT '0' COMMENT '是否wms下发，0不是，1是',
  `reback` int NOT NULL DEFAULT '0' COMMENT '是否回传，0不回传，1回传',
  `empty_container` int NOT NULL DEFAULT '0' COMMENT '0任务托  1空托',
  `task_type` int DEFAULT NULL COMMENT '任务托暂未定    空托的情况 0空托垛入库  1空托碟',
  `container_code` varchar(255) DEFAULT NULL COMMENT '母托盘编号',
  `ceng` varchar(20) DEFAULT NULL COMMENT '入库楼层',
  `agv_loc` varchar(255) DEFAULT NULL COMMENT 'Agv搬运点',
  `item_id` varchar(255) DEFAULT NULL COMMENT 'wms商品id',
  `lot_id` varchar(255) DEFAULT NULL COMMENT 'wms批号',
  `ownerid` varchar(255) DEFAULT NULL COMMENT 'wms业主',
  `qty` decimal DEFAULT NULL COMMENT '数量（重量）',
  `task_state` int NOT NULL DEFAULT '0' COMMENT '0 创建 1开始 3扫码入库 4完成',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `ruku_time` datetime DEFAULT NULL COMMENT '扫码入库时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='入库任务表';

DROP TABLE if exists `inbound_task_history`;
CREATE TABLE `inbound_task_history` (
  `id` int NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(255) NOT NULL COMMENT '入库单号',
  `wms_push` int NOT NULL DEFAULT '0' COMMENT '是否wms下发，0不是，1是',
  `reback` int NOT NULL DEFAULT '0' COMMENT '是否回传，0不回传，1回传',
  `empty_container` int NOT NULL DEFAULT '0' COMMENT '0任务托  1空托',
  `task_type` int DEFAULT NULL COMMENT '任务托暂未定    空托的情况 0空托垛入库  1空托碟',
  `container_code` varchar(255) DEFAULT NULL COMMENT '母托盘编号',
  `ceng` varchar(20) DEFAULT NULL COMMENT '入库楼层',
  `agv_loc` varchar(255) DEFAULT NULL COMMENT 'Agv搬运点',
  `item_id` varchar(255) DEFAULT NULL COMMENT 'wms商品id',
  `lot_id` varchar(255) DEFAULT NULL COMMENT 'wms批号',
  `ownerid` varchar(255) DEFAULT NULL COMMENT 'wms业主',
  `qty` decimal DEFAULT NULL COMMENT '数量（重量）',
  `task_state` int NOT NULL DEFAULT '0' COMMENT '0 创建 1开始 3扫码入库 4完成',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `ruku_time` datetime DEFAULT NULL COMMENT '扫码入库时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='入库任务历史表';

DROP TABLE if exists `outbound_task`;
CREATE TABLE `outbound_task` (
  `id` int NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(255) NOT NULL COMMENT '出库单号',
  `wms_push` int NOT NULL DEFAULT '0' COMMENT '是否wms下发，0不是，1是',
  `reback` int NOT NULL DEFAULT '0' COMMENT '是否回传，0不回传，1回传',
  `empty_container` int NOT NULL DEFAULT '0' COMMENT '0任务托  1空托',
  `task_type` int DEFAULT NULL COMMENT 'eis内部暂定任务类型  1订单出库  2移库出库  3 盘点出库 4空托出库',
  `sfreq` int NOT NULL DEFAULT '0' COMMENT '站点要求 0 无   1有',
  `pick_code` varchar(20) DEFAULT NULL COMMENT '拣选站',
  `owner_id` varchar(20) DEFAULT NULL COMMENT '货主',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='出库任务表';

DROP TABLE if exists `outbound_task_history`;
CREATE TABLE `outbound_task_history` (
  `id` int NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(255) NOT NULL COMMENT '出库单号',
  `wms_push` int NOT NULL DEFAULT '0' COMMENT '是否wms下发，0不是，1是',
  `reback` int NOT NULL DEFAULT '0' COMMENT '是否回传，0不回传，1回传',
  `task_type` int DEFAULT NULL COMMENT 'eis内部暂定任务类型  1订单出库  2移库出库  3 盘点出库 4空托出库',
  `sfreq` int NOT NULL DEFAULT '0' COMMENT '站点要求 0 无   1有',
  `pick_code` varchar(20) DEFAULT NULL COMMENT '拣选站',
  `owner_id` varchar(20) DEFAULT NULL COMMENT '货主',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='出库任务表';

DROP TABLE if exists `outbound_task_detail`;
CREATE TABLE `outbound_task_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(255) NOT NULL COMMENT '出库单号',
  `seqno` varchar(255) NOT NULL COMMENT '明细行号',
  `ctreq` int NOT NULL DEFAULT '0' COMMENT '是否指定托盘 0不指定 1指定',
  `container_code` int NOT NULL DEFAULT '0' COMMENT '容器号',
  `owner_id` varchar(20) DEFAULT NULL COMMENT '货主',
  `item_id` varchar(255) DEFAULT NULL COMMENT 'wms商品id',
  `lot_id` varchar(255) DEFAULT NULL COMMENT 'wms批号',
  `qty` decimal DEFAULT NULL COMMENT '数量（重量）',
  `finish_qty` decimal DEFAULT NULL COMMENT '完成数量（重量）',
  `pick_code` varchar(20) DEFAULT NULL COMMENT '拣选站  指定拣选站  暂时移库出库用到 ',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='出库任务明细表';

DROP TABLE if exists `outbound_task_detail_history`;
CREATE TABLE `outbound_task_detail_history` (
  `id` int NOT NULL AUTO_INCREMENT,
  `seqno` varchar(255) NOT NULL COMMENT '明细行号',
  `ctreq` int NOT NULL DEFAULT '0' COMMENT '是否指定托盘 0不指定 1指定',
  `container_code` int NOT NULL DEFAULT '0' COMMENT '0任务托  1空托',
  `item_id` varchar(255) DEFAULT NULL COMMENT 'wms商品id',
  `lot_id` varchar(255) DEFAULT NULL COMMENT 'wms批号',
  `qty` decimal DEFAULT NULL COMMENT '数量（重量）',
  `pick_code` varchar(20) DEFAULT NULL COMMENT '拣选站  指定拣选站  暂时移库出库用到 ',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='出库任务明细表';

DROP TABLE IF EXISTS `wms_eis_idempotent`;
CREATE TABLE `wms_eis_idempotent`  (
  `message_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '幂等数',
  `rejson` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '返回json串数据',
  `loc_date` datetime(0) NULL DEFAULT NULL COMMENT '当前本地时间',
  PRIMARY KEY (`message_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
