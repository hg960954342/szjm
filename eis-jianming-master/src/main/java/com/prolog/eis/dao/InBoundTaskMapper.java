package com.prolog.eis.dao;

import com.prolog.eis.model.wms.InboundTask;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 入库操作
 */
public interface InBoundTaskMapper  extends BaseMapper<InboundTask>{





    @Select("select t.* from inbound_task t\r\n" +
            "\r\n" +
            "order by t.create_time desc")
     List<InboundTask> getListInboundTask();






}
