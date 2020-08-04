package com.prolog.eis.dao;

import com.prolog.eis.model.wms.InboundTask;
import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OutBoundTaskMapper extends BaseMapper<OutboundTask>{





    @Select("select t.* from outbound_task t\r\n" +
            "\r\n" +
            "order by t.create_time desc")
     List<OutboundTask> getListOutboundTask();






}
