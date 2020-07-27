package com.prolog.eis.util;

import java.util.List;

public class PrologInOfListToStringHelper {

	public static String getSqlStrByList(List<?> sqhList,String columnName) {  
		int splitNum = 1000;
		
        StringBuffer sql = new StringBuffer("");  
        if (sqhList != null) {  
            sql.append(" (").append(columnName).append (" IN ( ");  
            for (int i = 0; i < sqhList.size(); i++) {  
                sql.append("'").append(sqhList.get(i) + "',");  
                if ((i + 1) % splitNum == 0 && (i + 1) < sqhList.size()) {  
                    sql.deleteCharAt(sql.length() - 1);  
                    sql.append(" ) OR ").append(columnName).append (" IN (");  
                }  
            }  
            sql.deleteCharAt(sql.length() - 1);  
            sql.append(" ))");  
        }  
        return sql.toString();  
    }  
	
	public static String getNotInSqlStrByList(List<?> sqhList,String columnName) {  
		int splitNum = 1000;
		
        StringBuffer sql = new StringBuffer("");  
        if (sqhList != null) {  
            sql.append(" (").append(columnName).append (" NOT IN ( ");  
            for (int i = 0; i < sqhList.size(); i++) {  
                sql.append("'").append(sqhList.get(i) + "',");  
                if ((i + 1) % splitNum == 0 && (i + 1) < sqhList.size()) {  
                    sql.deleteCharAt(sql.length() - 1);  
                    sql.append(" ) AND ").append(columnName).append (" NOT IN (");  
                }  
            }  
            sql.deleteCharAt(sql.length() - 1);  
            sql.append(" ))");  
        }  
        return sql.toString();  
    } 
}
