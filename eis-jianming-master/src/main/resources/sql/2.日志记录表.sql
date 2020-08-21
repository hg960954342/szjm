DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `class_name` varchar(255)  NOT NULL COMMENT '调用的类',
  `class_simple_name` varchar(255) NOT NULL,
  `class_method` varchar(255)  NOT NULL COMMENT '调用的方法',
  `line_number` varchar(255) NOT NULL,
  `error` varchar(255) DEFAULT NULL COMMENT '错误信息',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
)COMMENT='EIS接口请求表';

DROP TABLE IF EXISTS `eis_log_interface`;
CREATE TABLE `eis_log_interface` (
  `id` int NOT NULL AUTO_INCREMENT,
  `url` varchar(255) 8888 NOT NULL COMMENT '接口地址',
  `params` varchar(255) DEFAULT NULL,
  `error` varchar(255) DEFAULT NULL COMMENT '错误消息',
  `result` varchar(255) 8888 DEFAULT NULL COMMENT '返回结果',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
)COMMENT='EIS接口被请求表';

DROP TABLE IF EXISTS `mcs_log`;
CREATE TABLE `mcs_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `interface_address` varchar(255)  NOT NULL COMMENT '接口地址',
  `params` varchar(255) DEFAULT NULL,
  `error` varchar(255) DEFAULT NULL COMMENT '错误消息',
  `result` varchar(255)  DEFAULT NULL COMMENT '返回结果',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
)COMMENT='请求MCS接口日志表';
