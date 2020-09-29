package com.prolog.eis.dao.base;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.ResultHandler;

import java.util.Map;

public interface BasePagerAndResultHanlerMapper {

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
	@ResultType(Map.class)
	@Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000)
	public void getPager(@Param("columns") String columns, @Param("tableName") String tableName,
                                              @Param("conditions") String conditions, @Param("orders") String orders,
                                              @Param("startRowNum") int startRowNum, @Param("endRowNum") int endRowNum, ResultHandler resultHanler);

	@Select("select count(*) count from ${tableName} where 1=1 ${conditions}")
	public int getToalCount(@Param("tableName") String tableName, @Param("conditions") String conditions);
}
