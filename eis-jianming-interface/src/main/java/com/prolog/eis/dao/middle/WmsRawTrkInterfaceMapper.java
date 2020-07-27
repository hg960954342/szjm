package com.prolog.eis.dao.middle;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.prolog.eis.model.middle.WmsRawTrkInterface;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface WmsRawTrkInterfaceMapper extends BaseMapper<WmsRawTrkInterface> {
	@Results({ @Result(property = "trkDt", column = "trk_dt"),
			@Result(property = "groupId", column = "group_id"),
			@Result(property = "commandNo", column = "command_no"),
			@Result(property = "whNo", column = "wh_no"),
			@Result(property = "areaNo", column = "area_no"),
			@Result(property = "devNo", column = "dev_no"),
			@Result(property = "binNo", column = "bin_no"),
			@Result(property = "emerge", column = "emerge"),
			@Result(property = "io", column = "io"),
			@Result(property = "wmsBack", column = "wms_back"),
			@Result(property = "wmsAction", column = "wms_action"),
			@Result(property = "wmsSerialNo", column = "wms_serial_no"),
			@Result(property = "wmsUser", column = "wms_user"),
			@Result(property = "wmsFlag", column = "wms_flag"),
			@Result(property = "palletId", column = "pallet_id"),
			@Result(property = "palletSize", column = "pallet_size"),
			@Result(property = "weight", column = "weight"),
			@Result(property = "status", column = "status"),
			@Result(property = "finished", column = "finished"),
			@Result(property = "errCode", column = "err_code"),
			@Result(property = "errMsg", column = "err_msg"),
			@Result(property = "wmsErrCode", column = "wms_err_code"),
			@Result(property = "wmsErrMsg", column = "wms_err_msg"),
			@Result(property = "productId", column = "product_id"),
			@Result(property = "boxCount", column = "box_count"),
			@Result(property = "port", column = "port"),
			@Result(property = "matType", column = "mat_type"),
			@Result(property = "stations", column = "stations"),
			@Result(property = "taskType", column = "task_type"),
			@Result(property = "vendorCode", column = "vendor_code") })
	@Select("select t.* from WMS_RAW_TRK_INTERFACE t where t.status = 0")
	List<WmsRawTrkInterface> getUnsyncData();
	
	@Update("update WMS_RAW_TRK_INTERFACE set status = 1 where command_no = #{commandNo}")
	long hasSync(@Param("commandNo")String commandNo);
	
	@Update("update WMS_RAW_TRK_INTERFACE set status = 2,err_code = 100,err_msg = #{errMsg,jdbcType=VARCHAR} where command_no = #{commandNo}")
	long hasErrorSync(@Param("commandNo")String commandNo,@Param("errMsg")String errMsg);

	@Update("update WMS_RAW_TRK_INTERFACE set bin_no = #{binNo,jdbcType=VARCHAR}, weight = #{weight,jdbcType=DOUBLE}, finished = #{finished,jdbcType=INTEGER} where command_no = #{commandNo}")
	long noticeInboundFinished(@Param("binNo")String binNo, @Param("weight")Double weight, @Param("finished")int finished, @Param("commandNo")String commandNo);

	@Update("update WMS_RAW_TRK_INTERFACE set finished = #{finished,jdbcType=INTEGER}, err_code = 92 where command_no = #{commandNo}")
	long forceFinish(@Param("finished")int finished, @Param("commandNo")String commandNo);
	
	@Update("update WMS_RAW_TRK_INTERFACE set finished = #{finished,jdbcType=INTEGER} where command_no = #{commandNo}")
	long noticeInboundFinishedState(@Param("finished")int finished, @Param("commandNo")String commandNo);
	
	@Update("update WMS_RAW_TRK_INTERFACE set port = #{port,jdbcType=VARCHAR}, finished = #{finished,jdbcType=INTEGER} where command_no = #{commandNo}")
	long noticeInboundPort(@Param("finished")int finished, @Param("port")String port, @Param("commandNo")String commandNo);
	
	@Update("update WMS_RAW_TRK_INTERFACE set bin_no = #{binNo,jdbcType=VARCHAR} where command_no = #{commandNo}")
	long noticeInboundBin(@Param("binNo")String binNo, @Param("commandNo")String commandNo);
	
	@Update("update WMS_RAW_TRK_INTERFACE t\r\n" + 
			"set t.port = #{port,jdbcType=VARCHAR}\r\n" + 
			"where t.command_no = #{commandNo}")
	long noticeInHoistFinished(@Param("port")String port,@Param("commandNo")String commandNo);

	@Update("update WMS_RAW_TRK_INTERFACE set port = #{portNo,jdbcType=VARCHAR}, finished = #{finished,jdbcType=INTEGER} where command_no = #{commandNo}")
	void noticeOutboundFinished(@Param("portNo")String portNo, @Param("finished")int finished, @Param("commandNo")String commandNo);
	
	@Insert("<script>insert into wms_raw_trk_interface(trk_dt, group_id, command_no, wh_no, area_no, dev_no, bin_no, emerge, io, wms_back, wms_action, wms_serial_no, wms_user, wms_flag, pallet_id, pallet_size, weight, status, finished, err_code, err_msg, wms_err_code, wms_err_msg, product_id, box_count, port, mat_type, stations, task_type, vendor_code) " +
            "VALUES " +
            "<foreach collection = \"list\" separator = \",\" item = \"item\">\n" +
            "     (   #{item.trkDt} ,\n" +
            "         #{item.groupId} ,\n" +
            "         #{item.commandNo} ,\n" +
            "         #{item.whNo} ,\n" +
            "         #{item.areaNo} ,\n" +
            "         #{item.devNo} ,\n" +
            "         #{item.binNo} ,\n" +
            "         #{item.emerge} ,\n" +
            "         #{item.io} ,\n" +
            "         #{item.wmsBack} ,\n" +
            "         #{item.wmsAction} ,\n" +
            "         #{item.wmsSerialNo} ,\n" +
            "         #{item.wmsUser} ,\n" +
            "         #{item.wmsFlag} ,\n" +
            "         #{item.palletId} ,\n" +
            "         #{item.palletSize} ,\n" +
            "         #{item.weight} ,\n" +
            "         #{item.status} ,\n" +
            "         #{item.finished} ,\n" +
            "         #{item.errCode} ,\n" +
            "         #{item.errMsg} ,\n" +
            "         #{item.wmsErrCode} ,\n" +
            "         #{item.wmsErrMsg} ,\n" +
            "         #{item.productId} ,\n" +
            "         #{item.boxCount} ,\n" +
            "         #{item.port} ,\n" +
            "         #{item.matType} ,\n" +
            "         #{item.stations} ,\n" +
            "         #{item.taskType} ,\n" +
            "         #{item.vendorCode} )\n" +
            "  </foreach></script>")
    long saveBatch(List<WmsRawTrkInterface> list);
}
