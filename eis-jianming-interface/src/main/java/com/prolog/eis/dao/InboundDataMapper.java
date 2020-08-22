package com.prolog.eis.dao;

import com.prolog.eis.model.wms.InboundTask;
import org.apache.ibatis.annotations.Insert;

public interface InboundDataMapper {

    @Insert("insert into inbound_task " +
            "(bill_no,wms_push,reback,empty_container,container_code,task_type,item_id,qty,lot_id,ceng,agv_loc,owner_id,task_state,create_time)" +
            "values (#{billNo},#{wmsPush},#{reBack},#{emptyContainer},#{containerCode},#{taskType},#{itemId},#{qty},#{lotId},#{ceng},#{agvLoc},#{ownerId},#{taskState},#{createTime})")
    void insertInboundTask(InboundTask datum);


    @Insert("insert into inbound_task (bill_no,wms_push,reback,empty_container,container_code,task_type,ceng,agv_loc,create_time) values(#{billNo},#{wmsPush},#{reBack},#{emptyContainer},#{containerCode},#{taskType},#{ceng},#{agvLoc},#{createTime})")
    void insertEmptyBoxInStockTask(InboundTask datum);
}
