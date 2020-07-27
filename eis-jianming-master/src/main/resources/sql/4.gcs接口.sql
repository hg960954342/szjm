DROP TABLE IF EXISTS `gcs_task`;
CREATE TABLE `gcs_task` (
  `id` varchar(32) NOT NULL COMMENT '主键uuid',
  `stock_id` varchar(100) NOT NULL COMMENT '托盘编号',
  `task_id` varchar(100) NOT NULL COMMENT '任务ID',
  `layer` int(11) NOT NULL COMMENT '楼层',
  `task_type` int(2) NOT NULL COMMENT '任务类型：1:入库 2:出库 3:搬运',
  `priority` int(2) NOT NULL COMMENT '优先级',
  `loc_id_from` varchar(50) NOT NULL COMMENT '源位置',
  `loc_id_to` varchar(50) DEFAULT NULL COMMENT '目标位置',
  `task_state` int(11) NOT NULL COMMENT '任务状态(1.完成、2.失败)',
  `send_count` int(11) NOT NULL COMMENT '发送次数',
  `err_msg` varchar(1000) NOT NULL COMMENT '错误消息',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `stock_id_unique` (`stock_id`) COMMENT '托盘编号唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='GCS任务表';

DROP TABLE IF EXISTS `gcs_task_history`;
CREATE TABLE `gcs_task_history` (
  `id` varchar(32) NOT NULL COMMENT '主键uuid',
  `stock_id` varchar(100) NOT NULL COMMENT '托盘编号',
  `task_id` varchar(100) NOT NULL COMMENT '任务ID',
  `layer` int(11) NOT NULL COMMENT '楼层',
  `task_type` int(2) NOT NULL COMMENT '任务类型：1:入库 2:出库 3:搬运',
  `priority` int(2) NOT NULL COMMENT '优先级',
  `loc_id_from` varchar(50) NOT NULL COMMENT '源位置',
  `loc_id_to` varchar(50) DEFAULT NULL COMMENT '目标位置',
  `task_state` int(11) NOT NULL COMMENT '任务状态(1.完成、2.失败)',
  `send_count` int(11) NOT NULL COMMENT '发送次数',
  `err_msg` varchar(1000) NOT NULL COMMENT '错误消息',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='GCS任务表';
