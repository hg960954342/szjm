package com.prolog.eis.dao.eis;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.prolog.eis.model.eis.WmsInboundTaskHistory;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface WmsInboundTaskHistoryMapper extends BaseMapper<WmsInboundTaskHistory> {
	
	@Insert("insert into wms_inbound_task_history select * from wms_inbound_task\n" +
            "t where FIND_IN_SET(t.id,#{ids})")
	void backupById(@Param("ids") String ids);
	
}
