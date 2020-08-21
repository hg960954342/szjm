package com.prolog.eis.dao.led;

import com.prolog.eis.model.led.LedShow;
import org.apache.ibatis.annotations.Select;

public interface LedShowMapper {

    @Select("select * from led_port where id = #{id}")
    LedShow queryByIp(int id);
}
