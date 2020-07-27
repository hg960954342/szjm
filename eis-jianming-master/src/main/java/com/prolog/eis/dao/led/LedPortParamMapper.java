package com.prolog.eis.dao.led;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.prolog.eis.dto.eis.led.LedPortParamDto;
import com.prolog.eis.model.eis.led.LedPortParam;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface LedPortParamMapper extends BaseMapper<LedPortParam>{
	@Select("select t.led_ip as ledIp, p.led_title as ledTitle, p.uda0, p.uda1, p.uda2, p.uda3, p.uda4 from port_info t left join led_port_param p on t.id = p.port_id where t.led_ip is not null and t.led_ip != ''")
	List<LedPortParamDto> getLedPortParam();
	
	@Select("select t.led_ip as ledIp, p.led_title as ledTitle, p.uda0, p.uda1, p.uda2, p.uda3, p.uda4 from port_info t left join led_port_param p on t.id = p.port_id where t.led_ip = ${ledIp}")
	LedPortParamDto getLedPortParamByLedIp(@Param("ledIp") String ledIp);
}
