package com.prolog.eis.dao.wms;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.prolog.eis.model.wms.InboundTask;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface InboundTaskMapper extends BaseMapper<InboundTask>{

	@Results({
		@Result(property = "id",  column = "id"),
		@Result(property = "billNo",  column = "bill_no"),
		@Result(property = "containerCode",  column = "container_code")
	})
	@Select("select t.id,t.bill_no,t.container_code from inbound_task t where t.task_state >= 1 and t.task_state < 4 and t.container_code = #{containerNo}")
	List<InboundTask> getRkStartInboundTask(@Param("containerNo")String containerNo);
}
