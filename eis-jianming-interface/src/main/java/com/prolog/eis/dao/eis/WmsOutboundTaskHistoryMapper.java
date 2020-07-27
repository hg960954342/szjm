package com.prolog.eis.dao.eis;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.prolog.eis.model.eis.WmsOutboundTaskHistory;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface WmsOutboundTaskHistoryMapper extends BaseMapper<WmsOutboundTaskHistory> {
	
	@Insert("insert into wms_outbound_task_history select * from wms_outbound_task\n" +
            "t where FIND_IN_SET(t.id,#{ids})")
	void backupById(@Param("ids") String ids);
	
}
