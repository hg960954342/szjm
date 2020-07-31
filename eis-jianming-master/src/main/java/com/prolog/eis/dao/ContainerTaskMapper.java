package com.prolog.eis.dao;

import com.prolog.eis.model.eis.ContainerTask;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ContainerTaskMapper extends BaseMapper<ContainerTask> {
    @Select("select * from container_task where task_state = #{taskState}")
    List<ContainerTask> selectByTaskState(String taskState);
}
