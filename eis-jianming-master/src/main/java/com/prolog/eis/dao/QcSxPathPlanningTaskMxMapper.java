package com.prolog.eis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.prolog.eis.dto.path.SxPathPlanningTaskDto;
import com.prolog.eis.model.eis.QcSxPathPlanningTaskDto;
import com.prolog.eis.model.path.SxPathPlanningTaskMx;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface QcSxPathPlanningTaskMxMapper extends BaseMapper<SxPathPlanningTaskMx> {

	@Results({
		@Result(property = "id",  column = "id"),
		@Result(property = "sortIndex",  column = "sort_index"),
		@Result(property = "taskHzId",  column = "task_hz_id"),
		@Result(property = "nodeType",  column = "node_type"),
		@Result(property = "transportationEquipment",  column = "transportation_equipment"),
		@Result(property = "node",  column = "node"),
		@Result(property = "lineDirection",  column = "line_direction"),
		@Result(property = "layer",  column = "layer"),
		@Result(property = "x",  column = "x"),
		@Result(property = "y",  column = "y"),
		@Result(property = "isComplete",  column = "is_complete"),
		@Result(property = "createTime",  column = "create_time"),
		@Result(property = "updateTime",  column = "update_time"),
		@Result(property = "taskId",  column = "task_id")
	})
	@Select("select t.* \r\n" + 
			"from sx_path_planning_task_mx t\r\n" + 
			"where t.node like #{hoister} and t.task_hz_id in\r\n" + 
			"(select mx.task_hz_id \r\n" + 
			"from sx_path_planning_task_mx mx\r\n" + 
			"where mx.task_hz_id != #{taskHzId} and mx.is_complete > 0 and mx.is_complete < 40 and mx.node like #{hoister})")
	List<SxPathPlanningTaskMx> getSxPathPlanningTaskMxSameHoister(@Param("taskHzId")int taskHzId,@Param("hoister")String hoister);

	@Select("select\r\n" + 
			"t.task_hz_id as taskHzId,\r\n" + 
			"t.create_time as createTime,\r\n" + 
			"t.id,\r\n" + 
			"t.is_complete as isComplete,\r\n" + 
			"t.line_direction as lineDirection,\r\n" + 
			"t.node as node,\r\n" + 
			"t.node_type as nodeType,\r\n" + 
			"t.sort_index as sortIndex,\r\n" + 
			"t.transportation_equipment as transportationEquipment,\r\n" + 
			"t.x,\r\n" + 
			"t.y,\r\n" + 
			"t.layer,\r\n" + 
			"hz.container_no as containerNo,\r\n" + 
			"hz.path_type as pathType\r\n" + 
			"  from\r\n" + 
			"sx_path_planning_task_mx t\r\n" + 
			"left join sx_path_planning_task_hz hz on\r\n" + 
			"hz.id = t.task_hz_id\r\n" + 
			"  where\r\n" + 
			"(t.is_complete = 10 or t.is_complete = 20)\r\n" + 
			"or (t.sort_index,hz.container_no) in (\r\n" + 
			"select\r\n" + 
			"	(mx.sort_index-1) as sort_index,\r\n" + 
			"	hz.container_no\r\n" + 
			"from\r\n" + 
			"	sx_path_planning_task_mx mx\r\n" + 
			"	left join sx_path_planning_task_hz hz on\r\n" + 
			"hz.id = mx.task_hz_id\r\n" + 
			"where\r\n" + 
			"	mx.is_complete = 10 or mx.is_complete = 20)")
	List<SxPathPlanningTaskDto> findSxCkPathTask();
	
	
	@Select("SELECT\r\n" + 
			"	t.task_hz_id AS taskHzId,\r\n" + 
			"	t.create_time AS createTime,\r\n" + 
			"	t.id,\r\n" + 
			"	t.is_complete AS isComplete,\r\n" + 
			"	t.line_direction AS lineDirection,\r\n" + 
			"	t.node AS node,\r\n" + 
			"	t.node_type AS nodeType,\r\n" + 
			"	t.sort_index AS sortIndex,\r\n" + 
			"	t.transportation_equipment AS transportationEquipment,\r\n" + 
			"	t.x,\r\n" + 
			"	t.y,\r\n" + 
			"	t.layer,\r\n" + 
			"	hz.container_no AS containerNo,\r\n" + 
			"	hz.path_type AS pathType,\r\n" + 
			"  otask.emerge AS emerge\r\n" + 
			"FROM\r\n" + 
			"	sx_path_planning_task_mx t\r\n" + 
			"LEFT JOIN sx_path_planning_task_hz hz ON hz.id = t.task_hz_id\r\n" + 
			"LEFT JOIN wms_outbound_task otask on hz.container_no = otask.container_code\r\n" + 
			"WHERE\r\n" + 
			"	t.is_complete = 10\r\n" + 
			"OR (\r\n" + 
			"	t.sort_index,\r\n" + 
			"	hz.container_no\r\n" + 
			") IN (\r\n" + 
			"	SELECT\r\n" + 
			"		(mx.sort_index - 1) AS sort_index,\r\n" + 
			"		hz.container_no\r\n" + 
			"	FROM\r\n" + 
			"		sx_path_planning_task_mx mx\r\n" + 
			"	LEFT JOIN sx_path_planning_task_hz hz ON hz.id = mx.task_hz_id\r\n" + 
			"	WHERE\r\n" + 
			"		mx.is_complete = 10\r\n" + 
			")")
	List<QcSxPathPlanningTaskDto> findSxPathTask();
}
