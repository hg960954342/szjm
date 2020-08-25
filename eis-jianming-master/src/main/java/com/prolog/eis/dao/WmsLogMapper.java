package com.prolog.eis.dao;

import com.prolog.eis.logs.WmsLog;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;


public interface WmsLogMapper extends BaseMapper<WmsLog> {

    @Delete("delete  from wms_log ")
    public void deleteAll();


}