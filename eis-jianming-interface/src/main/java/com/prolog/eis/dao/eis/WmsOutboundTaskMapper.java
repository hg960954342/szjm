package com.prolog.eis.dao.eis;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.prolog.eis.model.eis.WmsOutboundTask;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface WmsOutboundTaskMapper extends BaseMapper<WmsOutboundTask> {
	
	@Insert("<script>insert into wms_outbound_task(group_id,command_no, wms_push, wh_no, area_no, task_type, pallet_id, pallet_size, emerge, outbound_time, stations, port_no, finished, err_msg, create_time, wms_datasource_type) " +
            "VALUES " +
            "<foreach collection = \"list\" separator = \",\" item = \"item\">\n" +
            "     (   #{item.groupId} ,\n" +
            "         #{item.commandNo} ,\n" +
            "         #{item.wmsPush} ,\n" +
            "         #{item.whNo} ,\n" +
            "         #{item.areaNo} ,\n" +
            "         #{item.taskType} ,\n" +
            "         #{item.palletId} ,\n" +
            "         #{item.palletSize} ,\n" +
            "         #{item.emerge} ,\n" +
            "         #{item.outboundTime} ,\n" +
            "         #{item.stations} ,\n" +
            "         #{item.portNo} ,\n" +
            "         #{item.finished} ,\n" +
            "         #{item.errMsg} ,\n" +
            "         #{item.createTime}, \n" +
            "         #{item.wmsDatasourceType} )\n" +
            "  </foreach></script>")
    long saveBatch(List<WmsOutboundTask> list);
	
	@Results({ @Result(property = "id", column = "id"),
		@Result(property = "groupId", column = "group_id"),
		@Result(property = "commandNo", column = "command_no"),
		@Result(property = "wmsPush", column = "wms_push"),
		@Result(property = "whNo", column = "wh_no"),
		@Result(property = "areaNo", column = "area_no"),
		@Result(property = "taskType", column = "task_type"),
		@Result(property = "palletId", column = "pallet_id"),
		@Result(property = "containerCode", column = "container_code"),
		@Result(property = "palletSize", column = "pallet_size"),
		@Result(property = "emerge", column = "emerge"),
		@Result(property = "outboundTime", column = "outbound_time"),
		@Result(property = "stations", column = "stations"),
		@Result(property = "portNo", column = "port_no"),
		@Result(property = "entryCode", column = "entry_code"),
		@Result(property = "finished", column = "finished"),
		@Result(property = "report", column = "report"),
		@Result(property = "errMsg", column = "err_msg"),
		@Result(property = "createTime", column = "create_time"),
		@Result(property = "lxkExit", column = "lxk_exit"),
		@Result(property = "wmsDatasourceType", column = "wms_datasource_type")
	})
	
	@Select("select t.* from wms_outbound_task t where t.finished > 0")
	List<WmsOutboundTask> getTaskProgressData();

	@Select("select count(1) from sx_store t where t.CONTAINER_SUB_NO = #{palletId}")
	int countSxStore(@Param("palletId") String palletId);

	@Select("select count(1) from stacker_store t where t.box_no = #{palletId}")
	int countStackerStore(@Param("palletId") String palletId);
}
