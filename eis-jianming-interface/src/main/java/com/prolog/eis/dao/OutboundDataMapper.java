package com.prolog.eis.dao;

import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.eis.model.wms.OutboundTaskDetail;
import com.prolog.eis.model.wms.OutboundTaskDetailDto;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;

public interface OutboundDataMapper{


    @Insert("insert into outbound_task (bill_no,wms_push,reback,empty_container,task_type,task_state,sfreq,pick_code,owner_id,create_time) " +
            "values(#{billNo},#{wmsPush},#{reBack},#{emptyContainer},#{taskType},#{taskState},#{sfReq},#{pickCode},#{ownerId},#{createTime})")
    void insertOutboundTask(OutboundTask datum);


    @Insert("insert into outbound_task_detail (bill_no,seqno,ctreq,owner_id,item_id,item_name,lot_id,lot,qty,standard,finish_qty,pick_code,create_time)" +
            " values (#{billNo},#{seqNo},#{ctReq},#{ownerId},#{itemId},#{itemName},#{lotId},#{lot},#{qty},#{standard},#{finishQty},#{pickCode},#{createTime})")
    void insertOutboundTaskDetail(OutboundTaskDetailDto detail);

    @Insert("insert into outbound_task (bill_no,wms_push,reback,empty_container,task_type,task_state,create_time) values (#{billNo},#{wmsPush},#{reBack},#{emptyContainer},#{taskType},#{taskState},#{createTime})")
    void insertMoveTask(OutboundTask datum);

    @Insert("insert into outbound_task_detail (bill_no,seqno,ctreq,item_id,item_name,qty,standard,finish_qty,lot_id,lot,pick_code,container_code,create_time)" +
            " values (#{billNo},#{seqNo},#{ctReq},#{itemId},#{itemName},#{qty},#{standard},#{finishQty},#{lotId},#{lot},#{pickCode},#{containerCode},#{createTime})")
    void insertMoveTaskDetail(OutboundTaskDetailDto detail);


    @Insert("insert into outbound_task_detail (bill_no,seqno,ctreq,item_id,item_name,standard,lot_id,lot,create_time) values (#{billNo},#{seqNo},#{ctReq},#{itemId},#{itemName},#{standard},#{lotId},#{lot},#{createTime})")
    void insertCheckOutTaskDetail(OutboundTaskDetailDto detail);

    @Insert("insert into outbound_task (bill_no,wms_push,reback,empty_container,task_type,task_state,pick_code,create_time) values (#{billNo},#{wmsPush},#{reBack},#{emptyContainer},#{taskType},#{taskState},#{pickCode},#{createTime})")
    void insertEmptyBoxOutStockTask(OutboundTask datum);

    @Insert("insert into outbound_task_detail (bill_no,seqno,ctreq,qty,finish_qty,pick_code,create_time) values (#{billNo},#{seqNo},#{ctReq},#{qty},#{finishQty},#{pickCode},#{createTime})")
    void insertEmptyBoxOutStockTaskDetail(OutboundTaskDetailDto detail);
}
