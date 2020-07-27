package com.prolog.eis.dao.baseinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.prolog.eis.dto.eis.PortInfoDto;
import com.prolog.eis.dto.eis.PortTaskCount;
import com.prolog.eis.model.eis.PortInfo;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface PortInfoMapper extends BaseMapper<PortInfo>{

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
	@Select("select t.* from port_info t\r\n" + 
			"left join sx_connection_rim sx on t.junction_port = sx.entry_code\r\n" + 
			"where sx.layer = #{layer} and sx.x = #{x} and sx.y = #{y}")
	List<PortInfo> getWmsPortByPosition(@Param("layer")int layer,@Param("x")int x,@Param("y")int y);
	
	@Select("select portT.port_no portNo,COUNT(*) taskCount from\r\n" + 
			"(\r\n" + 
			"select t.port_no from wms_outbound_task t where t.finished < 90 and t.finished > 0\r\n" + 
			"UNION ALL\r\n" + 
			"select tt.end_port from through_task tt where tt.finished < 90 and tt.finished > 0\r\n" + 
			"UNION ALL\r\n" + 
			"select zt.port_no from zt_ckcontainer zt\r\n" + 
			") portT\r\n" + 
			"where portT.port_no is not null\r\n" + 
			"group by portT.port_no")
	List<PortTaskCount> getPortCkTaskCount();
	
	@Update("update port_info t\r\n" + 
			"set t.task_lock = 2\r\n" + 
			"where t.layer = #{layer} and t.x = #{x} and t.y = #{y}")
	void updatePortInfoUnlock(@Param("layer")int layer,@Param("x")int x,@Param("y")int y);
	
	@Update("update port_info t\r\n" + 
			"set t.task_lock = 1\r\n" + 
			"where t.layer = #{layer} and t.x = #{x} and t.y = #{y}")
	void updatePortInfoLock(@Param("layer")int layer,@Param("x")int x,@Param("y")int y);
	
//	@Results({
//		@Result(property = "id",  column = "id"),
//		@Result(property = "portType",  column = "port_type"),
//		@Result(property = "taskType",  column = "task_type"),
//		@Result(property = "workType",  column = "work_type"),
//		@Result(property = "reback",  column = "reback"),
//		@Result(property = "showLed",  column = "show_led"),
//		@Result(property = "dirMode",  column = "dir_mode"),
//		@Result(property = "position",  column = "position"),
//		@Result(property = "area",  column = "area"),
//		@Result(property = "wmsPortNo",  column = "wms_port_no"),
//		@Result(property = "junctionPort",  column = "junction_port"),
//		@Result(property = "layer",  column = "layer"),
//		@Result(property = "x",  column = "x"),
//		@Result(property = "y",  column = "y"),
//		@Result(property = "portlock",  column = "port_lock"),
//		@Result(property = "taskLock",  column = "task_lock"),
//		@Result(property = "maxCkCount",  column = "max_ck_count"),
//		@Result(property = "maxRkCount",  column = "max_rk_count"),
//		@Result(property = "remarks",  column = "remarks"),
//		@Result(property = "ledIp",  column = "led_ip"),
//		@Result(property = "errorPort",  column = "error_port")
//	})
//	@Select("select * \r\n" + 
//			"from port_info t\r\n" + 
//			"where t.area = 2 and (t.port_type = 3 or (t.task_type = 4 and t.port_type = 1)) and t.layer = #{layer} and t.x = #{x} and t.y = #{y}")
//	List<PortInfo> getEmptyCasePort(@Param("layer")int layer,@Param("x")int x,@Param("y")int y);

	/*@Select("select p.* \r\n" + 
			"from port_info p\r\n" + 
			"left join stations_port_configure pc on p.id = pc.port_id\r\n" + 
			"left join stations_info s on s.id = pc.stations_id\r\n" + 
			"where p.port_type = 4 and p.area = 2 and p.task_lock = 2")
	List<PortInfo> getIqTaskPort();*/
	
	@Select("select s.wms_station_no\r\n" + 
			"from port_info p\r\n" + 
			"left join stations_port_configure spc on p.id = spc.port_id\r\n" + 
			"left join stations_info s on spc.stations_id = s.id\r\n" + 
			"where (p.port_type = 1 or p.port_type = 3) and p.task_type = 4 and p.area = 2")
	List<String> getIqcStations();
	
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
	@Select("select p.*\r\n" + 
			"from stations_info s\r\n" + 
			"left join stations_port_configure spc on s.id = spc.stations_id \r\n" + 
			"left join port_info p on spc.port_id = p.id\r\n" + 
			"where p.area = 2 and p.port_lock = 2 and s.wms_station_no = #{station}")
	List<PortInfoDto> getSxkStationPort(@Param("station")String station);
}
