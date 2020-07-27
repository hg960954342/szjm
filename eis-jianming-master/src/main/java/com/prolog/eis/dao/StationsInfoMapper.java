package com.prolog.eis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.prolog.eis.dto.eis.PortInfoDto;
import com.prolog.eis.dto.eis.PortTemsInfoDto;
import com.prolog.eis.model.eis.StationsInfo;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface StationsInfoMapper extends BaseMapper<StationsInfo>{

	@Results({
		@Result(property = "id",  column = "id"),
		@Result(property = "portType",  column = "port_type"),
		@Result(property = "taskType",  column = "task_type"),
		@Result(property = "workType",  column = "work_type"),
		@Result(property = "reback",  column = "reback"),
		@Result(property = "showLed",  column = "show_led"),
		@Result(property = "dirMode",  column = "dir_mode"),
		@Result(property = "position",  column = "position"),
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
	@Select("select p.*,s.wms_station_no wmsStationNo\r\n" + 
			"from stations_port_configure spc\r\n" + 
			"left join stations_info s on s.id = spc.stations_id\r\n" + 
			"left join port_info p on spc.port_id = p.id\r\n" + 
			"where ${stationStr} \r\n" +
			"order by s.sort_index")
	List<PortInfoDto> getAllStationPorts(@Param("stationStr")String stationStr);
	
	@Results({
		@Result(property = "id",  column = "id"),
		@Result(property = "portType",  column = "port_type"),
		@Result(property = "taskType",  column = "task_type"),
		@Result(property = "workType",  column = "work_type"),
		@Result(property = "reback",  column = "reback"),
		@Result(property = "showLed",  column = "show_led"),
		@Result(property = "dirMode",  column = "dir_mode"),
		@Result(property = "position",  column = "position"),
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
	@Select("select p.*,s.wms_station_no wmsStationNo\r\n" + 
			"from stations_port_configure spc\r\n" + 
			"left join stations_info s on s.id = spc.stations_id\r\n" + 
			"left join port_info p on spc.port_id = p.id\r\n" + 
			"where p.area = 2 and p.port_lock = 2 and ${stationStr} \r\n" +
			"order by s.sort_index")
	List<PortInfoDto> getStationPorts(@Param("stationStr")String stationStr);
	
	@Results({
		@Result(property = "id",  column = "id"),
		@Result(property = "portInfoId",  column = "port_info_id"),
		@Result(property = "portType",  column = "port_type"),
		@Result(property = "taskType",  column = "task_type"),
		@Result(property = "junctionPort",  column = "junction_port"),
		@Result(property = "layer",  column = "layer"),
		@Result(property = "x",  column = "x"),
		@Result(property = "y",  column = "y"),
		@Result(property = "portLock",  column = "port_lock"),
		@Result(property = "taskLock",  column = "task_lock"),
		@Result(property = "remarks",  column = "remarks")
	})
	@Select("select pt.*,s.wms_station_no wmsStationNo\r\n" + 
			"from stations_info s\r\n" + 
			"left join stations_port_configure spc on s.id = spc.stations_id \r\n" + 
			"left join port_info p on spc.port_id = p.id\r\n" + 
			"left join port_tems_info pt on p.id = pt.port_info_id\r\n" + 
			"where p.area = 2 and pt.task_lock = 2 and ${stationStr} \r\n" +
			"order by s.sort_index")
	List<PortTemsInfoDto> getStationPortTems(@Param("stationStr")String stationStr);
	
	@Select("select distinct s.wms_station_no wmsStationNo,s.remark,s.sort_index sortIndex\r\n" + 
			"from port_info t\r\n" + 
			"left join stations_port_configure sp on t.id = sp.port_id\r\n" + 
			"left join stations_info s on sp.stations_id = s.id\r\n" + 
			"where t.area = 1\r\n" + 
			"order by s.sort_index")
	List<StationsInfo> getLxkStations();
	
	@Select("select distinct s.wms_station_no wmsStationNo,s.remark,s.sort_index sortIndex\r\n" + 
			"from port_info t\r\n" + 
			"left join stations_port_configure sp on t.id = sp.port_id\r\n" + 
			"left join stations_info s on sp.stations_id = s.id\r\n" + 
			"where t.area = 2\r\n" + 
			"order by s.sort_index")
	List<StationsInfo> getSxkStations();
}
