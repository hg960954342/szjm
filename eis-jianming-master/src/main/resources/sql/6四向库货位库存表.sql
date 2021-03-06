DROP TABLE IF EXISTS `sx_ceng_lock`;
CREATE TABLE `sx_ceng_lock` (
  `LAYER` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='层锁表';

DROP TABLE IF EXISTS `sx_store_location_group`;
CREATE TABLE `sx_store_location_group` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '货位组ID',
  `GROUP_NO` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '货位组编号',
  `ENTRANCE` int(11) NOT NULL COMMENT '入口类型：1、仅入口1，2、仅入口2，3、入口1+入口2',
  `IN_OUT_NUM` int(11) NOT NULL COMMENT '出入口数量',
  `IS_LOCK` int(11) DEFAULT NULL COMMENT '是否锁定',
  `ASCENT_LOCK_STATE` int(11) NOT NULL COMMENT '货位组升位锁',
  `READY_OUT_LOCK` int(11) NOT NULL COMMENT '待出库锁',
  `layer` int(255) DEFAULT NULL COMMENT '层',
  `x` int(255) DEFAULT NULL COMMENT 'X轴',
  `y` int(255) DEFAULT NULL COMMENT 'Y轴',
  `location_num` int(11) DEFAULT NULL COMMENT '货位数量',
  `entrance1_property1` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '入口1的属性1(无入口则值为''none'')',
  `entrance1_property2` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '入口1的属性2(无入口则值为''none'')',
  `entrance2_property1` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '入口2的属性1(无入口则值为''none'')',
  `entrance2_property2` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '入口2的属性2(无入口则值为''none'')',
  `reserved_location` int(255) DEFAULT NULL COMMENT '预留货位1.空托盘预留货位、2.理货预留货位、3.不用预留货位',
  `belong_area` int(255) DEFAULT NULL COMMENT '所属区域',
  `CREATE_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `INDEX_GROUP_NO` (`GROUP_NO`) USING BTREE COMMENT '唯一编号'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='货位组表';

DROP TABLE IF EXISTS `sx_store_location`;
CREATE TABLE `sx_store_location` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '货位id',
  `store_no` varchar(50) DEFAULT NULL COMMENT '货位编号',
  `store_location_group_id` int(11) NOT NULL COMMENT '货位组id',
  `layer` int(11) NOT NULL COMMENT '层',
  `x` int(11) NOT NULL COMMENT 'x轴',
  `y` int(11) NOT NULL COMMENT 'y轴',
  `store_location_id1` int(11) DEFAULT NULL COMMENT '相邻货位id1',
  `store_location_id2` int(11) DEFAULT NULL COMMENT '相邻货位id2',
  `ascent_lock_state` int(11) NOT NULL COMMENT '货位升位锁',
  `location_index` int(11) DEFAULT NULL COMMENT '货位组位置索引(从上到下、从左到右)',
  `depth` int(5) DEFAULT NULL COMMENT '货位深度',
  `dept_num` int(11) DEFAULT NULL COMMENT '移位数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `vertical_location_group_id` int(11)  DEFAULT NULL COMMENT '垂直货位组Id',
  `actual_weight` double  DEFAULT NULL COMMENT '实际重量',
  `limit_weight` double DEFAULT 999999 COMMENT '限重',
  `is_inBound_location` int(11)  DEFAULT NULL COMMENT '是否为入库货位(0.否、1、是)',
  `wms_store_no` varchar(50) COMMENT 'Wms货位编号',
  `task_lock` int(11)  DEFAULT NULL COMMENT '任务锁(0.否、1、是)',
PRIMARY KEY (`id`),
UNIQUE KEY `layer` (`layer`,`x`,`y`),
UNIQUE KEY `store_no` (`store_no`),
KEY `fk_group_id` (`store_location_group_id`),
CONSTRAINT `fk_group_id` FOREIGN KEY (`store_location_group_id`) REFERENCES `sx_store_location_group` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=98653197 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='货位表';

DROP TABLE IF EXISTS `sx_store`;
CREATE TABLE `sx_store` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '容器编号',
  `CONTAINER_NO` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '容器编号',
  `CONTAINER_SUB_NO` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '子容器号',
  `STORE_LOCATION_ID` int(11) DEFAULT NULL COMMENT '货位ID',
  `SX_STORE_TYPE` int(11) COMMENT '库存任务类型 1 wms库存 2 eis库存',
  `TASK_TYPE` int(11) NOT NULL COMMENT '托盘任务类型(-1.空托盘、1.融合、2.HUB、3.MIT)',
  `TASK_PROPERTY1` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '任务属性1（路由）',
  `TASK_PROPERTY2` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '任务属性2（时效）',
  `BUSINESS_PROPERTY1` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '业务属性1',
  `BUSINESS_PROPERTY2` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '业务属性2',
  `BUSINESS_PROPERTY3` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '业务属性3',
  `BUSINESS_PROPERTY4` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '业务属性4',
  `BUSINESS_PROPERTY5` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '业务属性5',
  `STORE_STATE` int(11) DEFAULT NULL COMMENT '库存状态(10：入库中、 20：已上架、 30：出库中、31:待出库、40：移位中)',
  `CAR_NO` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '小车编号',
  `HOISTER_NO` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '提升机编号',
  `IN_STORE_TIME` datetime DEFAULT NULL COMMENT '入库时间',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `TASK_ID` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '任务ID',
  `EMPTY_PALLET_COUNT` int(11)  DEFAULT NULL COMMENT '空托盘垛的托盘数量',
  `source_location_id` int(11)  DEFAULT NULL COMMENT '源货位Id(移位用)',
  `WEIGHT` double  DEFAULT NULL COMMENT '托盘重量',
  `item_id` varchar(255) DEFAULT NULL COMMENT 'wms商品id',
  `lot_id` varchar(255) DEFAULT NULL COMMENT 'wms批号',
  `owner_id` varchar(255) DEFAULT NULL COMMENT 'wms业主',
  `qty` decimal DEFAULT NULL COMMENT '数量',
  `container_state` int(11) DEFAULT NULL COMMENT '容器状态 1，合格 2不合格',
  `station_id` int(11)  DEFAULT NULL COMMENT '站台Id',
	PRIMARY KEY (`ID`) USING BTREE,
  KEY `fk_store_location_id_id` (`STORE_LOCATION_ID`) USING BTREE,
  CONSTRAINT `fk_store_location_id` FOREIGN KEY (`STORE_LOCATION_ID`) REFERENCES `sx_store_location` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='箱库库存表';

DROP TABLE if exists `device_junction_port`;
CREATE TABLE `device_junction_port` (
  `device_no` varchar(50) NOT NULL,
  `entry_code` varchar(50) NOT NULL,
  `layer` int NOT NULL COMMENT '层',
  `x` int NOT NULL COMMENT 'x',
  `y` int NOT NULL COMMENT 'y',
  `port_lock` int NOT NULL COMMENT '是否锁定 1锁定 2不锁定',
  `position` int NOT NULL COMMENT '位置 健民暂时没用',
  PRIMARY KEY (`device_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='设备接驳口';