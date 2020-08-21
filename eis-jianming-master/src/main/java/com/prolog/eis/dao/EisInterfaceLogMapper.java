package com.prolog.eis.dao;

import com.prolog.eis.logs.EisInterfaceLog;
import com.prolog.eis.logs.McsLog;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;


public interface EisInterfaceLogMapper extends BaseMapper<EisInterfaceLog> {

    @Delete("delete  from eis_log_interface ")
    public void deleteAll();
}