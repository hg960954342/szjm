package com.prolog.eis.dao;

import com.prolog.eis.dto.eis.CkContainerTaskDto;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface ContainerTaskMapper extends BaseMapper<ContainerTask> {

    @Select("select count(*) from container_task where container_code =#{containerCode}")
    int findByContainerCode(String containerCode);

}
