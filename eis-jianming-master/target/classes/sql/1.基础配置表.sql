
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
  `task_type` int NOT NULL COMMENT '任务类型 （1任务托 2包材 3 空托剁 4空托盘 5质检 ）',
  `work_type` int NOT NULL COMMENT '工作类型 （1人工 2AGV）',
  `reback` int NOT NULL COMMENT '1 可以退回  0 不可退回',
  `show_led` int NOT NULL COMMENT '1 显示Led屏 2 0不显示Led屏',
  `dir_mode` int NOT NULL COMMENT '0 不允许切换方向  1自动切换方向 2手动切换方向',
  `call_car` int NOT NULL COMMENT '是否呼叫agv小车 1呼叫  0 不呼叫',
  `detection` int NOT NULL COMMENT '0 不进行高度检测 1进行高度检测',
  `position` int NOT NULL COMMENT '0 输送线中间点位 1 输送线最前端点位',
  `area` int NOT NULL COMMENT '区域 （1 料箱库 2四向库）建民暂时没用到',
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

DROP TABLE if exists `pick_station`;
CREATE TABLE `pick_station` (
  `id` int NOT NULL AUTO_INCREMENT,
  `station_no` varchar(20) NOT NULL COMMENT '拣选站编号',
  `auto_supply` int NOT NULL COMMENT '是否自动补给空托盘',
  `io` int NOT NULL COMMENT '1 入库 2 出库 3(1+2)',
  `task_type` int NOT NULL COMMENT '1 订单 2 移库 3 （1+2）4空拖任务 5 (1+4) 6(2+4) 7(1+2+4)',
  `islock` varchar(50) COMMENT '锁定 0不锁定 1锁定',
  `device_no` varchar(50) default null comment '设备编号',
  `remarks` varchar(255) NOT NULL COMMENT '',
  PRIMARY KEY (`id`)
) COMMENT='拣选站表';

drop table if exists `agv_storagelocation`;
create table `agv_storagelocation` (
  `id` int(11) not null comment '主键',
  `ceng` int NOT NULL COMMENT '楼层',
  `x` int(11) not null comment '坐标x',
  `y` int(11) not null comment '坐标y',
  `location_type` int(11) not null comment '位置类型 1存储位 2 输送线 3整托托盘作业位 4非整托托盘作业位 5通用托盘位',
  `rcs_position_code` varchar(50) null comment 'rcs点位编号',
  `tally_code` varchar(50) null comment 'wms货位 可空',
  `task_lock` int(11) not null comment '任务锁  0空闲 1锁定',
  `location_lock` int(11) not null comment '锁定 0不锁定 1锁定',
  `device_no` varchar(50) default null comment '设备编号',
  `remarks` varchar(50) default null comment '拣选站名称',
  primary key (`id`)
) ;

DROP TABLE IF EXISTS `led_port`;
CREATE TABLE `led_port`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `led_ip` varchar(255) NULL DEFAULT NULL COMMENT 'Led ip',
  `port` int(0) NULL DEFAULT NULL COMMENT '端口',
  `message` varchar(255)  NULL DEFAULT NULL COMMENT 'ip 信息',
  PRIMARY KEY (`id`) USING BTREE
) ;



