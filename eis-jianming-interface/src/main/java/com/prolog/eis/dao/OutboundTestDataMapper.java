package com.prolog.eis.dao;

import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.eis.model.wms.OutboundTaskDetail;
import org.apache.ibatis.annotations.Insert;

public interface OutboundTestDataMapper {


    @Insert("insert into outbound_task (bill_no,task_type,sfreq,pick_code,owner_id,create_time,end_time) " +
            "values(#{billno},#{tasktype},#{sfreq},#{pickcode},#{ownerid},#{createTime},#{endTime})")
    void insertOutboundTask(OutboundTask datum);


    @Insert("insert into outbound_task_detail (seqno,owner_id,item_id,lot_id,qty,pick_code,create_time,end_time)" +
            " values (#{seqno},#{ownerid},#{itemid},#{lotid},#{qty},#{pickcode},#{createTime},#{endTime})")
    void insertOutboundTaskDetail(OutboundTaskDetail detail);
}
