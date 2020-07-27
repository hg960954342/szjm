package com.prolog.eis.dao.base;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface BasePagerMapper {

	/**
	 * 通用分页查询
	 * 
	 * @author huhao
	 * @date 2018.09.27 Pm 2:40
	 */
	// oracle
//	@Select("select  ${columns} from (  select t1.*,limit r from (select  ${columns} from  ${tableName}  where 1=1 ${conditions}   ${orders}  ) t1 where  limit <= #{endRowNum} ) t  where t.r >=  #{startRowNum}")
// 	public List<Map<String, Object>> getPager(@Param("columns") String columns, @Param("tableName") String tableName,
//			@Param("conditions") String conditions, @Param("orders") String orders,
//			@Param("startRowNum") int startRowNum, @Param("endRowNum") int endRowNum);
	
	// mysql
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
