package com.prolog.eis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.prolog.eis.dto.eis.TempPortZtTaskDto;
import com.prolog.eis.model.eis.ZtckContainer;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface ZtckContainerMapper extends BaseMapper<ZtckContainer>{

	@Select("select zt.container_code containerCode,pt.id temJjunctionPortId,pt.junction_port temJunctionPort,pt.layer,pt.x,pt.y,p.id junctionPortId,p.junction_port junctionPort,p.wms_port_no wmsPortNo,p.layer tagetLayer,p.x targetX,p.y targetY\r\n" + 
			"from zt_ckcontainer zt\r\n" + 
			"left join port_tems_info pt on zt.entry_code = pt.junction_port\r\n" + 
			"left join port_info p on pt.port_info_id = p.id\r\n" + 
			"where p.area = 2 and p.task_type = 4 and zt.task_status = 20 and (zt.task_type = 20 or zt.task_type = 10) and p.port_lock = 2 and p.task_lock = 2")
	List<TempPortZtTaskDto> getTempPort();
}
