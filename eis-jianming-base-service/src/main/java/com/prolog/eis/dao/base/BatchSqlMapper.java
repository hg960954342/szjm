package com.prolog.eis.dao.base;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

public interface BatchSqlMapper {

	 @InsertProvider(type = PrologSqlProvider.class,method = "insertSql")
	 public int pubInsertSql(@Param(value="list") List<Object> list,@Param(value="tableName") String tableName);

	 @UpdateProvider(type = PrologSqlProvider.class,method = "updateSql")
	 public void pubUpdateSql(@Param(value="list") List<Object> list,@Param(value="tableName") String tableName); 
 
  }
