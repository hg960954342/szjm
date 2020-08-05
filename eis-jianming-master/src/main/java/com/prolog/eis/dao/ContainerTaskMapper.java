package com.prolog.eis.dao;

import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ContainerTaskMapper extends BaseMapper<ContainerTask> {
    @Select("select * from container_task where task_state = #{taskState}")
    List<ContainerTask> selectByTaskState(String taskState);

    @Select("select * from container_task where task_code= #{taskCode}")
    ContainerTask selectByTaskCode(String taskCode);


    @Select("select * from container_task where source = #{source} and task_state='1' UNION all \r\n" +
            "select * from container_task where target = #{source} and task_state='1' ")
    List<ContainerTask> selectBySource(@Param("source") String source);

}
