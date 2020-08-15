package com.prolog.eis.dao;

import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

public interface AgvStorageLocationMapper extends BaseMapper<AgvStorageLocation> {
    @Results(id="AgvStorageLocation" , value= {
            @Result(property = "ceng",  column = "ceng"),
            @Result(property = "x",  column = "x"),
            @Result(property = "y",  column = "y"),
            @Result(property = "locationType",  column = "location_type"),
            @Result(property = "rcsPositionCode",  column = "rcs_position_code"),
            @Result(property = "tallyCode",  column = "tally_code"),
            @Result(property = "taskLock",  column = "task_lock"),
            @Result(property = "locationLock",  column = "location_lock"),
            @Result(property = "deviceNo",  column = "device_no")    })
    @Select("select * from agv_storagelocation where ceng=#{layer} and x=#{x] and y=#{y}")
    AgvStorageLocation findByCoord(int layer, int x, int y);

    @ResultMap(value = "AgvStorageLocation")
    @Select("select * from agv_storagelocation where rcs_position_code=#{location}")
    AgvStorageLocation findByRcs(String location);

    @ResultMap(value = "AgvStorageLocation")
    @Select("select * from agv_storagelocation  WHERE device_no=#{deviceCode} and task_lock=#{taskLock} and location_lock=#{lock} limit 1 ")
    AgvStorageLocation findByPickCodeAndLock(@Param("deviceCode") String deviceCode, @Param("lock") int lock,@Param("taskLock") int taskLock);


}
