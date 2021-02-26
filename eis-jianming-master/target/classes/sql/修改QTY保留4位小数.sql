ALTER TABLE inbound_task MODIFY COLUMN qty DECIMAL(10,4);
ALTER TABLE inbound_task_history MODIFY COLUMN qty DECIMAL(10,4);

ALTER TABLE outbound_task_detail MODIFY COLUMN qty DECIMAL(10,4);
ALTER TABLE outbound_task_detail MODIFY COLUMN standard DECIMAL(10,4);

ALTER TABLE outbound_task_detail_history MODIFY COLUMN qty DECIMAL(10,4);
ALTER TABLE outbound_task_detail_history MODIFY COLUMN standard DECIMAL(10,4);

ALTER TABLE picking_task MODIFY COLUMN qty DECIMAL(10,4);
ALTER TABLE sx_store MODIFY COLUMN qty DECIMAL(10,4);