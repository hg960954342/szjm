-- 托盘路径规划视图
CREATE
OR REPLACE VIEW sxpathplanningtaskhzview AS 
SELECT
	t.id,
	t.start_location startLocation,
	t.target_location targetLocation,
	t.path_type pathType,
	t.msg,
	t.container_no containerNo,
	t.create_time createTime 
FROM
	sx_path_planning_task_hz t;
	
-- 入库任务视图
CREATE OR REPLACE VIEW wmsinboundtaskview AS 
select * from
(
select t.id,t.command_no commandNo,t.wms_push wmsPush,t.task_type taskType,t.pallet_size palletSize,t.pallet_id palletId,t.container_code containerCode,t.materiel_no materielNo,t.factory_no factoryNo,
t.materiel_type materielType,t.materiel_name materielName,t.mat_type matType,t.weight,t.stations,t.port_no portNo,t.finished,t.create_time createTime
from wms_inbound_task t
union all
select his.id,his.command_no commandNo,his.wms_push wmsPush,his.task_type taskType,his.pallet_size palletSize,his.pallet_id palletId,his.container_code containerCode,his.materiel_no materielNo,his.factory_no factoryNo,
his.materiel_type materielType,his.materiel_name materielName,his.mat_type matType,his.weight,his.stations,his.port_no portNo,his.finished,his.create_time createTime 
from wms_inbound_task_history his
) task;

-- 出庫任務視圖
CREATE OR REPLACE VIEW wmsoutboundtaskview AS 
select * from
(
select t.id,t.group_id groupId,t.command_no commandNo,t.wms_push wmsPush,t.task_type taskType,t.pallet_size palletSize,t.pallet_id palletId,t.container_code containerCode,t.stations,t.port_no portNo,t.finished,t.create_time createTime
from wms_outbound_task t
union all
select his.id,his.group_id groupId,his.command_no commandNo,his.wms_push wmsPush,his.task_type taskType,his.pallet_size palletSize,his.pallet_id palletId,his.container_code containerCode,his.stations,his.port_no portNo,his.finished,his.create_time createTime
from wms_outbound_task_history his
) task;

--料箱库库存视图
CREATE OR REPLACE VIEW stackerstoreview AS 
SELECT
	s.id,
	s.box_no AS boxNo,
	s.store_type AS stackerStoreType,
	s.wms_push AS wmsPush,
	s.store_state AS storeState,
	s.create_time AS createTime,
	s.weight AS weight,
	l.hangdao AS hangdao,
	l.pai AS pai,
	l.ceng AS ceng,
	l.lie AS lie,
	l.address AS wmsStoreNo,
	s.uda0 AS whNo,
	s.uda1 AS areaNo,
	s.uda2 AS factoryNo,
	s.uda3 AS materielType,
	s.uda4 AS matType,
	s.materiel_code as materielCode,
	s.materiel_name as materielName
FROM
	stacker_store s
LEFT JOIN stacker_store_location l ON s.store_location_id = l.id;

-- 四向库库存视图
CREATE OR REPLACE VIEW storeview AS 
select t.id id,t.CONTAINER_NO containerNo,t.CONTAINER_SUB_NO containerSubNo,t.SX_STORE_TYPE sxStoreType,t.TASK_TYPE taskType,t.TASK_PROPERTY1 taskProperty1,
t.TASK_PROPERTY2 taskProperty2,t.BUSINESS_PROPERTY1 businessProperty1,t.BUSINESS_PROPERTY2 businessProperty2,t.BUSINESS_PROPERTY3 businessProperty3,
t.BUSINESS_PROPERTY4 businessProperty4,t.BUSINESS_PROPERTY5 businessProperty5,t.STORE_STATE storeState,t.CREATE_TIME createTime,t.WEIGHT weight,
s.layer,s.x,s.y,s.wms_store_no wmsStoreNo,g.GROUP_NO groupNo
from sx_store t
left join sx_store_location s on t.STORE_LOCATION_ID = s.id
left join sx_store_location_group g on s.store_location_group_id = g.ID;

-- 母托视图
CREATE OR REPLACE VIEW palletinfoview AS 
select t.container_code containerCode from pallet_info t;

-- 异常信息视图
CREATE OR REPLACE VIEW xmtexceptionhandview AS 
select zt.id Id,zt.container_code containerCode,zt.container_subcode containerSubCode,zt.port_no portNo,zt.entry_code entryCode,zt.error_msg errorMsg,zt.create_time createTime,p.position
from zt_container_msg zt
left join port_info p on p.wms_port_no = zt.port_no;

-- 异常信息视图
CREATE OR REPLACE VIEW throughtaskview AS 
select t.id,t.pallet_id palletId,t.container_code containerCode,t.materiel_no materielNo,t.factory_no factoryNo,t.materiel_type materielType,t.materiel_name materielName,
	t.start_stations startStations,t.start_port startPort,t.end_stations endStations,t.end_port endPort,t.finished,t.err_msg errMsg,t.create_time creatTime
from through_task t
union ALL
select his.id,his.pallet_id palletId,his.container_code containerCode,his.materiel_no materielNo,his.factory_no factoryNo,his.materiel_type materielType,his.materiel_name materielName,
	his.start_stations startStations,his.start_port startPort,his.end_stations endStations,his.end_port endPort,his.finished,his.err_msg errMsg,his.create_time creatTime
from through_task_history his;
