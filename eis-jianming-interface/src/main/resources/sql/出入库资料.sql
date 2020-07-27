DROP TABLE if exists `wms_inbound_task`;
CREATE TABLE `wms_inbound_task` (
  `id` int NOT NULL AUTO_INCREMENT,
  `command_no` varchar(255) NOT NULL COMMENT 'WMS任务流水号',
  `wms_push` int NOT NULL DEFAULT '0' COMMENT '是否wms下发，0不是，1是',
  `detection` int NOT NULL '高度值',
  `wh_no` varchar(255) DEFAULT NULL COMMENT '實體倉庫，庫區代號',
  `area_no` varchar(255) DEFAULT NULL COMMENT '實體倉庫，儲區代號',
  `task_type` int DEFAULT NULL COMMENT '任务类型 10一般作业 20质检作业  30空托作业 40 包材作业',
  `pallet_size` varchar(255) DEFAULT NULL COMMENT 'WMS記錄料號應收的棧板(P  BY 板入庫  C  BY箱入庫)',
  `pallet_id` varchar(255) DEFAULT NULL COMMENT '入庫時的棧板ID、料箱条码',
  `container_code` varchar(255) DEFAULT NULL COMMENT '母托盘编号',
  `materiel_no` varchar(255) DEFAULT NULL COMMENT '料号',
  `factory_no` varchar(255) DEFAULT NULL COMMENT '物料厂商',
  `materiel_type` varchar(255) DEFAULT NULL COMMENT '物料类别',
  `materiel_name` varchar(255) DEFAULT NULL COMMENT '物料名称',
  `box_count` varchar(255) DEFAULT NULL COMMENT '箱数',
  `mat_type` varchar(255) DEFAULT NULL COMMENT 'mat_type   VMI  海关  一楼  INX  群志 二三楼',
  `weight` double DEFAULT NULL COMMENT '入库重量',
  `stations` varchar(255) DEFAULT NULL COMMENT '叫料解包区',
  `port_no` varchar(255) DEFAULT NULL COMMENT 'Port口編號',
  `junction_port` varchar(255) DEFAULT NULL COMMENT 'Port口接駁口',
  `finished` int DEFAULT '0' COMMENT '默认0 10进行中  20进入提升机 90完成作業,91強制完成作業（设备单方面）,92強制取消 -1 异常',
  `report` int DEFAULT '0' COMMENT '默认0 0不需要上报 1需要上报',
  `bin_no` varchar(255) DEFAULT NULL COMMENT '入庫的庫位編號',
  `err_msg` varchar(255) DEFAULT NULL COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `end_time` datetime DEFAULT NULL COMMENT '创建时间',
  `wms_datasource_type` varchar(255) DEFAULT NULL COMMENT 'wms数据源(release/beta)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='入库任务表';

DROP TABLE if exists `wms_inbound_task_history`;
CREATE TABLE `wms_inbound_task_history` (
  `id` int NOT NULL AUTO_INCREMENT,
  `command_no` varchar(255) NOT NULL COMMENT 'WMS任务流水号',
  `wms_push` int NOT NULL DEFAULT '0' COMMENT '是否wms下发，0不是，1是',
  `detection` int NOT NULL '高度值',
  `wh_no` varchar(255) DEFAULT NULL COMMENT '實體倉庫，庫區代號',
  `area_no` varchar(255) DEFAULT NULL COMMENT '實體倉庫，儲區代號',
  `task_type` int DEFAULT NULL COMMENT '任务类型 10一般作业 20质检作业  30空托作业 40 包材作业',
  `pallet_size` varchar(255) DEFAULT NULL COMMENT 'WMS記錄料號應收的棧板(P  BY 板入庫  C  BY箱入庫)',
  `pallet_id` varchar(255) DEFAULT NULL COMMENT '入庫時的棧板ID、料箱条码',
  `container_code` varchar(255) DEFAULT NULL COMMENT '母托盘编号',
  `materiel_no` varchar(255) DEFAULT NULL COMMENT '料号',
  `factory_no` varchar(255) DEFAULT NULL COMMENT '物料厂商',
  `materiel_type` varchar(255) DEFAULT NULL COMMENT '物料类别',
  `materiel_name` varchar(255) DEFAULT NULL COMMENT '物料名称',
  `box_count` varchar(255) DEFAULT NULL COMMENT '箱数',
  `mat_type` varchar(255) DEFAULT NULL COMMENT 'mat_type   VMI  海关  一楼  INX  群志 二三楼',
  `weight` double DEFAULT NULL COMMENT '入库重量',
  `stations` varchar(255) DEFAULT NULL COMMENT '叫料解包区',
  `port_no` varchar(255) DEFAULT NULL COMMENT 'Port口編號',
  `junction_port` varchar(255) DEFAULT NULL COMMENT 'Port口接駁口',
  `finished` int DEFAULT '0' COMMENT '默认0 10进行中  20进入提升机 90完成作業,91強制完成作業（设备单方面）,92強制取消 -1 异常',
  `report` int DEFAULT '0' COMMENT '默认0 0不需要上报 1需要上报',
  `bin_no` varchar(255) DEFAULT NULL COMMENT '入庫的庫位編號',
  `err_msg` varchar(255) DEFAULT NULL COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `wms_datasource_type` varchar(255) DEFAULT NULL COMMENT 'wms数据源(release/beta)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='入库任务表历史';

DROP TABLE if exists `wms_outbound_task`;
CREATE TABLE `wms_outbound_task` (
  `id` int NOT NULL AUTO_INCREMENT,
  `group_id` int NOT NULL COMMENT '任务组',
  `command_no` varchar(255) NOT NULL COMMENT 'WMS任务流水号',
  `wms_push` int NOT NULL DEFAULT '0' COMMENT '是否wms下发，0不是，1是',
  `wh_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '實體倉庫，庫區代號',
  `area_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '實體倉庫，儲區代號',
  `task_type` int DEFAULT NULL COMMENT '任务类型',
  `pallet_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '入庫時的棧板ID、料箱条码',
  `pallet_size` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'WMS記錄料號應收的棧板(P  BY 板入庫  C  BY箱入庫)',
  `container_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '母托盘',
  `emerge` int DEFAULT '0' COMMENT '緊急預設帶：0 數字大愈快出庫',
  `box_count` varchar(255) DEFAULT NULL COMMENT '箱数',
  `outbound_time` datetime DEFAULT NULL COMMENT '出库时效 （什么时间点必须出库）',
  `stations` varchar(255) DEFAULT NULL COMMENT '叫料解包区',
  `port_no` varchar(255) DEFAULT NULL COMMENT 'Port口編號',
  `entry_code` varchar(255) DEFAULT NULL COMMENT '接驳口编号',
  `finished` int DEFAULT '0' COMMENT '默认0 10进行中 90完成作業,91強制完成作業（设备单方面）,92強制取消 ，99空出庫-1 异常',
  `report` int DEFAULT '0' COMMENT '默认0 0不需要上报 1需要上报',
  `err_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `lxk_exit` int(11) DEFAULT NULL COMMENT '料箱库出口：0左，1右',
  `wms_datasource_type` varchar(255) DEFAULT NULL COMMENT 'wms数据源(release/beta)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='出库任务表';

DROP TABLE if exists `wms_outbound_task_history`;
CREATE TABLE `wms_outbound_task_history` (
  `id` int NOT NULL AUTO_INCREMENT,
  `group_id` int NOT NULL COMMENT '任务组',
  `command_no` varchar(255) NOT NULL COMMENT 'WMS任务流水号',
  `wms_push` int NOT NULL DEFAULT '0' COMMENT '是否wms下发，0不是，1是',
  `wh_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '實體倉庫，庫區代號',
  `area_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '實體倉庫，儲區代號',
  `task_type` int DEFAULT NULL COMMENT '任务类型',
  `pallet_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '入庫時的棧板ID、料箱条码',
  `pallet_size` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'WMS記錄料號應收的棧板(P  BY 板入庫  C  BY箱入庫)',
  `container_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '母托盘',
  `emerge` int DEFAULT '0' COMMENT '緊急預設帶：0 數字大愈快出庫',
  `box_count` varchar(255) DEFAULT NULL COMMENT '箱数',
  `outbound_time` datetime DEFAULT NULL COMMENT '出库时效 （什么时间点必须出库）',
  `stations` varchar(255) DEFAULT NULL COMMENT '叫料解包区',
  `port_no` varchar(255) DEFAULT NULL COMMENT 'Port口編號',
  `entry_code` varchar(255) DEFAULT NULL COMMENT '接驳口编号',
  `finished` int DEFAULT '0' COMMENT '默认0 10进行中 90完成作業,91強制完成作業（设备单方面）,92強制取消 ，99空出庫-1 异常',
  `report` int DEFAULT '0' COMMENT '默认0 0不需要上报 1需要上报',
  `err_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `lxk_exit` int(11) DEFAULT NULL COMMENT '料箱库出口：0左，1右',
  `wms_datasource_type` varchar(255) DEFAULT NULL COMMENT 'wms数据源(release/beta)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='出库任务表历史';

DROP TABLE if exists `wms_move_task`;
CREATE TABLE `wms_move_task`(
  `id` int NOT NULL AUTO_INCREMENT,
  `wh_no` varchar(255) NOT NULL COMMENT '實體倉庫，庫區代號',
  `area_no` varchar(255) NOT NULL COMMENT '實體倉庫，儲區代號',
  `bin_no` varchar(255) NOT NULL COMMENT '新的庫位編號',
  `pallet_id` varchar(255) NOT NULL COMMENT '入庫時的棧板ID、料箱条码',
  `pallet_size` varchar(255) NOT NULL COMMENT 'WMS記錄料號應收的棧板(P  BY 板入庫  C  BY箱入庫)',
  `finished` int DEFAULT '0' COMMENT '默认0 10进行中 90完成作業,91強制完成作業（设备单方面）,92強制取消 ，99空出庫-1 异常',
  `report` int DEFAULT '0' COMMENT '默认0 0不需要上报 1需要上报',
  `err_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `wms_datasource_type` varchar(255) DEFAULT NULL COMMENT 'wms数据源(release/beta)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='移库表';

DROP TABLE if exists `wms_move_task_history`;
CREATE TABLE `wms_move_task_history`(
  `id` int NOT NULL AUTO_INCREMENT,
  `wh_no` varchar(255) NOT NULL COMMENT '實體倉庫，庫區代號',
  `area_no` varchar(255) NOT NULL COMMENT '實體倉庫，儲區代號',
  `bin_no` varchar(255) NOT NULL COMMENT '新的庫位編號',
  `pallet_id` varchar(255) NOT NULL COMMENT '入庫時的棧板ID、料箱条码',
  `pallet_size` varchar(255) NOT NULL COMMENT 'WMS記錄料號應收的棧板(P  BY 板入庫  C  BY箱入庫)',
  `finished` int DEFAULT '0' COMMENT '默认0 10进行中 90完成作業,91強制完成作業（设备单方面）,92強制取消 ，99空出庫-1 异常',
  `report` int DEFAULT '0' COMMENT '默认0 0不需要上报 1需要上报',
  `err_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `wms_datasource_type` varchar(255) DEFAULT NULL COMMENT 'wms数据源(release/beta)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='移库表任务表历史';

DROP TABLE if exists `wms_callcar_task`;
CREATE TABLE `wms_callcar_task`(
  `id` int NOT NULL AUTO_INCREMENT,
  `wh_no` varchar(255) NOT NULL COMMENT '實體倉庫，庫區代號',
  `area_no` varchar(255) NOT NULL COMMENT '實體倉庫，儲區代號',
  `pallet_id` varchar(255) NOT NULL COMMENT '棧板ID',
  `product_id` varchar(255) COMMENT '料号',
  `factory_no` varchar(255) COMMENT '物料厂商',
  `materiel_type` varchar(255) COMMENT '物料类别',
  `materiel_name` varchar(255) COMMENT '物料名称',
  `stations` varchar(255) COMMENT '叫料解包區',
  `port` varchar(255) NOT NULL COMMENT '出库口',
  `finished` int DEFAULT '0' COMMENT '默认0  90完成作業,91強制完成作業（设备单方面）,92強制取消',
  `err_code` int DEFAULT NULL COMMENT '异常信息',
  `err_msg` varchar(255) DEFAULT NULL COMMENT '异常消息',
  `creat_time` datetime DEFAULT NULL COMMENT '创建时间',
  `wms_datasource_type` varchar(255) DEFAULT NULL COMMENT 'wms数据源(release/beta)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='手动出库派车';

DROP TABLE if exists `wms_callcar_task_history`;
CREATE TABLE `wms_callcar_task_history`(
  `id` int NOT NULL AUTO_INCREMENT,
  `wh_no` varchar(255) NOT NULL COMMENT '實體倉庫，庫區代號',
  `area_no` varchar(255) NOT NULL COMMENT '實體倉庫，儲區代號',
  `pallet_id` varchar(255) NOT NULL COMMENT '棧板ID',
  `product_id` varchar(255) COMMENT '料号',
  `factory_no` varchar(255) COMMENT '物料厂商',
  `materiel_type` varchar(255) COMMENT '物料类别',
  `materiel_name` varchar(255) COMMENT '物料名称',
  `stations` varchar(255) COMMENT '叫料解包區',
  `port` varchar(255) NOT NULL COMMENT '出库口',
  `finished` int DEFAULT '0' COMMENT '默认0  90完成作業,91強制完成作業（设备单方面）,92強制取消',
  `err_code` int DEFAULT NULL COMMENT '异常信息',
  `err_msg` varchar(255) DEFAULT NULL COMMENT '异常消息',
  `creat_time` datetime DEFAULT NULL COMMENT '创建时间',
  `wms_datasource_type` varchar(255) DEFAULT NULL COMMENT 'wms数据源(release/beta)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='手动出库派车历史';

DROP TABLE if exists `through_task`;
CREATE TABLE `through_task`(
  `id` int NOT NULL AUTO_INCREMENT,
  `pallet_id` varchar(255) NOT NULL COMMENT '棧板ID',
  `container_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '母托盘',
  `materiel_no` varchar(255) COMMENT '料号',
  `factory_no` varchar(255) COMMENT '物料厂商',
  `materiel_type` varchar(255) COMMENT '物料类别',
  `materiel_name` varchar(255) COMMENT '物料名称',
  `start_stations` varchar(255) NOT NULL COMMENT '叫料解包區',
  `start_port` varchar(255) COMMENT '出库口',
  `end_stations` varchar(255) NOT NULL COMMENT '叫料解包區',
  `end_port` varchar(255) COMMENT '出库口',
  `finished` int DEFAULT '0' COMMENT '默认0  90完成作業,91強制完成作業（设备单方面）,92強制取消',
  `err_msg` varchar(255) DEFAULT NULL COMMENT '异常消息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='借道任务';

DROP TABLE if exists `through_task_history`;
CREATE TABLE `through_task_history`(
  `id` int NOT NULL AUTO_INCREMENT,
  `pallet_id` varchar(255) NOT NULL COMMENT '棧板ID',
  `container_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '母托盘',
  `materiel_no` varchar(255) COMMENT '料号',
  `factory_no` varchar(255) COMMENT '物料厂商',
  `materiel_type` varchar(255) COMMENT '物料类别',
  `materiel_name` varchar(255) COMMENT '物料名称',
  `start_stations` varchar(255) NOT NULL COMMENT '叫料解包區',
  `start_port` varchar(255) COMMENT '出库口',
  `end_stations` varchar(255) NOT NULL COMMENT '叫料解包區',
  `end_port` varchar(255) COMMENT '出库口',
  `finished` int DEFAULT '0' COMMENT '默认0  90完成作業,91強制完成作業（设备单方面）,92強制取消',
  `err_msg` varchar(255) DEFAULT NULL COMMENT '异常消息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='借道任务历史';
