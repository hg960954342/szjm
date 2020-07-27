package com.prolog.eis.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;

import net.sf.json.JSONObject;

public class PrologStringUtils {

	/**
	 * 去除字符串末尾,
	 * 
	 * @author huhao
	 * @date 2018/10/22 09:29
	 */
	public static String getReplacStr(String str) {
		return str.endsWith(",") ? str.substring(0, str.length() - 1) : str;
	}

	/**
	 * clob转String
	 * 
	 * @author cb
	 * @date 2018/12/27
	 */
	public static String ClobtoString(Clob clob) {
		String reString = "";
		Reader is = null;
		try {
			is = clob.getCharacterStream();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 得到流
		BufferedReader br = new BufferedReader(is);
		String s = null;
		try {
			s = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		while (s != null) {
			// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
			sb.append(s);
			try {
				s = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		reString = sb.toString();
		return reString;
	}

	/**
	 * 生成UUID
	 * 
	 * @date 2019年2月15日 下午3:12:42
	 * @author dengss
	 * @return
	 */
	public static String newGUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");
	}

	/**
	 * 转义
	 * @author hh
	 */
	public static String getTaskJson(JSONObject json) {
 		return StringEscapeUtils.escapeJava(json.toString());

	}
	
	public static String getUnTaskJson(String json) {
 		return StringEscapeUtils.unescapeJava(json);

	}
}
