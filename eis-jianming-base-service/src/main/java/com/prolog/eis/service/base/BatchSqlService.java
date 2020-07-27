package com.prolog.eis.service.base;

import java.util.List;

public interface BatchSqlService {
	
	@SuppressWarnings("rawtypes")
	public void pubInsertSql(List list,String tableName);
	
	@SuppressWarnings("rawtypes")
	public void pubUpdateSql(List list,String tableName);

	@SuppressWarnings("rawtypes")
	public void pubDeleteSql(List list,String tableName);
 


}
