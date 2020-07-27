package com.prolog.eis.dao.led;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.prolog.eis.dto.eis.led.LedMessageDto;
import com.prolog.eis.model.eis.led.LedMessage;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface LedMessageMapper extends BaseMapper<LedMessage>{
	@Select("SELECT\r\n" + 
			"	m.id AS id,\r\n" + 
			"	p.led_ip AS ledIp,\r\n" + 
			"	m.port_id AS portId,\r\n" + 
			"	m.read_state AS readState,\r\n" + 
			"	m.create_time AS createTime,\r\n" + 
			"	m.state_str AS stateStr,\r\n" + 
			"	m.message_type AS messageType,\r\n" + 
			"	m.message AS message,\r\n" + 
			"	pp.uda0 AS ledSize \r\n" + 
			"FROM\r\n" + 
			"	led_message m\r\n" + 
			"	LEFT JOIN port_info p ON m.port_id = p.id\r\n" + 
			"	LEFT JOIN led_port_param pp ON pp.port_id = p.id \r\n" + 
			"WHERE\r\n" + 
			"	m.read_state = 0 \r\n" + 
			"ORDER BY\r\n" + 
			"	m.create_time DESC")
	List<LedMessageDto> getAllUnreadMessageOrderByCreateTimeDesc();

	@Update("update led_message l set l.read_state = 1 where l.port_id = #{portId} and l.create_time <= #{createTime}")
	void markHasRead(@Param("portId") int portId, @Param("createTime") Date createTime);
}
