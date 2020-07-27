package com.prolog.eis.dao.eis;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.prolog.eis.model.eis.WmsCallCarTaskHistory;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface WmsCallCarTaskHistoryMapper extends BaseMapper<WmsCallCarTaskHistory>{
	@Insert("insert into wms_callcar_task_history select * from wms_callcar_task\n" +
            "t where FIND_IN_SET(t.id,#{ids})")
	void backupById(@Param("ids") String ids);
}
