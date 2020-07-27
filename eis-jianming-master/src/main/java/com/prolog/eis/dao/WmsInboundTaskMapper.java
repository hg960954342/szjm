package com.prolog.eis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.prolog.eis.dto.eis.WmsIqcInboundTaskDto;
import com.prolog.eis.model.eis.WmsInboundTask;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface WmsInboundTaskMapper extends BaseMapper<WmsInboundTask>{

	@Results({
		@Result(property = "id",  column = "id"),
		@Result(property = "commandNo",  column = "command_no"),
		@Result(property = "wmsPush",  column = "wms_push"),
		@Result(property = "detection",  column = "detection"),
		@Result(property = "whNo",  column = "wh_no"),
		@Result(property = "areaNo",  column = "area_no"),
		@Result(property = "taskType",  column = "task_type"),
		@Result(property = "palletId",  column = "pallet_id"),
		@Result(property = "containerCode",  column = "container_code"),
		@Result(property = "palletSize",  column = "pallet_size"),
		@Result(property = "materielNo",  column = "materiel_no"),
		@Result(property = "factoryNo",  column = "factory_no"),
		@Result(property = "materielType",  column = "materiel_type"),
		@Result(property = "materielName",  column = "materiel_name"),
		@Result(property = "factoryCode",  column = "factory_code"),
		@Result(property = "boxCount",  column = "box_count"),
		@Result(property = "matType",  column = "mat_type"),
		@Result(property = "weight",  column = "weight"),
		@Result(property = "stations",  column = "stations"),
		@Result(property = "portNo",  column = "port_no"),
		@Result(property = "finished",  column = "finished"),
		@Result(property = "report",  column = "report"),
		@Result(property = "binNo",  column = "bin_no"),
		@Result(property = "inDate",  column = "in_date"),
		@Result(property = "shfSd",  column = "shf_sd"),
		@Result(property = "errMsg",  column = "err_msg"),
		@Result(property = "createTime",  column = "create_time"),
		@Result(property = "wmsDatasourceType",  column = "wms_datasource_type"),
		@Result(property = "userId",  column = "user_id")
	})
	@Select("select i.*,zt.container_code parentContainerCode,zt.weight ztWeight,p.id portId,p.layer,p.x,p.y\r\n" + 
			"from wms_inbound_task i\r\n" + 
			"left join zt_ckcontainer zt on i.pallet_id = zt.container_subcode\r\n" + 
			"left join port_info p on zt.entry_code = p.junction_port\r\n" + 
			"where i.id = #{id} and i.pallet_size = 'P' and i.task_type = 20 and zt.task_status = 20 and p.task_type = 4")
	List<WmsIqcInboundTaskDto> getRkIqcTask(@Param("id")int id);
	
	@Results({
		@Result(property = "id",  column = "id"),
		@Result(property = "commandNo",  column = "command_no"),
		@Result(property = "wmsPush",  column = "wms_push"),
		@Result(property = "detection",  column = "detection"),
		@Result(property = "whNo",  column = "wh_no"),
		@Result(property = "areaNo",  column = "area_no"),
		@Result(property = "taskType",  column = "task_type"),
		@Result(property = "palletId",  column = "pallet_id"),
		@Result(property = "containerCode",  column = "container_code"),
		@Result(property = "palletSize",  column = "pallet_size"),
		@Result(property = "materielNo",  column = "materiel_no"),
		@Result(property = "factoryNo",  column = "factory_no"),
		@Result(property = "materielType",  column = "materiel_type"),
		@Result(property = "materielName",  column = "materiel_name"),
		@Result(property = "factoryCode",  column = "factory_code"),
		@Result(property = "boxCount",  column = "box_count"),
		@Result(property = "matType",  column = "mat_type"),
		@Result(property = "weight",  column = "weight"),
		@Result(property = "stations",  column = "stations"),
		@Result(property = "portNo",  column = "port_no"),
		@Result(property = "finished",  column = "finished"),
		@Result(property = "report",  column = "report"),
		@Result(property = "binNo",  column = "bin_no"),
		@Result(property = "inDate",  column = "in_date"),
		@Result(property = "shfSd",  column = "shf_sd"),
		@Result(property = "errMsg",  column = "err_msg"),
		@Result(property = "createTime",  column = "create_time"),
		@Result(property = "wmsDatasourceType",  column = "wms_datasource_type"),
		@Result(property = "userId",  column = "user_id")
	})
	@Select("select * from wms_inbound_task t where t.pallet_size = 'P' and t.container_code = #{containerCode} and t.finished > 0 and t.finished < 90")
	List<WmsInboundTask> getRkInStoreTask(@Param("containerCode")String containerCode);
	
	@Select("select t.entry_code\r\n" + 
			"	from sx_connection_rim t\r\n" + 
			"	where t.layer = #{layer} and t.x = #{x} and t.y = #{y} and \r\n" + 
			"	(t.entry_code = 'T010102' or t.entry_code = 'T010202' or t.entry_code = 'T010302' or t.entry_code = 'T010402' \r\n" + 
			"	or t.entry_code = 'T020102' or t.entry_code = 'T020202' or t.entry_code = 'T020302' or t.entry_code = 'T020402')")
	List<String> getRkInStorePositions(@Param("layer")int layer,@Param("x")int x,@Param("y")int y);
	
	@Update("update wms_inbound_task t\r\n" + 
			"set t.finished = 0\r\n" + 
			"where t.command_no = #{commandNo} and t.finished = -1")
	void updateFinishToZreo(@Param("commandNo")String commandNo);
	
	@Select("select DISTINCT t.pallet_id from wms_inbound_task t where EXISTS (select * from port_info p left join stations_port_configure c on p.id = c.port_id left join stations_info s on c.stations_id = s.id where p.port_type = 4 and p.area = 2 and p.junction_port = 'X060102' and s.wms_station_no = t.stations) and t.finished = 0 and t.pallet_size = 'P'")
	List<String> getSxkQcPortUndoInboundTaskPalletId();

	@Results({
		@Result(property = "id",  column = "id"),
		@Result(property = "commandNo",  column = "command_no"),
		@Result(property = "wmsPush",  column = "wms_push"),
		@Result(property = "detection",  column = "detection"),
		@Result(property = "whNo",  column = "wh_no"),
		@Result(property = "areaNo",  column = "area_no"),
		@Result(property = "taskType",  column = "task_type"),
		@Result(property = "palletId",  column = "pallet_id"),
		@Result(property = "containerCode",  column = "container_code"),
		@Result(property = "palletSize",  column = "pallet_size"),
		@Result(property = "materielNo",  column = "materiel_no"),
		@Result(property = "factoryNo",  column = "factory_no"),
		@Result(property = "materielType",  column = "materiel_type"),
		@Result(property = "materielName",  column = "materiel_name"),
		@Result(property = "factoryCode",  column = "factory_code"),
		@Result(property = "boxCount",  column = "box_count"),
		@Result(property = "matType",  column = "mat_type"),
		@Result(property = "weight",  column = "weight"),
		@Result(property = "stations",  column = "stations"),
		@Result(property = "portNo",  column = "port_no"),
		@Result(property = "finished",  column = "finished"),
		@Result(property = "report",  column = "report"),
		@Result(property = "binNo",  column = "bin_no"),
		@Result(property = "inDate",  column = "in_date"),
		@Result(property = "shfSd",  column = "shf_sd"),
		@Result(property = "errMsg",  column = "err_msg"),
		@Result(property = "createTime",  column = "create_time"),
		@Result(property = "wmsDatasourceType",  column = "wms_datasource_type"),
		@Result(property = "userId",  column = "user_id")
	})
	@Select("select * from wms_inbound_task t where t.pallet_id = #{containerSubNo} and t.finished >= 0 and t.finished < 10")
	WmsInboundTask getRkNoStartWmsInboundTask(@Param("containerSubNo")String containerSubNo);
	
	@Results({
		@Result(property = "id",  column = "id"),
		@Result(property = "commandNo",  column = "command_no"),
		@Result(property = "wmsPush",  column = "wms_push"),
		@Result(property = "detection",  column = "detection"),
		@Result(property = "whNo",  column = "wh_no"),
		@Result(property = "areaNo",  column = "area_no"),
		@Result(property = "taskType",  column = "task_type"),
		@Result(property = "palletId",  column = "pallet_id"),
		@Result(property = "containerCode",  column = "container_code"),
		@Result(property = "palletSize",  column = "pallet_size"),
		@Result(property = "materielNo",  column = "materiel_no"),
		@Result(property = "factoryNo",  column = "factory_no"),
		@Result(property = "materielType",  column = "materiel_type"),
		@Result(property = "materielName",  column = "materiel_name"),
		@Result(property = "factoryCode",  column = "factory_code"),
		@Result(property = "boxCount",  column = "box_count"),
		@Result(property = "matType",  column = "mat_type"),
		@Result(property = "weight",  column = "weight"),
		@Result(property = "stations",  column = "stations"),
		@Result(property = "portNo",  column = "port_no"),
		@Result(property = "finished",  column = "finished"),
		@Result(property = "report",  column = "report"),
		@Result(property = "binNo",  column = "bin_no"),
		@Result(property = "inDate",  column = "in_date"),
		@Result(property = "shfSd",  column = "shf_sd"),
		@Result(property = "errMsg",  column = "err_msg"),
		@Result(property = "createTime",  column = "create_time"),
		@Result(property = "wmsDatasourceType",  column = "wms_datasource_type"),
		@Result(property = "userId",  column = "user_id")
	})
	@Select("select * from wms_inbound_task t where t.container_code = #{containerNo} and t.finished >= 0 and t.finished < 10")
	WmsInboundTask getRkNoStartParentWmsInboundTask(@Param("containerNo")String containerNo);
	
	@Results({
		@Result(property = "id",  column = "id"),
		@Result(property = "commandNo",  column = "command_no"),
		@Result(property = "wmsPush",  column = "wms_push"),
		@Result(property = "detection",  column = "detection"),
		@Result(property = "whNo",  column = "wh_no"),
		@Result(property = "areaNo",  column = "area_no"),
		@Result(property = "taskType",  column = "task_type"),
		@Result(property = "palletId",  column = "pallet_id"),
		@Result(property = "containerCode",  column = "container_code"),
		@Result(property = "palletSize",  column = "pallet_size"),
		@Result(property = "materielNo",  column = "materiel_no"),
		@Result(property = "factoryNo",  column = "factory_no"),
		@Result(property = "materielType",  column = "materiel_type"),
		@Result(property = "materielName",  column = "materiel_name"),
		@Result(property = "factoryCode",  column = "factory_code"),
		@Result(property = "boxCount",  column = "box_count"),
		@Result(property = "matType",  column = "mat_type"),
		@Result(property = "weight",  column = "weight"),
		@Result(property = "stations",  column = "stations"),
		@Result(property = "portNo",  column = "port_no"),
		@Result(property = "finished",  column = "finished"),
		@Result(property = "report",  column = "report"),
		@Result(property = "binNo",  column = "bin_no"),
		@Result(property = "inDate",  column = "in_date"),
		@Result(property = "shfSd",  column = "shf_sd"),
		@Result(property = "errMsg",  column = "err_msg"),
		@Result(property = "createTime",  column = "create_time"),
		@Result(property = "wmsDatasourceType",  column = "wms_datasource_type"),
		@Result(property = "userId",  column = "user_id")
	})
	@Select("select * from wms_inbound_task t where t.palletId = #{containerSubNo} and t.finished >= 10 and t.finished < 100")
	WmsInboundTask getRkStartWmsInboundTask(@Param("containerSubNo")String containerSubNo);
	
	@Results({
		@Result(property = "id",  column = "id"),
		@Result(property = "commandNo",  column = "command_no"),
		@Result(property = "wmsPush",  column = "wms_push"),
		@Result(property = "detection",  column = "detection"),
		@Result(property = "whNo",  column = "wh_no"),
		@Result(property = "areaNo",  column = "area_no"),
		@Result(property = "taskType",  column = "task_type"),
		@Result(property = "palletId",  column = "pallet_id"),
		@Result(property = "containerCode",  column = "container_code"),
		@Result(property = "palletSize",  column = "pallet_size"),
		@Result(property = "materielNo",  column = "materiel_no"),
		@Result(property = "factoryNo",  column = "factory_no"),
		@Result(property = "materielType",  column = "materiel_type"),
		@Result(property = "materielName",  column = "materiel_name"),
		@Result(property = "factoryCode",  column = "factory_code"),
		@Result(property = "boxCount",  column = "box_count"),
		@Result(property = "matType",  column = "mat_type"),
		@Result(property = "weight",  column = "weight"),
		@Result(property = "stations",  column = "stations"),
		@Result(property = "portNo",  column = "port_no"),
		@Result(property = "finished",  column = "finished"),
		@Result(property = "report",  column = "report"),
		@Result(property = "binNo",  column = "bin_no"),
		@Result(property = "inDate",  column = "in_date"),
		@Result(property = "shfSd",  column = "shf_sd"),
		@Result(property = "errMsg",  column = "err_msg"),
		@Result(property = "createTime",  column = "create_time"),
		@Result(property = "wmsDatasourceType",  column = "wms_datasource_type"),
		@Result(property = "userId",  column = "user_id")
	})
	@Select("select * from wms_inbound_task t where t.container_code = #{containerNo} and t.finished >= 10 and t.finished < 100")
	WmsInboundTask getRkStartParentWmsInboundTask(@Param("containerNo")String containerNo);
	
	@Results({
		@Result(property = "id",  column = "id"),
		@Result(property = "commandNo",  column = "command_no"),
		@Result(property = "wmsPush",  column = "wms_push"),
		@Result(property = "detection",  column = "detection"),
		@Result(property = "whNo",  column = "wh_no"),
		@Result(property = "areaNo",  column = "area_no"),
		@Result(property = "taskType",  column = "task_type"),
		@Result(property = "palletId",  column = "pallet_id"),
		@Result(property = "containerCode",  column = "container_code"),
		@Result(property = "palletSize",  column = "pallet_size"),
		@Result(property = "materielNo",  column = "materiel_no"),
		@Result(property = "factoryNo",  column = "factory_no"),
		@Result(property = "materielType",  column = "materiel_type"),
		@Result(property = "materielName",  column = "materiel_name"),
		@Result(property = "factoryCode",  column = "factory_code"),
		@Result(property = "boxCount",  column = "box_count"),
		@Result(property = "matType",  column = "mat_type"),
		@Result(property = "weight",  column = "weight"),
		@Result(property = "stations",  column = "stations"),
		@Result(property = "portNo",  column = "port_no"),
		@Result(property = "finished",  column = "finished"),
		@Result(property = "report",  column = "report"),
		@Result(property = "binNo",  column = "bin_no"),
		@Result(property = "inDate",  column = "in_date"),
		@Result(property = "shfSd",  column = "shf_sd"),
		@Result(property = "errMsg",  column = "err_msg"),
		@Result(property = "createTime",  column = "create_time"),
		@Result(property = "wmsDatasourceType",  column = "wms_datasource_type"),
		@Result(property = "userId",  column = "user_id")
	})
	@Select("select t.* from wms_inbound_task t where t.port_no = #{portNo} and t.finished = 9 and t.task_type = 30")
	List<WmsInboundTask> getRkEmptyWmsInboundTask(@Param("portNo")String portNo);

	@Update("update wms_inbound_task t\r\n" + 
			"set t.finished = 92,t.report = t.wms_push\r\n" + 
			"where t.command_no = #{commandNo} and t.finished = 0")
	void cancelTask(String commandNo);
}
