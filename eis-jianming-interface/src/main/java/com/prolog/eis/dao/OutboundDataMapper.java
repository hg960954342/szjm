package com.prolog.eis.dao;

import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.eis.model.wms.OutboundTaskDetail;
import org.apache.ibatis.annotations.Insert;

public interface OutboundDataMapper {


    @Insert("insert into outbound_task (bill_no,wms_push,reback,empty_container,task_type,task_state,sfreq,pick_code,owner_id,create_time) " +
            "values(#{billNo},#{wmsPush},#{reBack},#{emptyContainer},#{taskType},#{taskState},#{sfReq},#{pickCode},#{ownerId},#{createTime})")
    void insertOutboundTask(OutboundTask datum);


    @Insert("insert into outbound_task_detail (bill_no,seqno,ctreq,owner_id,item_id,lot_id,qty,finish_qty,pick_code,create_time)" +
            " values (#{billNo},#{seqNo},#{ctReq},#{ownerId},#{itemId},#{lotId},#{qty},#{finishQty},#{pickCode},#{createTime})")
    void insertOutboundTaskDetail(OutboundTaskDetail detail);

    @Insert("insert into outbound_task (bill_no,wms_push,reback,empty_container,task_type,task_state,create_time) values (#{billNo},#{wmsPush},#{reBack},#{emptyContainer},#{taskType},#{taskState},#{createTime})")
    void insertMoveTask(OutboundTask datum);

    @Insert("insert into outbound_task_detail (bill_no,seqno,ctreq,item_id,qty,finish_qty,lot_id,pick_code,container_code,create_time)" +
            " values (#{billNo},#{seqNo},#{ctReq},#{itemId},#{qty},#{finishQty},#{lotId},#{pickCode},#{containerCode},#{createTime})")
    void insertMoveTaskDetail(OutboundTaskDetail detail);


    @Insert("insert into outbound_task_detail (bill_no,seqno,ctreq,item_id,lot_id,create_time) values (#{billNo},#{seqNo},#{ctReq},#{itemId},#{lotId},#{createTime})")
    void insertCheckOutTaskDetail(OutboundTaskDetail detail);

    @Insert("insert into outbound_task (bill_no,wms_push,reback,empty_container,task_type,task_state,pick_code,create_time) values (#{billNo},#{wmsPush},#{reBack},#{emptyContainer},#{taskType},#{taskState},#{pickCode},#{createTime})")
    void insertEmptyBoxOutStockTask(OutboundTask datum);

    @Insert("insert into outbound_task_detail (bill_no,seqno,ctreq,qty,finish_qty,pick_code,create_time) values (#{billNo},#{seqNo},#{ctReq},#{qty},#{finishQty},#{pickCode},#{createTime})")
    void insertEmptyBoxOutStockTaskDetail(OutboundTaskDetail detail);
}
