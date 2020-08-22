package com.prolog.eis.dao;

import com.prolog.eis.model.wms.InboundTask;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 入库操作
 */
public interface InBoundTaskMapper  extends BaseMapper<InboundTask>{




    @Results(id="inboundTask",value = {
            @Result(property = "id",  column = "id"),
            @Result(property = "billNo",  column = "bill_no"),
            @Result(property = "wmsPush",  column = "wms_push"),
            @Result(property = "reBack",  column = "reback") ,
            @Result(property = "containerCode",  column = "container_code") ,
            @Result(property = "emptyContainer",  column = "empty_container") ,
            @Result(property = "taskType",  column = "task_type") ,
            @Result(property = "itemId",  column = "item_id") ,
            @Result(property = "qty",  column = "qty") ,
            @Result(property = "lotId",  column = "lot_id") ,
            @Result(property = "ceng",  column = "ceng") ,
            @Result(property = "agvLoc",  column = "agv_loc"),
            @Result(property = "ownerId",  column = "owner_id"),
            @Result(property = "taskState",  column = "task_state"),
            @Result(property = "createTime",  column = "create_time"),
            @Result(property = "startTime",  column = "start_time"),
            @Result(property = "rukuTime",  column = "ruku_time"),
            @Result(property = "endTime",  column = "end_time")
    })
    @Select("select t.* from inbound_task t where t.task_state=0\r\n" +
            "\r\n" +
            "order by t.create_time desc")
     List<InboundTask> getListInboundTask();

   /* @Update("update inbound_task set bill_no=#{inboundTask.billNo},wms_push=#{inboundTask.wmsPush},reback=#{inboundTask.reBack},container_code=#{inboundTask.containerCode}," +
            "empty_container=#{inboundTask.emptyContainer},task_type=#{inboundTask.taskType},item_id=#{inboundTask.itemId},qty=#{inboundTask.qty},lot_id=#{inboundTask.lotId},ceng=#{inboundTask.ceng}," +
            "agv_loc=#{inboundTask.agvLoc},owner_id=#{inboundTask.ownerId},task_state=#{inboundTask.taskState},create_time=#{inboundTask.createTime},start_time=#{inboundTask.startTime},ruku_time=#{inboundTask.rukuTime},end_time=#{inboundTask.endTime} where id= #{inboundTask.id}")
    long update(@Param("inboundTask")InboundTask inboundTask);*/

    @ResultMap("inboundTask")
    @Select("select * from inbound_task where container_code=#{containerCode}")
    InboundTask getReportData(String containerCode);
}
