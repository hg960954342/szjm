package com.prolog.eis.util;

import com.prolog.eis.dto.base.Coordinate;

public class PrologCoordinateUtils {

	/**
	 * 解析坐标
	 * @param coordinateStr
	 * @return
	 */
	public static Coordinate analysis(String coordinateStr) {
		Coordinate coordinate = new Coordinate();
		int layer  = Integer.valueOf(coordinateStr.substring(0, 2));
		int x = Integer.valueOf(coordinateStr.substring(2, 6));
		int y = Integer.valueOf(coordinateStr.substring(6, 10));
		coordinate.setLayer(layer);
		coordinate.setX(x);
		coordinate.setY(y);
		return coordinate;
	}
	/**
	 * 生成坐标字符串
	 * @param coordinateStr
	 * @return
	 */
	public static String splicingStr(int x,int y,int layer) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%02d", layer));
		sb.append(String.format("%04d",x));
		sb.append(String.format("%04d", y));
		return sb.toString();
	}
	
	public static String trimBothEndsChars(String srcStr, String splitter) {
	    String regex = "^" + splitter + "*|" + splitter + "*$";
	    return srcStr.replaceAll(regex, "");
	}
}
