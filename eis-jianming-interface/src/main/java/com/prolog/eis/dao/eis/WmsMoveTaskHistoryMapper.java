package com.prolog.eis.dao.eis;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.prolog.eis.model.eis.WmsMoveTaskHistory;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface WmsMoveTaskHistoryMapper extends BaseMapper<WmsMoveTaskHistory> {
	
	@Insert("insert into wms_move_task_history select * from wms_move_task\n" +
            "t where FIND_IN_SET(t.id,#{ids})")
	void backupById(@Param("ids") String ids);
	
}
