package com.prolog.eis.dao;

import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

public interface AgvStorageLocationMapper extends BaseMapper<AgvStorageLocation> {
    @Results(id="AgvStorageLocation" , value= {
            @Result(property = "ceng",  column = "ceng"),
            @Result(property = "x",  column = "x"),
            @Result(property = "y",  column = "y"),
            @Result(property = "locationType",  column = "location_type"),
            @Result(property = "tallyCode",  column = "tally_code"),
            @Result(property = "taskLock",  column = "task_lock"),
            @Result(property = "lock",  column = "lock"),
            @Result(property = "deviceNo",  column = "device_no")    })
    @Select("select * from agv_storagelocation where ceng=#{layer} and x=#{x] and y=#{y}")
    AgvStorageLocation findByCoord(int layer, int x, int y);
}
