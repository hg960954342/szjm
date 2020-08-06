DROP TABLE IF EXISTS `mcs_task`;
CREATE TABLE `mcs_task`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `task_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务ID',
  `type` int(11) NOT NULL COMMENT '任务类型：1：入库:2：出库 3移库',
  `bank_id` int(11) NOT NULL COMMENT '库编号',
  `container_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '母托盘编号',
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求位置:原坐标',
  `target` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目的位置：目的坐标',
  `weight` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '入库重量',
  `priority` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 99 COMMENT '任务优先级,0-99,0优先级最大',
  `task_state` int(11) NOT NULL COMMENT '任务状态(1.完成、2.失败)',
  `send_count` int(11) NOT NULL COMMENT '发送次数',
  `err_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '错误消息',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5182 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'MCS任务表' ROW_FORMAT = Dynamic;