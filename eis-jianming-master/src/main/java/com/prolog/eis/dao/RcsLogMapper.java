package com.prolog.eis.dao;

import com.prolog.eis.logs.RcsLog;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;


public interface RcsLogMapper extends BaseMapper<RcsLog> {

    @Delete("delete  from rcs_log ")
    public void deleteAll();


}