package com.prolog.eis.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.prolog.framework.utils.StringUtils;

import net.bytebuddy.asm.Advice.This;

public class PrologTaskIdUtils {

	private static Map<String, Object> map = new HashMap<String, Object>();
	private static final String taskId = "taskId";
	
	public static synchronized String getTaskId() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssFFF");
		String dateStr = dateFormat.format(new Date());
		
		int seqNo = getSeqID(PrologTaskIdUtils.taskId);
		
		return dateStr + String.format("%04d", seqNo);
	}
	
	public static int getSeqID(String genKey) {
		if (!map.containsKey(genKey)) {
			map.put(genKey, 1);
			return 1;
		} else {
			int totalCount = (int) map.get(genKey);
			++totalCount;
			if (totalCount > 9999) {
				totalCount = 1;
			}
			map.put(genKey, totalCount);
			return (int)map.get(genKey);
		}
	}
	
	public static void main(String[] args) {
		
		for (int i = 0; i < 20000; i++) {
			System.err.println(getTaskId());
//			try {
//				Thread.sleep(1);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		
	}
}
