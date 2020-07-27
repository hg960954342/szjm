package com.prolog.eis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.prolog.eis.dto.eis.QcSxPathPlanningTaskHz;

public interface QcSxPathPlanningTaskHzMapper {

	@Results({
		@Result(property = "containerSubNo",  column = "containerSubNo"),
		@Result(property = "id",  column = "id"),
		@Result(property = "configId",  column = "config_id"),
		@Result(property = "startLocation",  column = "start_location"),
		@Result(property = "targetLocation",  column = "target_location"),
		@Result(property = "containerNo",  column = "container_no"),
		@Result(property = "pathType",  column = "path_type"),
		@Result(property = "msg",  column = "msg"),
		@Result(property = "dodgeTime",  column = "dodge_time"),
		@Result(property = "dodgeMsg",  column = "dodge_msg"),
		@Result(property = "createTime",  column = "create_time")
	})
	@Select("select t.containerSubNo,hz.* from sx_path_planning_task_hz hz\r\n" + 
			"left join \r\n" + 
			"(\r\n" + 
			"	select s.CONTAINER_NO containerNo,s.CONTAINER_SUB_NO containerSubNo from sx_store s\r\n" + 
			"	union all\r\n" + 
			"	select zt.container_code containerNo,zt.container_subcode containerSubNo from zt_ckcontainer zt where zt.task_type != 50\r\n" + 
			") t on hz.container_no = t.containerNo")
	List<QcSxPathPlanningTaskHz> findAll();
}
