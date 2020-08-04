package com.prolog.eis.dao.base;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface BasePagerMapper {

	@Select("select\r\n" + 
			"	${columns}\r\n" + 
			"\r\n" + 
			"from\r\n" + 
			"	(\r\n" + 
			"		select ${columns}\r\n" + 
			"	from\r\n" + 
			"		${tableName}\r\n" + 
			"	where\r\n" + 
			"		1 = 1 ${conditions} ${orders} ) t1\r\n" + 
			"limit #{startRowNum},#{endRowNum}")
 	public List<Map<String, Object>> getPager(@Param("columns") String columns, @Param("tableName") String tableName,
			@Param("conditions") String conditions, @Param("orders") String orders,
			@Param("startRowNum") int startRowNum, @Param("endRowNum") int endRowNum);

	@Select("select count(*) count from ${tableName} where 1=1 ${conditions}")
	public int getToalCount(@Param ("tableName") String tableName,@Param ("conditions") String conditions);
}
