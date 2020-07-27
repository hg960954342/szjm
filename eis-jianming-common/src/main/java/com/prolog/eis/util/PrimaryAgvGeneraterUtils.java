package com.prolog.eis.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PrimaryAgvGeneraterUtils {
	private static PrimaryAgvGeneraterUtils primaryGenerater = null;
	private static String genKey = "agvMsgId";

	private static Map<String, Object> map = new HashMap<String, Object>();


	private PrimaryAgvGeneraterUtils() {
	}

	public static PrimaryAgvGeneraterUtils getInstance() {
		if (primaryGenerater == null) {
			synchronized (PrimaryAgvGeneraterUtils.class) {
				if (primaryGenerater == null) {
					primaryGenerater = new PrimaryAgvGeneraterUtils();
				}
			}
		}
		return primaryGenerater;
	}

	public synchronized String generaterNextNumber() {
		String id = null;
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssFFF");
		DecimalFormat df = new DecimalFormat("0000");
		String seqNo = getSeqID(genKey);
 		id = formatter.format(date) + df.format(1 + Integer.parseInt(seqNo));
 		return id;
	}
	

	public static String getSeqID(String genKey) {
		if (!map.containsKey(genKey)) {
			map.put(genKey, 1);
			return "1";
		} else {
			int totalCount = (int) map.get(genKey);
			++totalCount;
			if (totalCount > 9999) {
				totalCount = 1;
			}
			map.put(genKey, totalCount);
			return String.valueOf(map.get(genKey));
		}
	}
}
