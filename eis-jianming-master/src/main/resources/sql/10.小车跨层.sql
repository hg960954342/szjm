CREATE TABLE `sx_car_across` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `source_layer` int(3) NOT NULL COMMENT '源层',
    `source_area` int(2) NOT NULL COMMENT '源区域',
    `tar_layer` int(3) NOT NULL COMMENT '目标层',
    `tar_area` int(2) NOT NULL COMMENT '目标区域',
    `car_no` varchar(50) DEFAULT NULL COMMENT '小车编号',
    `sx_hoister_id` int(11) NOT NULL COMMENT '唯一指定跨层提升机',
    `sx_hoister_no` varchar(50) NOT NULL,
    `across_status` int(2) NOT NULL COMMENT '跨层状态(0,已创建  , 1.提升机去往源层 ,2 提升机到达源层 3 小车去往源层接驳口, 4 小车到达源层接驳口 ,5 小车去往提升机, 6 小车到达提升机,7 提升机去往目标层 ,8 提升机到达目标层, 9 小车去往目标层接驳口,10 小车到达目标层接驳口, 11 回告提升机解锁完成)',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `abnormal_state` int(11) DEFAULT NULL COMMENT '异常状态 0否 1是',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=4556860 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='小车跨层表';


CREATE TABLE `sx_car_across_his` (
    `id` int(11) NOT NULL,
    `source_layer` int(3) NOT NULL COMMENT '源层',
    `source_area` int(2) NOT NULL COMMENT '源区域',
    `tar_layer` int(3) NOT NULL COMMENT '目标层',
    `tar_area` int(2) NOT NULL COMMENT '目标区域',
    `car_no` varchar(50) DEFAULT NULL COMMENT '小车编号',
    `sx_hoister_id` int(11) NOT NULL COMMENT '唯一指定跨层提升机',
    `sx_hoister_no` varchar(50) NOT NULL,
    `across_status` int(2) NOT NULL COMMENT '跨层状态(0,已创建 1 小车去往源层接驳口, 2 小车到达源层接驳口 , 3.提升机去往源层 ,4 提升机到达源层 ,5 小车去往提升机, 6 小车到达提升机,7 提升机去往目标层 ,8 提升机到达目标层, 9 小车去往目标层接驳口,10 小车到达目标层接驳口, 11 回告提升机解锁完成)',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `abnormal_state` int(11) DEFAULT NULL COMMENT '异常状态 0否 1是',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='小车跨层历史表';


CREATE TABLE `sx_car_across_task` (
    `id` varchar(33) NOT NULL COMMENT '主键',
    `across_id` int(11) NOT NULL COMMENT '跨层任务id',
    `task_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务id',
    `floor` int(5) NOT NULL COMMENT '层',
    `task_type` int(5) NOT NULL COMMENT 'GCS(1,进入提升机接驳口，2进入提升机，3出提升机） MCS(1,提升机到远程 2 提升机到目标层）',
    `systype` varchar(10) NOT NULL COMMENT 'MCS/GCS',
    `hoistId` varchar(20) NOT NULL COMMENT '提升机编号',
    `send_err_msg` varchar(2000) DEFAULT NULL COMMENT '发送返回错误消息',
    `send_status` int(3) NOT NULL COMMENT '发送状态（ 1成功 2失败）',
    `task_status` int(3) NOT NULL DEFAULT '0' COMMENT '任务状态（ 1已发送 2开始 3完成）',
    `send_count` int(11) NOT NULL DEFAULT '1' COMMENT '发送次数',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `finish_time` timestamp NULL DEFAULT NULL COMMENT '任务完成时间',
    `start_time` timestamp NULL DEFAULT NULL COMMENT '任务开始时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='小车跨层GCS/MCS任务表';



CREATE TABLE `sx_car_across_task_his` (
    `id` varchar(33) NOT NULL COMMENT '主键',
    `across_id` int(11) NOT NULL COMMENT '跨层任务id',
    `task_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务id',
    `floor` int(5) NOT NULL COMMENT '层',
    `task_type` int(5) NOT NULL COMMENT 'GCS(1,进入提升机接驳口，2进入提升机，3出提升机） MCS(1,提升机到远程 2 提升机到目标层）',
    `systype` varchar(10) NOT NULL COMMENT 'MCS/GCS',
    `hoistId` varchar(20) NOT NULL COMMENT '提升机编号',
    `send_err_msg` varchar(2000) DEFAULT NULL COMMENT '发送返回错误消息',
    `send_status` int(3) NOT NULL COMMENT '发送状态（ 1成功 2失败）',
    `send_count` int(11) NOT NULL DEFAULT '1' COMMENT '发送次数',
    `task_status` int(11) NOT NULL DEFAULT '0' COMMENT '任务状态（ 1已发送 2开始 3完成）',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `finish_time` timestamp NULL DEFAULT NULL COMMENT '任务完成时间',
    `start_time` timestamp NULL DEFAULT NULL COMMENT '任务开始时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='小车跨层GCS/MCS任务历史表';


CREATE TABLE `sx_car` (
    `id` varchar(10) NOT NULL COMMENT '小车ID',
    `LAYER` int DEFAULT NULL COMMENT '层',
    `CAR_STATE` int DEFAULT NULL COMMENT '小车状态(1.工作，2.空闲,3.跨层任务执行中,4 故障中)',
    `alarm` int DEFAULT NULL COMMENT '故障信息',
    `curr_coord` varchar(20) DEFAULT NULL COMMENT '当前坐标',
    `belong_area` int DEFAULT NULL COMMENT '所属区域 (1 1分区  2 2分区)',
    `last_update_time` datetime DEFAULT NULL COMMENT '最后更新时间',
    `online` bit(1) DEFAULT NULL COMMENT '是否在线',
    `auto` bit(1) DEFAULT NULL COMMENT '是否自动',
    `use` bit(1) DEFAULT NULL COMMENT '是否启用',
    `leisure` bit(1) DEFAULT NULL COMMENT '是否空闲',
    PRIMARY KEY (`id`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=COMPACT COMMENT='SX_CAR';