package com.prolog.eis.dao;

import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

public interface AgvStorageLocationMapper extends BaseMapper<AgvStorageLocation> {
    @Select("select * from agv_storagelocation where ceng=#{layer} and x=#{x] and y=#{y}")
    AgvStorageLocation findByCoord(int layer, int x, int y);
}
