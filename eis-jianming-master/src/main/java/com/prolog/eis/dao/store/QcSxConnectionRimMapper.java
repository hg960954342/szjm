package com.prolog.eis.dao.store;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.prolog.eis.model.store.SxConnectionRim;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface QcSxConnectionRimMapper extends BaseMapper<SxConnectionRim>{

	@Results({
		@Result(property = "id",  column = "ID"),
		@Result(property = "sxHoisterId",  column = "sx_hoister_id"),
		@Result(property = "lineState",  column = "line_state"),
		@Result(property = "entryCode",  column = "entry_code"),
		@Result(property = "wmsEntryCode",  column = "wms_entry_code"),
		@Result(property = "entryName",  column = "entry_name"),
		@Result(property = "entryType",  column = "entry_type"),
		@Result(property = "section",  column = "section"),
		@Result(property = "routeCode",  column = "route_code"),
		@Result(property = "routeName",  column = "route_name"),
		@Result(property = "status",  column = "status"),
		@Result(property = "layer",  column = "layer"),
		@Result(property = "x",  column = "x"),
		@Result(property = "y",  column = "y"),
		@Result(property = "lineReadOnly",  column = "line_read_only"),
		@Result(property = "createTime",  column = "create_time"),
		@Result(property = "lastUpdateTime",  column = "last_update_time"),
		@Result(property = "cacheCount",  column = "cache_count")
	})
	@Select("select t.* from sx_connection_rim t\r\n" + 
			"where t.layer = #{layer} and t.x = #{x} and t.y = #{y} and t.entry_code like 'T%' and t.entry_type = 10")
	List<SxConnectionRim> findRealHoisterJunctionByPosition(@Param("layer")int layer,@Param("x")int x,@Param("y")int y);
}
