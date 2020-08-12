
package com.prolog.eis.util;

import com.prolog.eis.dto.base.Coordinate;

public class PrologLocationUtils {
    /**
     * 生成坐标字符串
     * @param
     * @return
     */
    public static String splicingXYStr(Coordinate coordinate) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%06d", coordinate.getX()));
        sb.append("XY");
        sb.append(String.format("%06d", coordinate.getY()));
        return sb.toString();
    }

    public static String splicingXYStr(String coordinateStr){
        return PrologLocationUtils.splicingXYStr(PrologCoordinateUtils.analysis(coordinateStr));
    }

}
