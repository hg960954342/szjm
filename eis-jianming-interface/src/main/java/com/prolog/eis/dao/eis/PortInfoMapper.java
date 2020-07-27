package com.prolog.eis.dao.eis;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.prolog.eis.model.eis.PortInfo;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface PortInfoMapper extends BaseMapper<PortInfo>{
	@Results({
		@Result(property = "id",  column = "id"),
		@Result(property = "portType",  column = "port_type"),
		@Result(property = "position",  column = "position"),
		@Result(property = "taskType",  column = "task_type"),
		@Result(property = "area",  column = "area"),
		@Result(property = "wmsPortNo",  column = "wms_port_no"),
		@Result(property = "junctionPort",  column = "junction_port"),
		@Result(property = "layer",  column = "layer"),
		@Result(property = "x",  column = "x"),
		@Result(property = "y",  column = "y"),
		@Result(property = "portlock",  column = "port_lock"),
		@Result(property = "taskLock",  column = "task_lock"),
		@Result(property = "maxCkCount",  column = "max_ck_count"),
		@Result(property = "maxRkCount",  column = "max_rk_count"),
		@Result(property = "remarks",  column = "remarks"),
		@Result(property = "ledIp",  column = "led_ip"),
		@Result(property = "errorPort",  column = "error_port")
	})
	@Select("select t.* from port_info t left join stations_port_configure c on t.id = c.port_id left join stations_info s on c.stations_id = s.id where t.area = 2 and t.port_type = 1 and t.work_type = 2 and s.wms_station_no = #{stations} and t.task_type in (${taskType}) order by t.task_type desc")
	public List<PortInfo> getInboundPortInfoByStationsAndTaskType(@Param("stations") String stations, @Param("taskType") String taskType);
}
