DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `class_name` varchar(255)  NOT NULL COMMENT '调用的类',
  `class_simple_name` varchar(255) NOT NULL,
  `class_method` varchar(255)  NOT NULL COMMENT '调用的方法',
  `line_number` varchar(255) NOT NULL,
  `error` varchar(5000) DEFAULT NULL COMMENT '错误信息',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
)COMMENT='EIS系统错误日志表';

DROP TABLE IF EXISTS `eis_log_interface`;
CREATE TABLE `eis_log_interface` (
  `id` int NOT NULL AUTO_INCREMENT,
  `url` varchar(255)  NOT NULL COMMENT '接口地址',
  `params` varchar(5000) DEFAULT NULL,
  `result` varchar(5000)  DEFAULT NULL COMMENT '返回结果',
  `create_time` datetime DEFAULT NULL,
  `error` varchar(5000) DEFAULT NULL COMMENT '错误消息',
  PRIMARY KEY (`id`)
)COMMENT='EIS接口被请求表';

DROP TABLE IF EXISTS `mcs_log`;
CREATE TABLE `mcs_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `interface_address` varchar(255)  NOT NULL COMMENT '接口地址',
  `params` varchar(5000) DEFAULT NULL,
  `result` varchar(5000)  DEFAULT NULL COMMENT '返回结果',
  `create_time` datetime DEFAULT NULL,
  `error` varchar(5000) DEFAULT NULL COMMENT '错误消息',
  PRIMARY KEY (`id`)
)COMMENT='请求MCS接口日志表';

DROP TABLE IF EXISTS `gcs_log`;
CREATE TABLE `gcs_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `interface_address` varchar(255)  NOT NULL COMMENT '接口地址',
  `params` varchar(5000) DEFAULT NULL,
  `result` varchar(5000)  DEFAULT NULL COMMENT '返回结果',
  `create_time` datetime DEFAULT NULL,
  `error` varchar(5000) DEFAULT NULL COMMENT '错误消息',
  PRIMARY KEY (`id`)
)COMMENT='请求GCS接口日志表';

DROP TABLE IF EXISTS `sys_log_business`;
CREATE TABLE `sys_log_business` (
  `id` int NOT NULL AUTO_INCREMENT,
  `class_name` varchar(255)  NOT NULL COMMENT '调用的类',
  `class_simple_name` varchar(255) NOT NULL,
  `class_method` varchar(255)  NOT NULL COMMENT '调用的方法',
  `line_number` varchar(255) NOT NULL,
  `error` varchar(5000) DEFAULT NULL COMMENT '错误信息',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
)COMMENT='EIS系统业务错误日志表';

DROP TABLE IF EXISTS `wms_log`;
CREATE TABLE `wms_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `interface_address` varchar(255)  NOT NULL COMMENT '接口地址',
  `params` varchar(5000) DEFAULT NULL,
  `result` varchar(5000)  DEFAULT NULL COMMENT '返回结果',
  `create_time` datetime DEFAULT NULL,
  `error` varchar(5000) DEFAULT NULL COMMENT '错误消息',
  PRIMARY KEY (`id`)
)COMMENT='请求Wms接口日志表';

DROP TABLE IF EXISTS `scheduled_log`;
CREATE TABLE `scheduled_log`(
`id` int NOT NULL AUTO_INCREMENT,
`class_name` varchar(255) NOT NULL COMMENT '调用的类',
`method_name` varchar(255) NOT NULL COMMENT '调用的方法',
`run_time` varchar(255) DEFAULT NULL,
`start_time` datetime DEFAULT NULL,
`end_time` datetime DEFAULT NULL,
 PRIMARY KEY (`id`)
) COMMENT='定时任务执行日志';
