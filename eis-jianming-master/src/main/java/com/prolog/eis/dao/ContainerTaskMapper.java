package com.prolog.eis.dao;

import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ContainerTaskMapper extends BaseMapper<ContainerTask> {

    @Select("select * from container_task where task_state=#{taskState} and source_type=#{sourceType}")
    List<ContainerTask> selectByTaskStateAndSourceType(@Param("taskState") String taskState,@Param("sourceType") String sourceType);


    @Select("select * from container_task where task_state = #{taskState}")
    List<ContainerTask> selectByTaskState(String taskState);

    @Select("select * from container_task where task_code= #{taskCode}")
    ContainerTask selectByTaskCode(@Param("taskCode") String taskCode);
}
