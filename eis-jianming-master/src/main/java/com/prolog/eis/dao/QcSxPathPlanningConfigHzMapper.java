package com.prolog.eis.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.prolog.eis.model.path.SxPathPlanningConfigHz;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface QcSxPathPlanningConfigHzMapper extends BaseMapper<SxPathPlanningConfigHz>{

	@Results({
		@Result(property = "id",  column = "id"),
		@Result(property = "startLocation",  column = "start_location"),
		@Result(property = "targetLocation",  column = "target_location"),
		@Result(property = "pathType",  column = "path_type"),
		@Result(property = "msg",  column = "msg"),
		@Result(property = "createTime",  column = "create_time")
	})
	@Select("select phz.*\r\n" + 
			"from sx_path_config_project t\r\n" + 
			"left join sx_path_config_project_hz hz on t.id = hz.project_id\r\n" + 
			"left join sx_path_planning_config_hz phz on hz.path_hz_id = phz.id\r\n" + 
			"where t.project_status = 2 and phz.start_location = #{startPoint} and phz.target_location = #{endPoint}")
	SxPathPlanningConfigHz getPointToPointConfigHz(@Param("startPoint")String startPoint,@Param("endPoint")String endPoint);
}
