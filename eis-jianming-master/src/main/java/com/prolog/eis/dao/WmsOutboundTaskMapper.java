package com.prolog.eis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.prolog.eis.dto.eis.SxkPointDto;
import com.prolog.eis.model.eis.WmsOutboundTask;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface WmsOutboundTaskMapper extends BaseMapper<WmsOutboundTask>{

	@Results({
		@Result(property = "id",  column = "id"),
		@Result(property = "groupId",  column = "GROUP_ID"),
		@Result(property = "commandNo",  column = "command_no"),
		@Result(property = "wmsPush",  column = "wms_push"),
		@Result(property = "whNo",  column = "wh_no"),
		@Result(property = "areaNo",  column = "area_no"),
		@Result(property = "taskType",  column = "task_type"),
		@Result(property = "palletId",  column = "pallet_id"),
		@Result(property = "containerCode",  column = "container_code"),
		@Result(property = "palletSize",  column = "pallet_size"),
		@Result(property = "emerge",  column = "emerge"),
		@Result(property = "outboundTime",  column = "outbound_time"),
		@Result(property = "stations",  column = "stations"),
		@Result(property = "portNo",  column = "port_no"),
		@Result(property = "finished",  column = "finished"),
		@Result(property = "report",  column = "report"),
		@Result(property = "errMsg",  column = "err_msg"),
		@Result(property = "createTime",  column = "create_time")
	})
	@Select("select t.* from wms_outbound_task t\r\n" + 
			"where t.finished < 90\r\n" + 
			"order by t.emerge desc,t.create_time desc")
	List<WmsOutboundTask> getSxkCkTask();
	
	@Results({
		@Result(property = "id",  column = "id"),
		@Result(property = "groupId",  column = "GROUP_ID"),
		@Result(property = "commandNo",  column = "command_no"),
		@Result(property = "wmsPush",  column = "wms_push"),
		@Result(property = "whNo",  column = "wh_no"),
		@Result(property = "areaNo",  column = "area_no"),
		@Result(property = "taskType",  column = "task_type"),
		@Result(property = "palletId",  column = "pallet_id"),
		@Result(property = "containerCode",  column = "container_code"),
		@Result(property = "palletSize",  column = "pallet_size"),
		@Result(property = "emerge",  column = "emerge"),
		@Result(property = "outboundTime",  column = "outbound_time"),
		@Result(property = "stations",  column = "stations"),
		@Result(property = "portNo",  column = "port_no"),
		@Result(property = "finished",  column = "finished"),
		@Result(property = "report",  column = "report"),
		@Result(property = "errMsg",  column = "err_msg"),
		@Result(property = "createTime",  column = "create_time")
	})
	@Select("select t.*\r\n" + 
			"from wms_outbound_task t\r\n" + 
			"where t.finished > 0 and t.finished < 90 and t.container_code = #{containerNo}")
	WmsOutboundTask getNoFinishTask(@Param("containerNo")String containerNo);
	
	@Select("select mx.layer,mx.x,mx.y from sx_path_planning_task_hz hz\r\n" + 
			"left join sx_path_planning_task_mx mx on hz.id = mx.task_hz_id\r\n" + 
			"left join sx_connection_rim r on mx.node = r.entry_code\r\n" + 
			"where hz.container_no = #{containerNo} and hz.path_type = 2 and mx.node_type = 3 and (mx.y = 15 or mx.y = 14)")
	List<SxkPointDto> getCkXmtNodes(@Param("containerNo")String containerNo);
	
	@Select("select mx.node\r\n" + 
			"from sx_path_planning_task_hz hz\r\n" + 
			"left join sx_path_planning_task_mx mx on hz.id = mx.task_hz_id\r\n" + 
			"where hz.path_type = 2 and (mx.node = 'T010102' or mx.node = 'T010202' or mx.node = 'T010302' or mx.node = 'T010402') and mx.layer = #{layer} and mx.is_complete < 40")
	List<String> getT01CkNode(@Param("layer")int layer);
	
	@Update("update wms_outbound_task t\r\n" + 
			"set t.finished = 0\r\n" + 
			"where t.command_no = #{commandNo} and t.finished = -1")
	void updateFinishToZreo(@Param("commandNo")String commandNo);

	@Update("update wms_outbound_task t\r\n" + 
			"set t.finished = 92,t.report = t.wms_push\r\n" + 
			"where t.command_no = #{commandNo} and t.finished = -1")
	void cancelTask(String commandNo);
	
	@Select("select count(*) from stacker_store s where s.box_no = #{boxNo}")
	int getStackerStoreExists(@Param("boxNo") String boxNo);
}
