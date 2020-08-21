package com.prolog.eis.dao;

import com.prolog.eis.logs.McsLog;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;


public interface LogMapper extends BaseMapper<McsLog> {

    @Delete("delete  from mcs_log ")
    public void deleteAll();


}