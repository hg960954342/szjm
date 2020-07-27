package com.prolog.eis.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 针对站台通讯提供的方法，用来自增传输次数（每一个站台单独自增，不允许重复）
 * 
 * @author hh
 *
 */
public class IdsSeqUtils {
	private static Map<String, Object> map = new HashMap<String, Object>();
	private static Map<Integer, Object> tsjMap = new HashMap<Integer, Object>();
	//private static Map<Integer, Object> rkMap = new HashMap<Integer, Object>();

	public static int getSeqID(String stationNo) {
		if (!map.containsKey(stationNo)) {
			map.put(stationNo, 1);
			return 1;
		} else {
			int totalCount = (int) map.get(stationNo);
			++totalCount;
			if (totalCount > 100) {
				totalCount = 1;
			}
			map.put(stationNo, totalCount);
			return (int) map.get(stationNo);
		}
	}
	
	public static int getXKTargetAddress(Integer hangDao) {
		if (!tsjMap.containsKey(hangDao)) {
			tsjMap.put(hangDao, 1);
			return 1;
		} else {
			int totalAddress = (int) tsjMap.get(hangDao);
			if (totalAddress == 2) {
				totalAddress = 1;
			}else {
				totalAddress = 2;
			}
			tsjMap.put(hangDao, totalAddress);
 			return  totalAddress;
		}
	}
	
	

//	public static String getTargetAddress(Integer hangDao) {
//		if (!tsjMap.containsKey(hangDao)) {
//			tsjMap.put(hangDao, 1);
//			return "2000";
//		} else {
//			int totalAddress = (int) tsjMap.get(hangDao);
//			++totalAddress;
//			if (totalAddress > 2) {
//				totalAddress = 1;
//			}
//			tsjMap.put(hangDao, totalAddress);
//			int temp = (int) tsjMap.get(hangDao);
//			return temp == 1 ? "2000" : "3000";
//		}
//		int tsj= IdsSeqUtils.getXKTargetAddress(hangDao);
//		return tsj == 1 ? "2000" : "3000";
//	}
	
	public static int getRKTargetAddress(Integer hangDao) {
		int tsj= IdsSeqUtils.getXKTargetAddress(hangDao);
		return tsj;
//		if (!rkMap.containsKey(hangDao)) {
//			rkMap.put(hangDao, 1);
//			return 1;
//		} else {
//			int totalAddress = (int) rkMap.get(hangDao);
//			if (totalAddress == 2) {
//				totalAddress = 1;
//			}else {
//				totalAddress = 2;
//			}
//			rkMap.put(hangDao, totalAddress);
// 			return  totalAddress;
//		}
	}
}
