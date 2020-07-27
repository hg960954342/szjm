package com.prolog.eis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.prolog.eis.model.eis.ZtContainerMsg;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface ZtContainerMsgMapper extends BaseMapper<ZtContainerMsg> {

	@Results({
		@Result(property = "id",  column = "id"),
		@Result(property = "containerCode",  column = "container_code"),
		@Result(property = "containerSubCode",  column = "container_subcode"),
		@Result(property = "portNo",  column = "port_no"),
		@Result(property = "entryCode",  column = "entry_code"),
		@Result(property = "errorMsg",  column = "error_msg"),
		@Result(property = "createTime",  column = "create_time")
	})
	@Select("select zt.*\r\n" + 
			"from zt_container_msg zt\r\n" + 
			"left join port_info p on p.wms_port_no = zt.port_no\r\n" + 
			"where p.position = #{position}")
	List<ZtContainerMsg> getZtContainerMsgList(@Param("position")String position);
}
