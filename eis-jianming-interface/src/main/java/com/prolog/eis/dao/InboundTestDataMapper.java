package com.prolog.eis.dao;

import com.prolog.eis.model.wms.InboundTask;
import org.apache.ibatis.annotations.Insert;

import java.sql.Timestamp;

public interface InboundTestDataMapper {

    @Insert("insert into inbound_task " +
            "(bill_no,container_code,task_type,item_id,qty,lot_id,ceng,agv_loc,ownerid,task_state,create_time,end_time)" +
            "values (#{billno},#{containercode},#{tasktype},#{itemid},#{qty},#{lotid},#{ceng},#{agvloc},#{ownerid},#{taskState},#{createTime},#{endTime})")
    void insertInboundTask(InboundTask datum);


}
