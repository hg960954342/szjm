package com.prolog.eis.dao;

import com.prolog.eis.dto.eis.CkContainerTaskDto;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.framework.dao.mapper.BaseMapper;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface ContainerTaskMapper extends BaseMapper<ContainerTask> {
	@ResultMap("ContainerTask")
    @Select("select * from container_task where task_state = #{taskState}")
    List<ContainerTask> selectByTaskState(String taskState);
	@ResultMap("ContainerTask")
    @Select("select * from container_task where task_code= #{taskCode}")
    ContainerTask selectByTaskCode(String taskCode);
    
    @Results(id="ContainerTask" ,value={
		@Result(property = "id",  column = "id"),
		@Result(property = "containerCode",  column = "container_code"),
		@Result(property = "taskType",  column = "task_type"),
		@Result(property = "source",  column = "source"),
		@Result(property = "sourceType",  column = "source_type"),
		@Result(property = "target",  column = "target"),
		@Result(property = "targetType",  column = "target_type"),
		@Result(property = "taskState",  column = "task_state"),
		@Result(property = "taskCode",  column = "task_code"),
		@Result(property = "itemId",  column = "item_id"),
		@Result(property = "lotId",  column = "lot_id"),
		@Result(property = "ownerId",  column = "owner_id"),
		@Result(property = "qty",  column = "qty"),
		@Result(property = "createTime",  column = "create_time"),
		@Result(property = "sendTime",  column = "send_time"),
		@Result(property = "startTime",  column = "start_time"),
		@Result(property = "moveTime",  column = "move_time"),
		@Result(property = "endTime",  column = "end_time")
	})
    @Select("select t.* from container_task t where t.container_code = #{containerCode} and (t.task_state = 2 or t.task_state = 3 or t.task_state = 4)")
    ContainerTask selectStartTaskByContainerCode(@Param("containerCode") String containerCode);

   @ResultMap("ContainerTask")
    @Select("select * from container_task where source = #{source} and task_state='1' UNION all \r\n" +
            "select * from container_task where target = #{source} and task_state='1' ")
    List<ContainerTask> selectBySource(@Param("source") String source);


    @Results({
			@Result(property = "containerCode",  column = "container_code"),
	})
	@Select("select * from container_task t, container_task_detail d where container_code=#{containerCode}; ")
	List<Map<String,Object>> getData(@Param("containerCode") String containerCode);
    
    @Select("select l.x,l.y,l.layer,t.container_code containerCode,t.id containerTaskId,t.target,t.target_type targetType,t.task_code taskCode\r\n" + 
    		"from container_task t\r\n" + 
    		"left join sx_store s on t.container_code = s.CONTAINER_NO\r\n" + 
    		"left join sx_store_location l on s.STORE_LOCATION_ID = l.id\r\n" + 
    		"left join sx_store_location_group g on l.store_location_group_id = g.ID\r\n" + 
    		"where t.source_type = 1 and t.task_state = 1 and t.task_code is not null")
    List<CkContainerTaskDto> getCkTask();
}
