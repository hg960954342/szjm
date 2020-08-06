package com.prolog.eis.dao;

import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.framework.dao.mapper.BaseMapper;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ContainerTaskMapper extends BaseMapper<ContainerTask> {
    @Select("select * from container_task where task_state = #{taskState}")
    List<ContainerTask> selectByTaskState(String taskState);

    @Select("select * from container_task where task_code= #{taskCode}")
    ContainerTask selectByTaskCode(String taskCode);
    
    @Results({
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
		@Result(property = "ownerId",  column = "ownerid"),
		@Result(property = "qty",  column = "qty"),
		@Result(property = "createTime",  column = "create_time"),
		@Result(property = "sendTime",  column = "send_time"),
		@Result(property = "startTime",  column = "start_time"),
		@Result(property = "moveTime",  column = "move_time"),
		@Result(property = "endTime",  column = "end_time")
	})
    @Select("select t.* from container_task t where t.container_code = {containerCode} and (t.task_state = 2 or t.task_state = 3 or t.task_state = 4)")
    ContainerTask selectStartTaskByContainerCode(String containerCode);
}
