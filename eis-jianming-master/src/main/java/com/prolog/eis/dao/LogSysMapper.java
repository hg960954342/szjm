package com.prolog.eis.dao;

import com.prolog.eis.logs.SysLog;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;


public interface LogSysMapper extends BaseMapper<SysLog> {


    @Delete("delete  from sys_log ")
    public void deleteAll();
}