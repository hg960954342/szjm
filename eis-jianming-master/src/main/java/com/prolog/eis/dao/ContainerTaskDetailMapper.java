package com.prolog.eis.dao;

import com.prolog.eis.model.wms.ContainerTaskDetail;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface ContainerTaskDetailMapper extends BaseMapper<ContainerTaskDetail> {


    @Select("select d.bill_no billno,d.lot_id lotid,d.seqno,d.item_id itemid,d.ownerid,d.qty,t.container_code " +
            "containercode,task_type tasktype,t.source agvloc from container_task_detail d ,container_task t " +
            "where d.container_code=t.container_code and t.container_code=#{containerCode}; ")
    List<Map<String,Object>> getReportData(@Param("containerCode") String containerCode);

    @Select("select d.bill_no billno,d.lot_id lotid,d.seqno,d.item_id itemid,d.ownerid,d.qty,t.container_code containercode,ibk.task_type type,t.source agvloc from container_task_detail d ,container_task t, inbound_task ibk where ibk.bill_no=d.bill_no and d.container_code=t.container_code and t.container_code=#{containerCode}; ")
    List<Map<String, Object>> getInBoundReport(@Param("containerCode")String containerCode);


    @Select("select d.bill_no billno,d.lot_id lotid,d.seqno,d.item_id itemid,d.ownerid,d.qty,t.container_code containercode,ibk.task_type type,t.source agvloc from container_task_detail d ,container_task t, inbound_task ibk where ibk.bill_no=d.bill_no and d.container_code=t.container_code and d.bill_no=#{billNo}; ")
    @Results({
            @Result(column = "billno",property = "billno")

    })
    List<Map<String, Object>> getCheckBoundReport(@Param("billNo")String billNo);
}
