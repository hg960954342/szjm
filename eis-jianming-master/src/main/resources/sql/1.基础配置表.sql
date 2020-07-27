
DROP TABLE IF EXISTS `sys_parame`;
CREATE TABLE `sys_parame` (
  `parame_no` varchar(30) NOT NULL COMMENT '参数编号',
  `parame_value` varchar(100) DEFAULT NULL COMMENT '参数值',
  `parame_type` int(11) NOT NULL COMMENT '参数类型 1：字符串 2：整数 3 ：小数',
  `is_read_only` int(11) NOT NULL COMMENT '是否只读 0：可修改  1：只读',
  `visibility` int(11) NOT NULL COMMENT '是否前台展示 0：不展示  1：展示',
  `default_value` varchar(100) DEFAULT NULL COMMENT '默认值',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `sortindex` int(11) NOT NULL COMMENT '排序索引',
  PRIMARY KEY (`parame_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统参数';

DROP TABLE if exists `pallet_info`;
CREATE TABLE `pallet_info` (
  `container_code` varchar(50) NOT NULL COMMENT '母托号',
  PRIMARY KEY (`container_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='母托资料表';

DROP TABLE if exists `port_info`;
CREATE TABLE `port_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `port_type` int NOT NULL COMMENT '出库口类型 （1入库口 2出库口 3出入库口）',
  `task_type` int NOT NULL COMMENT '任务类型 （1任务托 2包材 3 空拖 4质检 ）',
  `work_type` int NOT NULL COMMENT '工作类型 （1人工 2AGV）',
  `reback` int NOT NULL COMMENT '1 可以退回  0 不可退回',
  `show_led` int NOT NULL COMMENT '1 显示Led屏 2 0不显示Led屏',
  `dir_mode` int NOT NULL COMMENT '0 不允许切换方向  1自动切换方向 2手动切换方向',
  `call_car` int NOT NULL COMMENT '是否呼叫agv小车 1呼叫  0 不呼叫',
  `detection` int NOT NULL COMMENT '0 不进行高度检测 1进行高度检测',
  `position` int NOT NULL COMMENT '1 西码头 2 7号口 0 其他',
  `area` int NOT NULL COMMENT '区域 （1 料箱库 2四向库）',
  `default_weight` double COMMENT '默认重量',
  `wms_port_no` varchar(50) NOT NULL COMMENT 'WMS出库口号',
  `junction_port` varchar(50) COMMENT '出入口编号对应 eis sx_connection_rim entry_code',
  `layer` int NOT NULL COMMENT '层',
  `x` int NOT NULL COMMENT 'x',
  `y` int NOT NULL COMMENT 'y',
  `port_lock` int NOT NULL COMMENT '是否锁定 1锁定 2不锁定',
  `task_lock` int NOT NULL COMMENT '任务锁 1锁定 2不锁定',
  `max_ck_count` int NOT NULL COMMENT 'port口最大出库数量',
  `max_rk_count` int NOT NULL COMMENT 'port口最大入库数量',
  `remarks` varchar(255) NOT NULL,
  `led_ip` varchar(255) DEFAULT NULL COMMENT 'LED屏幕ip',
  `error_port` int COMMENT '是否为异常口  1 异常口 0非异常口',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='port口资料表';

DROP TABLE if exists `port_tems_info`;
CREATE TABLE `port_tems_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `port_info_id` int NOT NULL COMMENT '外键port_info id',
  `port_type` int NOT NULL COMMENT '出库口类型 （1暂存位）',
  `task_type` int NOT NULL COMMENT '任务类型 （1人工） ',
  `junction_port` varchar(50) COMMENT '出入口编号对应 eis sx_connection_rim entry_code',
  `layer` int NOT NULL COMMENT '层',
  `x` int NOT NULL COMMENT 'x',
  `y` int NOT NULL COMMENT 'y',
  `port_lock` int NOT NULL COMMENT '是否锁定 1锁定 2不锁定',
  `task_lock` int NOT NULL COMMENT '任务锁 1锁定 2不锁定',
  `remarks` varchar(255) NOT NULL COMMENT '是否锁定 1锁定 2不锁定',
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_sp_portinfoid` FOREIGN KEY (`port_info_id`) REFERENCES `port_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='port口暂存位资料表';

DROP TABLE if exists `stations_info`;
CREATE TABLE `stations_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `wms_station_no` varchar(50) NOT NULL,
  `remark` varchar(50) NOT NULL COMMENT '備註',
  `sort_index` int NOT NULL COMMENT '排序值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='叫料解包區资料表';

DROP TABLE if exists `stations_port_configure`;
CREATE TABLE `stations_port_configure` (
  `id` int NOT NULL AUTO_INCREMENT,
  `stations_id` int NOT NULL,
  `port_id` int NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_sp_stationsid` FOREIGN KEY (`stations_id`) REFERENCES `stations_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_sp_portid` FOREIGN KEY (`port_id`) REFERENCES `port_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='叫料解包區和port配置表';

DROP TABLE if exists `device_junction_port`;
CREATE TABLE `device_junction_port` (
  `device_no` varchar(50) NOT NULL,
  `entry_code` varchar(50) NOT NULL,
  `layer` int NOT NULL COMMENT '层',
  `x` int NOT NULL COMMENT 'x',
  `y` int NOT NULL COMMENT 'y',
  `port_lock` int NOT NULL COMMENT '是否锁定 1锁定 2不锁定',
  `position` int NOT NULL COMMENT '位置 1 23楼需要发mcs前进指令的任务  0其他',
  PRIMARY KEY (`device_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='设备接驳口';

CREATE TABLE `led_message` (
  `id` int NOT NULL AUTO_INCREMENT,
  `port_id` int NOT NULL COMMENT 'port口id',
  `read_state` int NOT NULL DEFAULT '0' COMMENT '是否读取 0未读 1已读',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `state_str` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '状态',
  `message_type` int DEFAULT NULL COMMENT '信息类型 0正常 10报警 20异常',
  `message` varchar(255) DEFAULT NULL COMMENT '信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='led消息';

CREATE TABLE `layer_limit_high` (
  `id` int NOT NULL AUTO_INCREMENT,
  `layer` int NOT NULL COMMENT '层',
  `limit_high` double NOT NULL COMMENT '限高',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='层限高';

CREATE TABLE `layer_port_origin` (
  `id` int NOT NULL AUTO_INCREMENT,
  `entry_code` varchar(50) NOT NULL COMMENT '接驳点',
  `layer` int NOT NULL COMMENT '层',
  `origin_x` int NOT NULL COMMENT 'X原点',
  `origin_y` int NOT NULL COMMENT 'Y原点',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='层原点';

CREATE TABLE `factory_type_config` (
  `id` int NOT NULL AUTO_INCREMENT,
  `factory_type` varchar(50) NOT NULL COMMENT '厂别',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='厂别配置表';

CREATE TABLE `cang_code_config` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cang_code` varchar(50) NOT NULL COMMENT '仓码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='仓码配置表';

CREATE TABLE `factory_name_config` (
  `id` int NOT NULL AUTO_INCREMENT,
  `factory_name` varchar(50) NOT NULL COMMENT '厂商',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='厂商配置表';

CREATE TABLE `materiel_name_config` (
  `id` int NOT NULL AUTO_INCREMENT,
  `materiel_name` varchar(50) NOT NULL COMMENT '品名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='品名配置表';

CREATE TABLE `materiel_no_config` (
  `id` int NOT NULL AUTO_INCREMENT,
  `materiel_no` varchar(50) NOT NULL COMMENT '料号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='料号配置表';

CREATE TABLE `empty_case_config` (
	`id` int NOT NULL AUTO_INCREMENT,
	`layer` int NOT NULL COMMENT '层',
	`min_count` int NOT NULL COMMENT '最小数量',
	`sort_index` int NOT NULL COMMENT '排序值',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='空箱入库配置表';

