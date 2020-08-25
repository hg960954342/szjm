package com.prolog.eis.dao;

import com.prolog.eis.service.impl.unbound.entity.CheckOutTask;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface CheckOutTaskMapper extends BaseMapper<CheckOutTask> {

    @Select("select count(*) from checkout_task")
    public Double getCount();


    @Select("select (case when count(*)>=1 then 0 else 1 end) from checkout_task where bill_no=#{bill_no} and state!='3' ")
    public Boolean getCheckoutTaskIsEnd(@Param("bill_no") String billNo);
}
