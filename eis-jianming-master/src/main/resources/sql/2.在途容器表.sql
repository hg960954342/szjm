DROP TABLE IF EXISTS `zt_ckcontainer`;
CREATE TABLE `zt_ckcontainer` (
  `container_code` varchar(50) NOT NULL COMMENT '母托盘编号', 
  `container_subcode` varchar(50) COMMENT '子托盘编号',
  `stations` varchar(50) COMMENT '目的叫料解包区',
  `port_no` varchar(50) COMMENT 'Wms port口编号',
  `entry_code` varchar(50) COMMENT '接驳口编号',
  `task_type` int DEFAULT NULL COMMENT '任务类型 10一般作业 20质检作业  30空托作业 40 包材任务',
  `task_status` int DEFAULT NULL COMMENT '任务类型 10进行中   20完成',
  `materiel_no` varchar(255) DEFAULT NULL COMMENT '料号',
  `factory_no` varchar(255) DEFAULT NULL COMMENT '物料厂商',
  `materiel_type` varchar(255) DEFAULT NULL COMMENT '物料类别',
  `materiel_name` varchar(255) DEFAULT NULL COMMENT '物料名称',
  `factory_code` varchar(255) DEFAULT NULL COMMENT '倉碼',
  `box_count` varchar(255) DEFAULT NULL COMMENT '箱數',
  `mat_type` varchar(255) DEFAULT NULL COMMENT 'mat_type   VMI  海关  一楼  INX  群志 二三楼',
  `weight` double  DEFAULT NULL COMMENT '托盘重量',
  `detection` int  DEFAULT NULL COMMENT '高度',
  `hoist_no` varchar(255) DEFAULT NULL COMMENT '提升机编号',
  `targe_layer` datetime DEFAULT NULL COMMENT '目標層',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`container_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='出库在途容器表';

DROP TABLE IF EXISTS `zt_container_msg`;
CREATE TABLE `zt_container_msg` (
  `id` int NOT NULL AUTO_INCREMENT,
  `container_code` varchar(50) NOT NULL COMMENT '母托盘编号', 
  `container_subcode` varchar(50) COMMENT '子托盘编号',
  `port_no` varchar(50) COMMENT 'Wms port口编号',
  `entry_code` varchar(50) COMMENT 'eis接驳口',
  `error_msg` varchar(255) COMMENT '错误消息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='出库在途信息表';