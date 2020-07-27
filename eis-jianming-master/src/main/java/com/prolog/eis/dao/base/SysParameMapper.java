package com.prolog.eis.dao.base;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.prolog.eis.model.base.SysParame;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface SysParameMapper extends BaseMapper<SysParame>{

	@Select("select t.parame_no parameNo,t.parame_value parameValue,t.parame_type parameType,t.is_read_only isReadOnly,t.visibility visibility,t.default_value defaultValue,t.remark remark,t.sortindex sortindex\r\n" + 
			"from sys_parame t\r\n" + 
			"where t.visibility = 1\r\n" + 
			"order by t.sortindex")
	List<SysParame> getSysParames();
}
