package com.prolog.eis.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PrologTaskIdUtils {

	
	public static synchronized String getTaskId() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr = dateFormat.format(new Date());
		int number = (int)((Math.random()*9+1)*1000);
		return dateStr + String.format("%04d", number);
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
