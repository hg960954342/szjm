package com.prolog.eis.dao.eis;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.prolog.eis.dto.eis.InboundTaskPortDto;
import com.prolog.eis.model.eis.WmsInboundTask;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface WmsInboundTaskMapper extends BaseMapper<WmsInboundTask> {
	
	@Insert("<script>insert into wms_inbound_task(command_no, stations, wms_push, wh_no, area_no, task_type, pallet_id, pallet_size, materiel_no, factory_no, materiel_type, materiel_name, mat_type, weight, finished, bin_no, err_msg, create_time, wms_datasource_type) " +
            "VALUES " +
            "<foreach collection = \"list\" separator = \",\" item = \"item\">\n" +
            "     (   #{item.commandNo} ,\n" +
            "         #{item.stations} ,\n" +
            "         #{item.wmsPush} ,\n" +
            "         #{item.whNo} ,\n" +
            "         #{item.areaNo} ,\n" +
            "         #{item.taskType} ,\n" +
            "         #{item.palletId} ,\n" +
            "         #{item.palletSize} ,\n" +
            "         #{item.materielNo} ,\n" +
            "         #{item.factoryNo} ,\n" +
            "         #{item.materielType} ,\n" +
            "         #{item.materielName} ,\n" +
            "         #{item.matType} ,\n" +
            "         #{item.weight} ,\n" +
            "         #{item.finished} ,\n" +
            "         #{item.binNo} ,\n" +
            "         #{item.errMsg} ,\n" +
            "         #{item.createTime}, \n" +
            "         #{item.wmsDatasourceType} )\n" +
            "  </foreach></script>")
    long saveBatch(List<WmsInboundTask> list);
	
	@Results({ @Result(property = "id", column = "id"),
		@Result(property = "commandNo", column = "command_no"),
		@Result(property = "wmsPush", column = "wms_push"),
		@Result(property = "whNo", column = "wh_no"),
		@Result(property = "areaNo", column = "area_no"),
		@Result(property = "taskType", column = "task_type"),
		@Result(property = "palletId", column = "pallet_id"),
		@Result(property = "containerCode", column = "container_code"),
		@Result(property = "palletSize", column = "pallet_size"),
		@Result(property = "materielNo", column = "materiel_no"),
		@Result(property = "factoryNo", column = "factory_no"),
		@Result(property = "materielType", column = "materiel_type"),
		@Result(property = "materielName", column = "materiel_name"),
		@Result(property = "matType", column = "mat_type"),
		@Result(property = "weight", column = "weight"),
		@Result(property = "finished", column = "finished"),
		@Result(property = "stations", column = "stations"),
		@Result(property = "portNo", column = "port_no"),
		@Result(property = "report", column = "report"),
		@Result(property = "binNo", column = "bin_no"),
		@Result(property = "errMsg", column = "err_msg"),
		@Result(property = "createTime", column = "create_time"),
		@Result(property = "wmsDatasourceType", column = "wms_datasource_type")
	})
	@Select("select t.* from wms_inbound_task t ")
	List<WmsInboundTask> getTaskProgressData();
	
	@Select("select p.wms_port_no wmsPortNo,t.id taskId,t.task_type taskType,t.port_no portNo,t.finished\r\n" + 
			"from port_info p\r\n" + 
			"left join stations_port_configure sp on p.id = sp.port_id\r\n" + 
			"left join stations_info s on sp.stations_id = s.id\r\n" + 
			"left join wms_inbound_task t on s.wms_station_no = t.stations and t.finished = 0 and ((p.task_type = 3 and t.task_type = 40) or (p.task_type = 4 and t.task_type = 30) or (p.task_type = 2 and t.task_type = 10))\r\n" + 
			"where (p.task_type = 2 or p.task_type = 3 or p.task_type = 4) and p.port_type = 1 and (t.finished = 0 or t.finished is null)\r\n" + 
			"order by t.create_time")
	List<InboundTaskPortDto> getInboundTaskPortData();

	@Select("select count(*) from stacker_store s where s.box_no = #{palletId}")
	int getStackerStoreByBoxNo(@Param("palletId") String palletId);

	@Select("select count(*) from sx_store t where t.CONTAINER_SUB_NO = #{palletId}")
	int getSxkStoreByBoxNo(@Param("palletId") String palletId);

	@Select("select s.wms_station_no from port_info p left join stations_port_configure spc on p.id = spc.port_id left join stations_info s on spc.stations_id = s.id where p.port_type = 4 and p.area = 2")
	List<String> getQcStations();
}
