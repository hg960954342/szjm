package com.prolog.eis.dao.base;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import com.prolog.eis.util.PrologStringUtils;
import com.prolog.framework.core.annotation.Column;

public class PrologSqlProvider {

	public String insertSql(List<Object> list, String tableName) {
		Field field;
		String column = "";
		// 通过反射获得list中的column
		Field[] file2 = list.get(0).getClass().getDeclaredFields();
		for (int i = 0; i < file2.length; i++) {
		    try {
				field=list.get(0).getClass().getDeclaredField(file2[i].getName());
			    Column filedCoulumn=field.getAnnotation(Column.class);
			    String name ="";
				if (filedCoulumn == null) {
					name = file2[i].getName();
				} else {
					name = filedCoulumn.value();
				}
				column += name + ",";
			} catch (NoSuchFieldException | SecurityException e) {
 				e.printStackTrace();
			}
 		}
		column = PrologStringUtils.getReplacStr(column);
		String sel = "select ";
		String unionSel = "";
		for (int j = 0; j < list.size(); j++) {
			Field[] fields = list.get(j).getClass().getDeclaredFields();
			Object object = list.get(j);
			for (int i = 0; i < fields.length; i++) {
				if (!fields[i].isAccessible()) {
					fields[i].setAccessible(true);
				}
				try {
					if (j >= 1) {
						if (i == 0) {
							unionSel += "union all select ";
						}
						// 判断结果类型，如果是字符串类型，就需要转换
						if (fields[i].get(object) instanceof String) {
							unionSel = unionSel + " '" + fields[i].get(object) + "' " + ",";
						} else if (fields[i].get(object) instanceof Date) {
 							unionSel = unionSel + " to_date(substr('" + fields[i].get(object) + "',1,10),'yyyy-mm-dd')"+ ",";
						} else {
 							unionSel = unionSel + fields[i].get(object) + ",";
						}
						if (i == fields.length - 1) {
							unionSel = PrologStringUtils.getReplacStr(unionSel) + " from  dual  ";
						}
					} else if (j == 0) {
						if (fields[i].get(object) instanceof String) {
							sel = sel + " '" + fields[i].get(object) + "' " + ",";
						} else if (fields[i].get(object) instanceof Date) {
							sel = sel + " to_date(substr('" + fields[i].get(object) + "',1,10),'yyyy-mm-dd')"+ ",";
						} else {
							sel += fields[i].get(object) + ",";
						}
  					}
 				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}

		}
		sel =PrologStringUtils.getReplacStr(sel) + " from  dual  ";
 		StringBuffer buffer = new StringBuffer();
		buffer.append("insert into ").append(tableName);
		buffer.append("(").append(column).append(") ");
		buffer.append(sel).append("\r\n");
		buffer.append(unionSel);
 		return buffer.toString();
	}

	public String updateSql(List<Object> list) {
		return null;
	}

}
