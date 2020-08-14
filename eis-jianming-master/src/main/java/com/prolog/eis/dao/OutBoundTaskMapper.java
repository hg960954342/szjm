package com.prolog.eis.dao;

import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface OutBoundTaskMapper extends BaseMapper<OutboundTask>{



    @Results(id="OutboundTask" , value= {
            @Result(property = "id",  column = "id"),
            @Result(property = "billNo",  column = "bill_no"),
            @Result(property = "wmsPush",  column = "wms_push"),
            @Result(property = "reBack",  column = "reback"),
            @Result(property = "emptyContainer",  column = "empty_container"),
            @Result(property = "taskType",  column = "task_type"),
            @Result(property = "sfReq",  column = "sfreq"),
            @Result(property = "pickCode",  column = "pick_code"),
            @Result(property = "ownerId",  column = "owner_id"),
            @Result(property = "createTime",  column = "create_time"),
            @Result(property = "endTime",  column = "end_time")
    })
    @Select("select t.* from outbound_task t where t.task_state=0\r\n" +
            "\r\n" +
            "order by t.create_time desc")
     List<OutboundTask> getListOutboundTask();


     @ResultType(value = java.lang.String.class)
    /**
     * 获取所有超时出库订单 按降序排序
     * @return
     */
    @Select("select * from (select TIMESTAMPDIFF(MINUTE,t.create_time,NOW()) overtime,t.* from outbound_task t where t.task_type=1 )a \r\n"+
            "WHERE a.overtime>#{overTime} ORDER BY a.overtime")
    List<OutboundTask> getOutBoudTaskOverTime(@Param("overTime") long overTime);


    /**
     * 获取所有超时出库订单 按降序排序
     * @return
     */
    @Select("select CONCAT(CONCAT(\"'\",bill_no),\"'\") from (select TIMESTAMPDIFF(MINUTE,t.create_time,NOW()) overtime,t.* from outbound_task t where t.task_type=1 and t.sfreq=#{sfreq})a \r\n"+
            "WHERE a.overtime>#{overTime} ORDER BY a.overtime")
    List<String> getOutBoudTaskBillNoOverTime(@Param("overTime") long overTime,@Param("sfreq") int sfreq);

     @Update("update outbound_task set task_state=1 where bill_no in (#{bill_no_string})")
     int updateOutBoundTaskBySQL(@Param("bill_no_string") String bill_no_string);

}
