package com.prolog.eis.dao;

import com.prolog.eis.dao.base.BasePagerAndResultHanlerMapper;
import com.prolog.eis.logs.McsLog;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;


public interface LogMapper extends BaseMapper<McsLog>, BasePagerAndResultHanlerMapper {

    @Delete("delete  from mcs_log ")
    public void deleteAll();


   @Select("select count(id) from mcs_log")
    Long coutMcsLog();
}