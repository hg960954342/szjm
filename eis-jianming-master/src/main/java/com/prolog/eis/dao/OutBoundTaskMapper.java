package com.prolog.eis.dao;

import com.prolog.eis.model.wms.InboundTask;
import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

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
    @Select("select t.* from outbound_task t\r\n" +
            "\r\n" +
            "order by t.create_time desc")
     List<OutboundTask> getListOutboundTask();






}
