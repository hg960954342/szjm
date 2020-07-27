package com.prolog.eis.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.prolog.eis.model.eis.ThroughTask;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface ThroughTaskMapper extends BaseMapper<ThroughTask> {

	@Results({
		@Result(property = "id",  column = "id"),
		@Result(property = "palletId",  column = "pallet_id"),
		@Result(property = "containerCode",  column = "container_code"),
		@Result(property = "materielNo",  column = "materiel_no"),
		@Result(property = "factoryNo",  column = "factory_no"),
		@Result(property = "materielType",  column = "materiel_type"),
		@Result(property = "materielName",  column = "materiel_name"),
		@Result(property = "startStations",  column = "start_stations"),
		@Result(property = "startPort",  column = "start_port"),
		@Result(property = "endStations",  column = "end_stations"),
		@Result(property = "endPort",  column = "end_port"),
		@Result(property = "finished",  column = "finished"),
		@Result(property = "errMsg",  column = "err_msg"),
		@Result(property = "createTime",  column = "create_time"),
		@Result(property = "wmsDatasourceType",  column = "wms_datasource_type")
	})
	@Select("select * from through_task t where t.pallet_id = #{containerSubNo} and t.finished >= 0 and t.finished < 10")
	ThroughTask getRkNoStartThroughTask(@Param("containerSubNo")String containerSubNo);
	
	@Results({
		@Result(property = "id",  column = "id"),
		@Result(property = "palletId",  column = "pallet_id"),
		@Result(property = "containerCode",  column = "container_code"),
		@Result(property = "materielNo",  column = "materiel_no"),
		@Result(property = "factoryNo",  column = "factory_no"),
		@Result(property = "materielType",  column = "materiel_type"),
		@Result(property = "materielName",  column = "materiel_name"),
		@Result(property = "startStations",  column = "start_stations"),
		@Result(property = "startPort",  column = "start_port"),
		@Result(property = "endStations",  column = "end_stations"),
		@Result(property = "endPort",  column = "end_port"),
		@Result(property = "finished",  column = "finished"),
		@Result(property = "errMsg",  column = "err_msg"),
		@Result(property = "createTime",  column = "create_time"),
		@Result(property = "wmsDatasourceType",  column = "wms_datasource_type")
	})
	@Select("select * from through_task t where t.container_code = #{containerNo} and t.finished >= 0 and t.finished < 10")
	ThroughTask getRkNoStartParentThroughTask(@Param("containerNo")String containerNo);
}
