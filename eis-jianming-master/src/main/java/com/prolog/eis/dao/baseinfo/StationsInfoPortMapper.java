package com.prolog.eis.dao.baseinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.prolog.eis.model.eis.PortInfo;
import com.prolog.eis.model.eis.StationsPortConfigure;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface StationsInfoPortMapper extends BaseMapper<StationsPortConfigure>{

	@Results({
		@Result(property = "id",  column = "id"),
		@Result(property = "portType",  column = "port_type"),
		@Result(property = "wmsPortNo",  column = "wms_port_no"),
		@Result(property = "junctionPort",  column = "junction_port"),
		@Result(property = "layer",  column = "layer"),
		@Result(property = "x",  column = "x"),
		@Result(property = "y",  column = "y"),
		@Result(property = "portlock",  column = "portlock"),
		@Result(property = "remarks",  column = "remarks")
	})
	@Select("select p.* \r\n" + 
			"from port_info p\r\n" + 
			"where p.id not in\r\n" + 
			"(\r\n" + 
			"	select stp.port_id\r\n" + 
			"	from stations_port_configure stp\r\n" + 
			"	where stp.stations_id = #{stationId}\r\n" + 
			")")
	List<PortInfo> getOtherPortInfo(@Param("stationId")int stationId);
}
