package com.prolog.eis.dao;

import com.prolog.eis.logs.SysBusinessLog;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;


public interface LogSysBusinessMapper extends BaseMapper<SysBusinessLog> {


    @Delete("delete  from sys_log_business ")
    public void deleteAll();
}