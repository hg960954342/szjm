package com.prolog.eis.dao.pick;

import com.prolog.eis.model.wms.WmsEisIdempotent;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EisIdempotentMapper {


    @Select("select * from wms_eis_idempotent where message_id = #{messageId}")
    List<WmsEisIdempotent> queryRejsonById(String messageId);

    @Insert("insert into wms_eis_idempotent (message_id,loc_date,rejson) values (#{messageId},#{locDate},#{rejson})")
    void insertReport(WmsEisIdempotent wmsEisIdempotent);
}
