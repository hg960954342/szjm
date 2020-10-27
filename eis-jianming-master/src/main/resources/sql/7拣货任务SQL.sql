DROP TABLE IF EXISTS `picking_task` ;
CREATE TABLE `picking_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(255) NOT NULL COMMENT '拣货单号',
  `bar_code` varchar(255) NOT NULL COMMENT '条码',
  `item_id` varchar(255) DEFAULT NULL COMMENT 'wms商品id',
  `lot_no` varchar(255) DEFAULT NULL COMMENT 'wms批号',
  `lot_id` varchar(255) DEFAULT NULL COMMENT '批号',
  `item_name` varchar(255) DEFAULT NULL COMMENT '商品名称',
  `qty` decimal(10,0) DEFAULT NULL COMMENT '数量（重量）',
  `is_over` varchar(255) DEFAULT NULL COMMENT '结束标识N,Y（结束）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='拣货任务表';