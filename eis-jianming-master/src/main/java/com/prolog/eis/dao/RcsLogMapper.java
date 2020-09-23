package com.prolog.eis.dao;

import com.prolog.eis.dao.base.BasePagerMapper;
import com.prolog.eis.logs.RcsLog;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;


public interface RcsLogMapper extends BaseMapper<RcsLog>, BasePagerMapper {

    @Delete("delete  from rcs_log ")
    public void deleteAll();

    @Select("select count(id) from rcs_log")
    Long coutLog();


}