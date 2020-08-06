package com.prolog.eis.dao;

import com.prolog.eis.model.wms.InboundTask;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

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
            @Result(property = "end_time",  column = "end_time")
    })
    @Select("select t.* from inbound_task t\r\n" +
            "\r\n" +
            "order by t.create_time desc")
     List<InboundTask> getListInboundTask();

    @ResultMap("inboundTask")
    @Select("select * from inbound_task where container_code=#{containerCode}")
    InboundTask getReportData(String containerCode);
}
