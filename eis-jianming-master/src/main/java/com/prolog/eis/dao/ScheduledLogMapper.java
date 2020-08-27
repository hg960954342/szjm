package com.prolog.eis.dao;

import com.prolog.eis.logs.ScheduledLog;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;


public interface ScheduledLogMapper extends BaseMapper<ScheduledLog> {

    @Delete("delete  from scheduled_log ")
    public void deleteAll();


}